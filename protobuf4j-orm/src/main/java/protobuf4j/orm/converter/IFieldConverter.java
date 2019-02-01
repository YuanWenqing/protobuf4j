package protobuf4j.orm.converter;

import com.google.protobuf.Descriptors;

/**
 * handle type conversion between value of field and sql-style value
 */
public interface IFieldConverter {
  /**
   * check if support conversion for this field {@code fieldDescriptor}
   *
   * @param fieldDescriptor
   * @return
   */
  boolean supports(Descriptors.FieldDescriptor fieldDescriptor);

  /**
   * type of converted sql-style value
   *
   * @return
   */
  Class<?> getSqlValueType();

  /**
   * convert {@code fieldValue} of field {@code fieldDescriptor} to sql-style value
   *
   * @param fieldDescriptor
   * @param fieldValue      to convert value
   * @return
   */
  Object toSqlValue(Descriptors.FieldDescriptor fieldDescriptor, Object fieldValue);

  /**
   * parse from sql-style value to value of field {@code fieldDescriptor}
   *
   * @param fieldDescriptor
   * @param sqlValue        sql-style value to parse from
   * @return
   */
  Object fromSqlValue(Descriptors.FieldDescriptor fieldDescriptor, Object sqlValue);
}
