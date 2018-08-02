package org.protoframework.orm.dao;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.Message;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.protoframework.core.ProtoMessageHelper;
import org.protoframework.orm.sql.*;
import org.protoframework.orm.sql.clause.*;
import org.protoframework.orm.sql.expr.RawExpr;
import org.protoframework.util.ThreadLocalTimer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.sql.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.*;

/**
 * 处理Protobuf Message的DAO
 * <p>
 *
 * @param <T> 访问的数据表的数据元素类型
 * @author yuanwq
 */
public class ProtoMessageDao<T extends Message> implements IMessageDao<T> {
  protected static final ThreadLocalTimer timer = new ThreadLocalTimer();
  protected static final String SQL_INSERT_TEMPLATE = "INSERT INTO %s (%s) VALUES (%s);";
  protected static final String SQL_INSERT_IGNORE_TEMPLATE =
      "INSERT IGNORE INTO %s (%s) VALUES (%s);";
  /**
   * 访问的数据表的数据元素类型
   */
  protected final Class<T> messageType;
  protected final ProtoMessageHelper<T> messageHelper;
  protected final IProtoSqlConverter sqlConverter;
  protected final ProtoMessageRowMapper<T> messageMapper;
  /**
   * 访问的数据表名
   */
  protected final String tableName;
  protected final FromClause fromClause;
  /**
   * 记录dao日志的logger
   */
  protected final Logger daoLogger;
  /**
   * 记录执行的sql的logger
   */
  protected final DaoSqlLogger sqlLogger;
  protected JdbcTemplate jdbcTemplate;

  public ProtoMessageDao(@Nonnull Class<T> messageType) {
    this(messageType,
        (IProtoSqlConverter) SqlConverterRegistry.getInstance().findSqlConverter(messageType),
        null);
  }

  /**
   * @param tableName 为空，表示使用默认规则生成表名
   */
  public ProtoMessageDao(@Nonnull Class<T> messageType, IProtoSqlConverter sqlConverter,
      @Nullable String tableName) {
    this.messageType = checkNotNull(messageType);
    this.messageHelper = ProtoMessageHelper.getHelper(messageType);
    this.sqlConverter = sqlConverter;
    checkNotNull(this.sqlConverter, "no available sqlConverter for " + messageType.getName());
    this.messageMapper = new ProtoMessageRowMapper<>(messageType, this.sqlConverter);
    this.tableName =
        StringUtils.defaultIfBlank(tableName, this.sqlConverter.tableName(messageType));
    this.fromClause = QueryCreator.from(this.tableName);

    this.daoLogger = LoggerFactory
        .getLogger(getClass().getName() + "#" + messageHelper.getDescriptor().getFullName());
    this.sqlLogger = new DaoSqlLogger(messageHelper.getDescriptor().getFullName());
  }

  @Override
  public Class<T> getMessageType() {
    return messageType;
  }

  @Override
  public String getTableName() {
    return tableName;
  }

  public ProtoMessageHelper<T> getMessageHelper() {
    return messageHelper;
  }

  @Override
  public RowMapper<T> getMessageMapper() {
    return messageMapper;
  }

  @Override
  public JdbcTemplate getJdbcTemplate() {
    return jdbcTemplate;
  }

  public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  private List<Object> convertSqlValues(List<ISqlValue> sqlValues) {
    List<Object> values = Lists.newArrayListWithExpectedSize(sqlValues.size());
    for (ISqlValue sqlValue : sqlValues) {
      Object value;
      if (StringUtils.isBlank(sqlValue.getField())) {
        value = sqlValue.getValue();
      } else {
        value = sqlConverter.toSqlValue(messageType, sqlValue.getField(), sqlValue.getValue());
      }
      values.add(value);
    }
    return values;
  }

  ////////////////////////////// raw sql //////////////////////////////

