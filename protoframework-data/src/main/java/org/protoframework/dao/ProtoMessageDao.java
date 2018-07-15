package org.protoframework.dao;

/**
 * Created by tuqc on 15-3-17.
 */

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.Message;
import org.apache.commons.lang3.StringUtils;
import org.protoframework.core.ProtoMessageHelper;
import org.protoframework.sql.DeleteSql;
import org.protoframework.sql.IExpression;
import org.protoframework.sql.SelectSql;
import org.protoframework.sql.UpdateSql;
import org.protoframework.sql.clause.*;
import org.protoframework.util.ThreadLocalTimer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.annotation.Nonnull;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * We make a convention that methods to search multiple beans in the inherited DAO Classes should be
 * named starting with 'list', like listXXXsByYYY(..), instead of 'find' naming in this abstract DAO
 * Class. We reserve the word 'find' for naming more general methods.<br>
 * 所有子类必须有一个public static final String TABLE_NAME = "...";
 *
 * @param <T> 访问的数据表的数据元素类型
 * @author yuanwq
 */
public class ProtoMessageDao<T extends Message> implements IMessageDao<T> {
  protected static final ThreadLocalTimer timer = new ThreadLocalTimer();

  /**
   * 访问的数据表的数据元素类型
   */
  protected final Class<T> messageType;
  /**
   * 访问的数据表名
   */
  protected final String tableName;
  protected final FromClause fromClause;
  protected final ProtoMessageHelper<T> messageHelper;
  protected final RowMapper<T> messageMapper;

  /**
   * 记录dao日志的logger
   */
  protected final Logger daoLogger;
  /**
   * 记录执行的sql的logger
   */
  protected final DaoSqlLogger sqlLogger;

  protected JdbcTemplate jdbcTemplate;

  protected ISqlConvention sqlConvention;

  protected static final String SQL_INSERT_TEMPLATE = "INSERT INTO %s (%s) VALUES (%s);";
  protected static final String SQL_INSERT_IGNORE_TEMPLATE =
      "INSERT IGNORE INTO %s (%s) VALUES (%s);";
  protected static final String SQL_UPDATE_TEMPLATE = "UPDATE %s %s %s;";
  protected static final String SQL_DELETE_TEMPLATE = "DELETE FROM %s %s";

  public ProtoMessageDao(Class<T> messageType) {
    this(messageType, ProtoSqls.tableName(messageType));
  }

  private ProtoMessageDao(Class<T> messageType, String tableName) {
    this.messageType = messageType;
    this.tableName = tableName;
    this.messageHelper = ProtoMessageHelper.getHelper(messageType);
    this.messageMapper = new ProtoMessageRowMapper<>(messageType);
    this.fromClause = FromClause.from(tableName);

    this.daoLogger = LoggerFactory
        .getLogger(getClass().getName() + "#" + messageHelper.getDescriptor().getFullName());
    this.sqlLogger = new DaoSqlLogger(messageHelper.getDescriptor().getFullName());
  }

  public Class<T> getMessageType() {
    return messageType;
  }

  public String getTableName() {
    return tableName;
  }

  public ProtoMessageHelper<T> getMessageHelper() {
    return messageHelper;
  }

  public RowMapper<T> getMessageMapper() {
    return messageMapper;
  }

  public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public JdbcTemplate getJdbcTemplate() {
    return jdbcTemplate;
  }

  ////////////////////////////// insert //////////////////////////////

  /**
   * 插入记录
   */
  @Override
  public boolean insert(@Nonnull T message) {
    int rows = doInsert(SQL_INSERT_TEMPLATE, message, null);
    return rows > 0;
  }

  @Override
  public Number insertReturnKey(@Nonnull T message) {
    KeyHolder keyHolder = new GeneratedKeyHolder();
    int rows = doInsert(SQL_INSERT_TEMPLATE, message, keyHolder);
    if (rows == 0) {
      throw new RuntimeException(
          "fail to insert into " + tableName + ": " + messageHelper.toString(message));
    }
    return keyHolder.getKey();
  }

  /**
   * 插入记录。如果记录不存在，插入；如果记录已经存在，什么也不做，直接返回。
   */
  @Override
  public boolean insertIgnore(@Nonnull T message) {
    int rows = doInsert(SQL_INSERT_IGNORE_TEMPLATE, message, null);
    return rows > 0;
  }

  /**
   * Warn: 实例化的dao中的方法不建议直接使用该方法
   */
  protected int doInsert(String sqlTemplate, T message, KeyHolder keyHolder) {
    Set<String> fields = getInsertFields(message);
    if (fields.isEmpty()) {
      throw new RuntimeException("empty message to insert into " + tableName);
    }
    final String sql = String.format(sqlTemplate, this.tableName, StringUtils.join(fields, ","),
        StringUtils.repeat("?", ",", fields.size()));
    PreparedStatementCreator creator = makeInsertCreator(sql, fields, message);
    timer.restart();
    try {
      if (keyHolder == null) {
        return getJdbcTemplate().update(creator);
      } else {
        return getJdbcTemplate().update(creator, keyHolder);
      }
    } finally {
      sqlLogger.insert().info("cost={}, {}, message: {}", timer.stop(TimeUnit.MILLISECONDS), sql,
          messageHelper.toString(message));
    }
  }

