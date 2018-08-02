package org.protoframework.dao;

import org.protoframework.sql.*;
import org.protoframework.sql.clause.SetClause;
import org.protoframework.sql.clause.WhereClause;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.KeyHolder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 基本的接口
 */
public interface IMessageDao<T> {
  Class<T> getMessageType();

  String getTableName();

  RowMapper<T> getMessageMapper();

  JdbcTemplate getJdbcTemplate();

  /**
   * 新增一条数据
   *
   * @return 插入的条数
   */
  int insert(@Nonnull T message);

  /**
   * 新增一条数据并返回主键
   *
   * @return 主键
   */
  Number insertReturnKey(@Nonnull T message);

  /**
   * 新增一条数据，唯一键冲突时会忽略，导致新增失败
   *
   * @return 插入的条数
   */
  int insertIgnore(@Nonnull T message);

  /**
   * insert sql
   */
  int doInsert(@Nonnull InsertSql insertSql, @Nullable KeyHolder keyHolder);

  /**
   * 批量新增多条数据
   *
   * @return 每条数据是否新增成功，与{@code messages}下标一致
   */
  int[] insertMulti(List<T> messages);

  /**
   * 批量新增多条数据，唯一键冲突时会忽略，导致新增失败
   *
   * @return 每条数据是否新增成功，与{@code messages}下标一致
   */
  int[] insertIgnoreMulti(List<T> messages);

  /**
   * 根据条件查找一条数据
   */
  T selectOne(@Nullable IExpression cond);

  /**
   * 根据where子句查找一条数据
   *
   * @param where 遍历条件、排序和分页配置
   */
  T selectOne(@Nonnull WhereClause where);

  /**
   * 返回表中的所有数据
   * <p>
   * 注意：表太大时慎用！
   */
  List<T> selectAll();

  /**
   * 根据条件返回所有数据
   * <p>
   * 注意：条件需要制定的范围小一些，不要一次取太多！
   */
  List<T> selectAll(@Nullable IExpression cond);

  /**
   * 根据where子句返回所有数据
   *
   * @param where 遍历条件、排序和分页配置
   */
  List<T> selectAll(@Nonnull WhereClause where);

  /**
   * select方法
   */
  <V> List<V> doSelect(@Nonnull SelectSql selectSql, @Nonnull RowMapper<V> rowMapper);

  /**
   * 获取表上的一个遍历器
   *
   * @param batch 分批取数据时每批数据的条数
   */
  Iterator<T> iterator(int batch);

  /**
   * 根据条件，获取表上的一个遍历器
   *
   * @param cond  遍历条件，null表示全表
   * @param batch 分批取数据时每批数据的条数
   */
  Iterator<T> iterator(@Nullable IExpression cond, int batch);

  /**
   * 根据条件和排序，获取表上的一个遍历器
   *
   * @param where 遍历条件、排序和分页配置，必须配置好分页子句
   */
  Iterator<T> iterator(@Nonnull WhereClause where);

  /**
   * 根据条件删除数据
   *
   * @return 删除的数据条数
   */
  int delete(@Nullable IExpression cond);

  /**
   * @return 删除的数据条数
   */
  int doDelete(@Nonnull DeleteSql deleteSql);

  /**
   * 根据条件，更新新旧数据的变动字段
   *
   * @return 更新的数据条数
   */
  int updateMessage(T newItem, T oldItem, IExpression cond);

  /**
   * 根据条件更新字段
   *
   * @return 更新的数据条数
   */
  int update(@Nonnull SetClause setClause, @Nullable IExpression cond);

  /**
   * @return 更新的数据条数
   */
  int doUpdate(@Nonnull UpdateSql updateSql);

  /**
   * @return 影响的数据条数
   */
  int doRawSql(@Nonnull RawSql rawSql);

  /**
   * 根据条件count
   *
   * @param cond null表示全表
   */
  int count(@Nullable IExpression cond);

  /**
   * 根据条件，对指定列{@code column}求和
   *
   * @param cond null表示全表
   */
  long sum(String column, @Nullable IExpression cond);

  /**
   * 根据条件对表达式{@code expr}求和
   *
   * @param cond null表示全表
   */
  long sum(@Nonnull IExpression expr, @Nullable IExpression cond);

  /**
   * 根据条件，求指定列{@code column}最大值
   *
   * @param cond null表示全表
   */
  <V> V max(String column, @Nullable IExpression cond);

  /**
   * 根据条件，求表达式{@code expr}最大值
   *
   * @param cond null表示全表
   */
  <V> V max(@Nonnull IExpression expr, @Nullable IExpression cond, @Nonnull RowMapper<V> mapper);

  /**
   * 根据条件，求指定列{@code column}最小值
   *
   * @param cond null表示全表
   */
  <V> V min(String column, @Nullable IExpression cond);

  /**
   * 根据条件，求表达式{@code expr}最小值
   *
   * @param cond null表示全表
   */
  <V> V min(@Nonnull IExpression expr, @Nullable IExpression cond, @Nonnull RowMapper<V> mapper);

  /**
   * 对字段{@code groupColumn}进行分组聚合计数
   */
  <GK> Map<GK, Integer> groupCount(String groupColumn);

  /**
   * 根据条件对字段{@code groupColumn}进行分组聚合计数
   */
  <GK> Map<GK, Integer> groupCount(String groupColumn, @Nullable IExpression cond);
}