  @Override
  public int doRawSql(@Nonnull RawSql rawSql) {
    SqlStatementExecution execution = new SqlStatementExecution(rawSql);
    timer.restart();
    try {
      return this.jdbcTemplate.update(execution.getStatementCreator());
    } finally {
      execution.log(sqlLogger.raw(), timer.stop(TimeUnit.MILLISECONDS));
    }
  }

  ////////////////////////////// insert //////////////////////////////

  /**
   * 插入记录
   */
  @Override
  public int insert(@Nonnull T message) {
    checkNotNull(message);
    return doInsert(buildInsertSql(message), null);
  }

  protected InsertSql buildInsertSql(@Nonnull T message) {
    InsertSql insertSql = QueryCreator.insertInto(tableName);
    for (FieldDescriptor fd : messageHelper.getFieldDescriptorList()) {
      if (messageHelper.isFieldSet(message, fd.getName())) {
        Object value = messageHelper.getFieldValue(message, fd.getName());
        insertSql.addField(fd.getName(), value);
      }
    }
    return insertSql;
  }

  @Override
  public Number insertReturnKey(@Nonnull T message) {
    checkNotNull(message);
    KeyHolder keyHolder = new GeneratedKeyHolder();
    int rows = doInsert(buildInsertSql(message), keyHolder);
    if (rows == 0) {
      throw new RuntimeException(
          "fail to insert into " + tableName + ": " + messageHelper.toString(message));
    }
    return keyHolder.getKey();
  }

  @Override
  public int insertIgnore(@Nonnull T message) {
    checkNotNull(message);
    InsertSql insertSql = buildInsertSql(message);
    insertSql.setIgnore(true);
    return doInsert(insertSql, null);
  }

  @Override
  public int doInsert(@Nonnull InsertSql insertSql, @Nullable KeyHolder keyHolder) {
    SqlStatementExecution execution = new SqlStatementExecution(insertSql);
    timer.restart();
    try {
      if (keyHolder == null) {
        return this.jdbcTemplate.update(execution.getStatementCreator());
      } else {
        return this.jdbcTemplate.update(execution.getStatementCreator(), keyHolder);
      }
    } finally {
      execution.log(sqlLogger.insert(), timer.stop(TimeUnit.MILLISECONDS));
    }
  }

  @Override
  public int[] insertMulti(List<T> messages) {
    return doInsertMulti(SQL_INSERT_TEMPLATE, messages);
  }

  @Override
  public int[] insertIgnoreMulti(List<T> messages) {
    return doInsertMulti(SQL_INSERT_IGNORE_TEMPLATE, messages);
  }