  private PreparedStatementCreator makeInsertCreator(String sql, Collection<String> fields,
      T message) {
    return new PreparedStatementCreator() {
      @Override
      public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
        PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        setValuesInner(ps, message, fields);
        return ps;
      }
    };
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
   * Warn: 实例化的dao中的方法不建议直接使用该方法
   */
  protected int[] doInsertMulti(String sqlTemplate, List<T> messages) {
    if (messages.isEmpty()) return new int[0];
    Set<String> used = getInsertFields(messages);
    final String sql = String.format(sqlTemplate, this.tableName, StringUtils.join(used, ","),
        StringUtils.repeat("?", ",", used.size()));
    try {
      return this.getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
        @Override
        public void setValues(PreparedStatement ps, int i) throws SQLException {
          T message = messages.get(i);
          setValuesInner(ps, message, used);
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

  private LinkedHashSet<String> getInsertFields(T message) {
    return getInsertFields(Collections.singletonList(message));
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

  private void setValuesInner(PreparedStatement ps, T message, Collection<String> fields)
      throws SQLException {
    int j = 1;
    for (String name : fields) {
      FieldDescriptor fd = messageHelper.getFieldDescriptor(name);
      Object value = messageHelper.getFieldValue(message, name);
      // TODO: 转换value
      value = ProtoSqls.sqlValue(fd, value);
      ps.setObject(j++, value);
    }
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
    Preconditions.checkNotNull(where);
    Preconditions.checkNotNull(where.getPagination(), "no pagination");
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
  public T selectOne(WhereClause where) {
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
    SelectClause select = new SelectClause().select(SelectExpr.STAR);
    SelectSql sql = new SelectSql(select, fromClause, where);
    return doSelect(sql, messageMapper);
  }

  @Override
  public <V> List<V> doSelect(@Nonnull SelectSql selectSql, RowMapper<V> mapper) {
    String sqlTemplate = selectSql.toSqlTemplate(new StringBuilder()).toString();
    List<Object> sqlValues = selectSql.collectSqlValue(Lists.newArrayList());
    timer.restart();
    try {
      return this.getJdbcTemplate()
          .query(DaoUtil.makeStatementCreator(sqlTemplate, sqlValues), mapper);
    } finally {
      sqlLogger.select()
          .info("cost={}, {}, values: {}", timer.stop(TimeUnit.MILLISECONDS), sqlTemplate,
              sqlValues);
    }
  }

  ////////////////////////////// delete //////////////////////////////

  @Override
  public int delete(IExpression cond) {
    DeleteSql deleteSql = new DeleteSql(fromClause, new WhereClause().setCond(cond));
    return doDelete(deleteSql);
  }

  @Override
  public int doDelete(DeleteSql deleteSql) {
    String sqlTemplate = deleteSql.toSqlTemplate(new StringBuilder()).toString();
    List<Object> sqlValues = deleteSql.collectSqlValue(Lists.newArrayList());
    timer.restart();
    try {
      return this.getJdbcTemplate().update(DaoUtil.makeStatementCreator(sqlTemplate, sqlValues));
    } finally {
      sqlLogger.delete()
          .info("cost={}, {}, values: {}", timer.stop(TimeUnit.MILLISECONDS), sqlTemplate,
              sqlValues);
    }
  }

  ////////////////////////////// update //////////////////////////////

  @Override
  public int updateItem(T newItem, T oldItem, IExpression cond) {
    SetClause setClause = makeSetClause(newItem, oldItem);
    WhereClause where = new WhereClause().setCond(cond);
    UpdateSql updateSql = new UpdateSql(fromClause.getTableRef(), setClause, where);
    return doUpdate(updateSql);
  }

  private SetClause makeSetClause(T newItem, T oldItem) {
    SetClause setClause = new SetClause();
    for (FieldDescriptor fd : messageHelper.getFieldDescriptorList()) {
      Object oldValue = messageHelper.getFieldValue(oldItem, fd.getName());
      Object newValue = messageHelper.getFieldValue(newItem, fd.getName());
      if (!Objects.equals(oldValue, newValue)) {
        setClause.setColumn(fd.getName(), newValue);
      }
    }
    return setClause;
  }

  @Override
  public int doUpdate(UpdateSql updateSql) {
    if (updateSql.getSet().isEmpty()) {
      return 0;
    }
    String sqlTemplate = updateSql.toSqlTemplate(new StringBuilder()).toString();
    List<Object> sqlValues = updateSql.collectSqlValue(Lists.newArrayList());
    timer.restart();
    try {
      return this.getJdbcTemplate().update(DaoUtil.makeStatementCreator(sqlTemplate, sqlValues));
    } finally {
      sqlLogger.update()
          .info("cost={}, {}, values: {}", timer.stop(TimeUnit.MILLISECONDS), sqlTemplate,
              sqlValues);
    }
  }

}
