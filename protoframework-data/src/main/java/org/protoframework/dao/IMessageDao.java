package org.protoframework.dao;

import org.protoframework.sql.DeleteSql;
import org.protoframework.sql.IExpression;
import org.protoframework.sql.SelectSql;
import org.protoframework.sql.UpdateSql;
import org.protoframework.sql.clause.SetClause;
import org.protoframework.sql.clause.WhereClause;
import org.springframework.jdbc.core.RowMapper;

import javax.annotation.Nonnull;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by zqs on 2017/2/27. 接口抽取
 */
public interface IMessageDao<T> {

  /**
   * 新增一条数据
   *
   * @return 是否成功
   */
  boolean insert(@Nonnull T message);

  /**
   * 新增一条数据并返回主键
   *
   * @return 主键
   */
  Number insertReturnKey(@Nonnull T message);

  /**
   * 新增一条数据，唯一键冲突时会忽略，导致新增失败
   *
   * @return 是否成功
   */
  boolean insertIgnore(@Nonnull T message);

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
  T selectOne(IExpression cond);

  /**
   * 根据where子句查找一条数据
   *
   * @param where 遍历条件、排序和分页配置
   */
  T selectOne(WhereClause where);

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
  List<T> selectAll(IExpression cond);

  /**
   * 根据where子句返回所有数据
   *
   * @param where 遍历条件、排序和分页配置
   */
  List<T> selectAll(@Nonnull WhereClause where);

  /**
   * select方法
   */
  <V> List<V> doSelect(SelectSql selectSql, RowMapper<V> rowMapper);

  /**
   * 获取表上的一个遍历器
   *
   * @param batch 分批取数据时每批数据的条数
   */
  Iterator<T> iterator(int batch);

  /**
   * 根据条件，获取表上的一个遍历器
   *
   * @param cond  遍历条件
   * @param batch 分批取数据时每批数据的条数
   */
  Iterator<T> iterator(IExpression cond, int batch);

  /**
   * 根据条件和排序，获取表上的一个遍历器
   *
   * @param where 遍历条件、排序和分页配置
   */
  Iterator<T> iterator(@Nonnull WhereClause where);

  /**
   * 根据条件删除数据
   *
   * @return 删除的数据条数
   */
  int delete(IExpression cond);

  /**
   * @return 删除的数据条数
   */
  int doDelete(DeleteSql deleteSql);

  /**
   * 根据条件，更新新旧数据的变动字段
   *
   * @return 更新的数据条数
   */
  int updateItem(T newItem, T oldItem, IExpression cond);

  /**
   * 根据条件更新字段
   *
   * @return 更新的数据条数
   */
  int update(SetClause setClause);

  /**
   * 根据条件更新字段
   *
   * @return 更新的数据条数
   */
  int update(SetClause setClause, IExpression cond);

  /**
   * @return 更新的数据条数
   */
  int doUpdate(UpdateSql updateSql);

  /**
   * 根据条件count
   */
  int count(IExpression cond);

  /**
   * 返回整个表的数据总条数
   */
  int countAll();

  /**
   * 根据条件，对指定列求和
   */
  long sum(String field, IExpression cond);

  /**
   * 对字段{@code groupField}进行分组聚合计数
   */
  <GK> Map<GK, Integer> groupCount(String groupField);

  /**
   * 根据条件对字段{@code groupField}进行分组聚合计数
   */
  <GK> Map<GK, Integer> groupCount(String groupField, IExpression cond);
}
