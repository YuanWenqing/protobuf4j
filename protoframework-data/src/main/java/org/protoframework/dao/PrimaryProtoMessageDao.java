package org.protoframework.dao;

import com.google.common.collect.Maps;
import com.google.protobuf.Message;
import org.protoframework.sql.FieldValues;
import org.protoframework.sql.IExpression;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author: yuanwq
 * @date: 2018/7/23
 */
public class PrimaryProtoMessageDao<K, T extends Message> extends ProtoMessageDao<T>
    implements IPrimaryKeyMessageDao<K, T> {
  protected final String primaryKey;

  public PrimaryProtoMessageDao(@Nonnull Class<T> messageType, String primaryKey) {
    super(messageType);
    this.primaryKey = primaryKey;
  }

  @Override
  public String getPrimaryKey() {
    return primaryKey;
  }

  @Nullable
  @Override
  public T selectOneByPrimaryKey(@Nonnull K key) {
    return selectOne(FieldValues.eq(primaryKey, key));
  }

  @Override
  public Map<K, T> selectMultiByPrimaryKey(Collection<K> keys) {
    if (keys.isEmpty()) {
      return Collections.emptyMap();
    }
    List<T> items = selectAll(FieldValues.in(primaryKey, keys));
    if (items == null || items.isEmpty()) {
      return Collections.emptyMap();
    }
    Map<K, T> map = Maps.newLinkedHashMapWithExpectedSize(items.size());
    for (T item : items) {
      if (item == null) {
        continue;
      }
      K k = (K) messageHelper.getFieldValue(item, primaryKey);
      map.put(k, item);
    }
    return map;
  }

  @Override
  public int updateMessageByPrimaryKey(T newItem, T oldItem) {
    Object k = messageHelper.getFieldValue(oldItem, primaryKey);
    IExpression cond = FieldValues.eq(primaryKey, k);
    return updateMessage(newItem, oldItem, cond);
  }

  @Override
  public int deleteByPrimaryKey(K key) {
    return delete(FieldValues.eq(primaryKey, key));
  }

  @Override
  public int deleteMultiByPrimaryKey(Collection<K> keys) {
    if (keys.isEmpty()) {
      return 0;
    }
    return delete(FieldValues.in(primaryKey, keys));
  }
}
