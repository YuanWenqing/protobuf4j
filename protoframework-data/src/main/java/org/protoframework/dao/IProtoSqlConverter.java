package org.protoframework.dao;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;
import org.protoframework.core.ProtoMessageHelper;

/**
 * @author: yuanwq
 * @date: 2018/7/20
 */
public interface IProtoSqlConverter extends ISqlConverter<Message> {
  /**
   * 将{@code fd}字段的值{@code value}转换为对应sql类型的值
   */
  Object toSqlValue(Descriptors.FieldDescriptor fd, Object value);

  /**
   * 判断{@code fd}是否是一个时间戳字段
   */
  boolean isTimestampField(Descriptors.FieldDescriptor fd);

  /**
   * 将sql类型的值{@code sqlValue}转换为{@code fd}字段的类型的值
   */
  <M extends Message> Object fromSqlValue(ProtoMessageHelper<M> helper,
      Descriptors.FieldDescriptor fd, Object sqlValue);

  /**
   * 寻找{@code fd}的字段值对应的sql类型
   */
  Class<?> resolveSqlValueType(Descriptors.FieldDescriptor fd);
}