  /**
   * TODO: 抽象
   */
  private int[] doInsertMulti(String sqlTemplate, List<T> messages) {
    if (messages.isEmpty()) return new int[0];
    Set<String> used = getInsertFields(messages);
    final String sql = String.format(sqlTemplate, this.tableName, StringUtils.join(used, ","),
        StringUtils.repeat("?", ",", used.size()));
    timer.restart();
    try {
      return this.jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
        @Override
        public void setValues(PreparedStatement ps, int i) throws SQLException {
          T message = messages.get(i);
          int j = 1;
          for (String name : used) {
            FieldDescriptor fd = messageHelper.getFieldDescriptor(name);
            Object value = messageHelper.getFieldValue(message, name);
            value = sqlConverter.toSqlValue(fd, value);
            ps.setObject(j++, value);
          }
        }

        @Override
        public int getBatchSize() {
          return messages.size();
        }
      });
    } finally {
      sqlLogger.insert()
          .info("cost={}, {}, multi messages: {}", timer.stop(TimeUnit.MILLISECONDS), sql,
              Lists.transform(messages, messageHelper::toString));
    }
  }

  private LinkedHashSet<String> getInsertFields(Collection<T> messages) {
    LinkedHashSet<String> fields = Sets.newLinkedHashSet();
    for (T message : messages) {
      for (FieldDescriptor fd : messageHelper.getFieldDescriptorList()) {
        if (messageHelper.isFieldSet(message, fd.getName())) {
          fields.add(fd.getName());
        }
      }
    }
    return fields;
  }

  ////////////////////////////// iterator //////////////////////////////

  @Override
  public Iterator<T> iterator(int batch) {
    return iterator(new WhereClause().limit(batch));
  }

  @Override
  public Iterator<T> iterator(IExpression cond, int batch) {
    return iterator(new WhereClause().setCond(cond).limit(batch));
  }

  @Override
  public Iterator<T> iterator(@Nonnull WhereClause where) {
    checkNotNull(where);
    checkNotNull(where.getPagination(), "no pagination");
    if (where.getPagination().getLimit() <= 0) {
      return Collections.emptyIterator();
    }
    return new Iterator<T>() {
      Iterator<T> delegate;

      {
        setupNextIteration();
      }

      private void setupNextIteration() {
        delegate = ProtoMessageDao.this.selectAll(where).iterator();
        where.setPagination(where.getPagination().next());
      }

      @Override
      public boolean hasNext() {
        if (delegate == null) return false;
        if (delegate.hasNext()) return true;
        setupNextIteration();
        if (delegate.hasNext()) return true;
        delegate = null;
        return false;
      }

      @Override
      public T next() {
        if (!hasNext()) return null;
        return delegate.next();
      }
    };
  }

  ////////////////////////////// select //////////////////////////////

  @Override
  public T selectOne(IExpression cond) {
    WhereClause where = new WhereClause().setCond(cond).limit(1);
    return selectOne(where);
  }

  @Override
  public T selectOne(@Nonnull WhereClause where) {
    checkNotNull(where);
    if (where.getPagination() == null) {
      where.limit(1);
    }
    List<T> messages = selectAll(where);
    if (messages.isEmpty()) {
      return null;
    }
    return messages.get(0);
  }

  @Override
  public List<T> selectAll() {
    return selectAll(new WhereClause());
  }

  @Override
  public List<T> selectAll(IExpression cond) {
    return selectAll(new WhereClause().setCond(cond));
  }

  @Override
  public List<T> selectAll(@Nonnull WhereClause where) {
    checkNotNull(where);
    SelectClause select = new SelectClause().select(SqlUtil.SELECT_STAR);
    SelectSql sql = new SelectSql(select, fromClause, where);
    return doSelect(sql, messageMapper);
  }

  @Override
  public <V> List<V> doSelect(@Nonnull SelectSql selectSql, @Nonnull RowMapper<V> mapper) {
    checkNotNull(selectSql);
    SqlStatementExecution execution = new SqlStatementExecution(selectSql);
    timer.restart();
    try {
      return this.jdbcTemplate.query(execution.getStatementCreator(), mapper);
    } finally {
      execution.log(sqlLogger.select(), timer.stop(TimeUnit.MILLISECONDS));
    }
  }

  ////////////////////////////// delete //////////////////////////////

  @Override
  public int delete(IExpression cond) {
    DeleteSql deleteSql = new DeleteSql(fromClause, new WhereClause().setCond(cond));
    return doDelete(deleteSql);
  }

  @Override
  public int doDelete(@Nonnull DeleteSql deleteSql) {
    checkNotNull(deleteSql);
    SqlStatementExecution execution = new SqlStatementExecution(deleteSql);
    timer.restart();
    try {
      return this.jdbcTemplate.update(execution.getStatementCreator());
    } finally {
      execution.log(sqlLogger.delete(), timer.stop(TimeUnit.MILLISECONDS));
    }
  }

  ////////////////////////////// update //////////////////////////////

  @Override
  public int updateMessage(T newItem, T oldItem, IExpression cond) {
    SetClause setClause = makeSetClause(newItem, oldItem);
    return update(setClause, cond);
  }

  private SetClause makeSetClause(T newItem, T oldItem) {
    SetClause setClause = new SetClause();
    for (FieldDescriptor fd : messageHelper.getFieldDescriptorList()) {
      Object oldValue = messageHelper.getFieldValue(oldItem, fd.getName());
      Object newValue = messageHelper.getFieldValue(newItem, fd.getName());
      if (!Objects.equals(oldValue, newValue)) {
        setClause.setValue(fd.getName(), newValue);
      }
    }
    return setClause;
  }

  @Override
  public int update(@Nonnull SetClause setClause, @Nullable IExpression cond) {
    WhereClause where = new WhereClause().setCond(cond);
    UpdateSql updateSql = new UpdateSql(fromClause.getTableRef(), setClause, where);
    return doUpdate(updateSql);
  }

  @Override
  public int doUpdate(@Nonnull UpdateSql updateSql) {
    if (updateSql.getSet().isEmpty()) {
      return 0;
    }
    SqlStatementExecution execution = new SqlStatementExecution(updateSql);
    timer.restart();
    try {
      return this.jdbcTemplate.update(execution.getStatementCreator());
    } finally {
      execution.log(sqlLogger.update(), timer.stop(TimeUnit.MILLISECONDS));
    }
  }

  ////////////////////////////// aggregate ////////////////////////////

  protected <V> V doSelectFirst(SelectSql selectSql, RowMapper<V> mapper) {
    checkNotNull(selectSql);
    List<V> ret = doSelect(selectSql, mapper);
    if (ret == null || ret.isEmpty()) {
      return null;
    }
    return ret.get(0);
  }

  @Override
  public int count(@Nullable IExpression cond) {
    return count(SqlUtil.SELECT_COUNT, cond);
  }

  protected int count(@Nonnull SelectExpr countExpr, @Nullable IExpression cond) {
    checkNotNull(countExpr);
    SelectClause select = new SelectClause().select(countExpr);
    WhereClause where = new WhereClause().setCond(cond);
    SelectSql selectSql = new SelectSql(select, fromClause, where);
    Integer ret = doSelectFirst(selectSql, new SingleColumnRowMapper<>(Integer.class));
    return ret == null ? 0 : ret;
  }

  @Override
  public long sum(String column, IExpression cond) {
    checkArgument(StringUtils.isNotBlank(column));
    return sum(new RawExpr(column), cond);
  }

  @Override
  public long sum(@Nonnull IExpression expr, @Nullable IExpression cond) {
    checkNotNull(expr);
    IExpression sumExpr = SqlUtil.aggregateWrap("SUM", expr);
    SelectClause select = new SelectClause().select(sumExpr);
    WhereClause where = new WhereClause().setCond(cond);
    SelectSql selectSql = new SelectSql(select, fromClause, where);
    Long ret = doSelectFirst(selectSql, new SingleColumnRowMapper<>(Long.class));
    return ret == null ? 0 : ret;
  }

  @Override
  public <V> V max(String column, @Nullable IExpression cond) {
    checkArgument(StringUtils.isNotBlank(column));
    return max(new RawExpr(column), cond, getSingleColumnMapper(column));
  }

  @Override
  public <V> V max(@Nonnull IExpression expr, @Nullable IExpression cond,
      @Nonnull RowMapper<V> mapper) {
    checkNotNull(expr);
    IExpression maxExpr = SqlUtil.aggregateWrap("MAX", expr);
    SelectClause select = new SelectClause().select(maxExpr);
    WhereClause where = new WhereClause().setCond(cond);
    SelectSql selectSql = new SelectSql(select, fromClause, where);
    return doSelectFirst(selectSql, mapper);
  }

  protected <V> RowMapper<V> getSingleColumnMapper(String column) {
    FieldDescriptor fd = messageHelper.checkFieldDescriptor(column);
    // 与ProtoMessageRowMapper类似的方式处理我们约定的字段类型
    return new ProtoFieldRowMapper<>(messageHelper, sqlConverter, fd);
  }

  @Override
  public <V> V min(String column, @Nullable IExpression cond) {
    checkArgument(StringUtils.isNotBlank(column));
    return min(new RawExpr(column), cond, getSingleColumnMapper(column));
  }

  @Override
  public <V> V min(@Nonnull IExpression expr, @Nullable IExpression cond,
      @Nonnull RowMapper<V> mapper) {
    checkNotNull(expr);
    IExpression minExpr = SqlUtil.aggregateWrap("MIN", expr);
    SelectClause select = new SelectClause().select(minExpr);
    WhereClause where = new WhereClause().setCond(cond);
    SelectSql selectSql = new SelectSql(select, fromClause, where);
    return doSelectFirst(selectSql, mapper);
  }

  @Override
  public <GK> Map<GK, Integer> groupCount(String groupColumn) {
    return groupCount(groupColumn, null);
  }

  @Override
  public <GK> Map<GK, Integer> groupCount(String groupColumn, IExpression cond) {
    SelectClause select = new SelectClause();
    select.select(groupColumn);
    select.select(SqlUtil.SELECT_COUNT);
    WhereClause where = new WhereClause().setCond(cond);
    where.setGroupBy(new GroupByClause().by(groupColumn));
    SelectSql selectSql = new SelectSql(select, fromClause, where);
    RowMapper<Pair<GK, Integer>> mapper = getGroupCountMapper(groupColumn);
    List<Pair<GK, Integer>> ret = doSelect(selectSql, mapper);
    if (ret == null || ret.isEmpty()) {
      return Collections.emptyMap();
    }
    Map<GK, Integer> map = Maps.newLinkedHashMap();
    for (Pair<GK, Integer> pair : ret) {
      if (pair.getKey() == null) {
        continue;
      }
      map.put(pair.getKey(), pair.getValue());
    }
    return map;
  }

  protected <GK> RowMapper<Pair<GK, Integer>> getGroupCountMapper(String column) {
    FieldDescriptor fd = messageHelper.checkFieldDescriptor(column);
    // 与ProtobufRowMapper类似的方式处理我们约定的字段类型
    return new GroupCountMapper<>(fd);
  }

  @SuppressWarnings("unchecked")
  class GroupCountMapper<K> implements RowMapper<Pair<K, Integer>> {
    final FieldDescriptor fd;

    public GroupCountMapper(FieldDescriptor fd) {
      this.fd = fd;
    }

    @Override
    public Pair<K, Integer> mapRow(ResultSet rs, int rowNum) throws SQLException {
      Object obj = messageMapper.getColumnValue(rs, 1, fd);
      K k;
      if (obj == null) {
        k = null;
      } else {
        k = (K) sqlConverter.fromSqlValue(messageHelper, fd, obj);
      }
      int count = rs.getInt(2);
      return Pair.of(k, count);
    }
  }

  private class SqlStatementExecution {
    private final ISqlStatement sqlStatement;
    private final String sqlTemplate;
    private final List<Object> values;

    public SqlStatementExecution(ISqlStatement sqlStatement) {
      this.sqlStatement = sqlStatement;
      this.sqlTemplate = sqlStatement.toSqlTemplate(new StringBuilder()).toString();
      List<ISqlValue> sqlValues = sqlStatement.collectSqlValue(Lists.newArrayList());
      this.values = ProtoMessageDao.this.convertSqlValues(sqlValues);
    }

    public PreparedStatementCreator getStatementCreator() {
      return new PreparedStatementCreator() {
        @Override
        public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
          PreparedStatement ps = con.prepareStatement(sqlTemplate, Statement.RETURN_GENERATED_KEYS);
          if (values.isEmpty()) return ps;
          int i = 1;
          for (Object value : values) {
            ps.setObject(i++, value);
          }
          return ps;
        }
      };
    }

    public void log(Logger logger, long cost) {
      logger.info("cost={}, {}, values: {}, {}", cost, this.sqlTemplate, this.values,
          this.sqlStatement.toString().replace("\n", " "));
    }
  }
}
