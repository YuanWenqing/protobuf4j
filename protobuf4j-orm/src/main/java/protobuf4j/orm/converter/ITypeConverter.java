package protobuf4j.orm.converter;

import com.google.protobuf.Descriptors;

/**
 * converter to handle {@link Descriptors.FieldDescriptor.JavaType} conversion
 */
public interface ITypeConverter {
  /**
   * whether support conversion for {@code fieldDescriptor}
   *
   * @param fieldDescriptor
   * @return
   */
  boolean supports(Descriptors.FieldDescriptor fieldDescriptor);

  /**
   * type of converted sql-like value
   *
   * @return
   */
  Class<?> getSqlValueType();

  /**
   * convert {@code fieldValue} to sql-like value
   *
   * @param fieldValue to convert value
   * @return
   */
  Object toSqlValue(Object fieldValue);

  /**
   * parse from sql-like value
   *
   * @param sqlValue sql-like value to parse from
   * @return
   */
  Object fromSqlValue(Object sqlValue);
}
