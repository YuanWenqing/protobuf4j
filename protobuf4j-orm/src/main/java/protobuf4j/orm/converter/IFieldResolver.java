package protobuf4j.orm.converter;

import com.google.protobuf.Descriptors;

/**
 * author: yuanwq
 * date: 2018/7/20
 */
public interface IFieldResolver extends IFieldConverter {

  /**
   * 寻找{@code fd}的字段值对应的sql类型
   *
   * @see #toSqlValue(Descriptors.FieldDescriptor, Object)
   */
  Class<?> resolveSqlValueType(Descriptors.FieldDescriptor fd);
}
