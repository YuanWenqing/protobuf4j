package protobuf4j.orm.converter;

import com.google.protobuf.Descriptors;

/**
 * handle type conversion between value of field and sql-style value
 */
public interface IFieldValueConverter {
  /**
   * whether support conversion to sql-style value
   *
   * @param fieldDescriptor
   * @param fieldValue
   * @return
   */
  boolean supportConversion(Descriptors.FieldDescriptor fieldDescriptor, Object fieldValue);

  /**
   * type of converted sql-style value
   *
   * @return
   */
  Class<?> getSqlValueType();

  /**
   * convert {@code fieldValue} to sql-style value
   *
   * @param fieldValue to convert value
   * @return
   */
  Object toSqlValue(Object fieldValue);

  /**
   * parse from sql-style value
   *
   * @param sqlValue sql-style value to parse from
   * @return
   */
  Object fromSqlValue(Object sqlValue);
}
