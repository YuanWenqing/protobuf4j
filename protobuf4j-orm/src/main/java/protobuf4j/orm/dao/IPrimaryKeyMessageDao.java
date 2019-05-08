package protobuf4j.orm.dao;

import java.util.Collection;
import java.util.Map;

/**
 * author: yuanwq
 * date: 2018/7/23
 */
public interface IPrimaryKeyMessageDao<K, T> extends IMessageDao<T> {
  /**
   * 主键字段名
   */
  String getPrimaryKey();

  /**
   * 通过主键值查找一条数据
   *
   * @param key 主键值
   */
  T selectOneByPrimaryKey(K key);

  /**
   * 通过主键值集合查找多条数据
   *
   * @param keys 主键值集合
   * @return 主键值到相应数据的map
   */
  Map<K, T> selectMultiByPrimaryKey(Collection<K> keys);

  /**
   * 根据主键值更新数据（变化的字段）
   * <p>
   * Warn: {@code newItem}和{@code oldItem}中必须都要有主键值，且相同
   *
   * @param newItem 新数据
   * @param oldItem 旧数据
   * @return 变动的数据条数，实际情况中若没有发生字段更新则返回0，否则返回1
   */
  int updateMessageByPrimaryKey(T newItem, T oldItem);

  /**
   * 根据主键值删除一条数据
   *
   * @param key 主键值
   * @return 变动的数据条数
   */
  int deleteByPrimaryKey(K key);

  /**
   * 根据主键值删除多条数据
   *
   * @param keys 主键值集合
   * @return 变动的数据条数
   */
  int deleteMultiByPrimaryKey(Collection<K> keys);
}
