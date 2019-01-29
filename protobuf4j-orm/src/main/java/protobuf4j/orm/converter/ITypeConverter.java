package protobuf4j.orm.converter;

import com.google.protobuf.Descriptors;

/**
 * converter to handle {@link Descriptors.FieldDescriptor.JavaType} conversion
 */
public interface ITypeConverter {
  /**
   * which JavaType this converter supports
   *
   * @return
   */
  Descriptors.FieldDescriptor.JavaType getJavaType();

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
