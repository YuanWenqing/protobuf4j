package protobuf4j.orm.converter;

import com.google.protobuf.Descriptors;
import org.springframework.dao.TypeMismatchDataAccessException;

public class FieldConversionException extends TypeMismatchDataAccessException {

  /**
   * generic construction
   *
   * @param msg
   */
  public FieldConversionException(String msg) {
    super(msg);
  }

  /**
   * construction when failing to convert
   *
   * @param javaType
   * @param fieldValue
   * @param sqlValueType
   */
  public FieldConversionException(Descriptors.FieldDescriptor.JavaType javaType, Object fieldValue,
      Class<?> sqlValueType) {
    super("fail to convert to sql value, javaType=" + javaType + ", fieldValue=" +
        toString(fieldValue) + ", sqlValueType=" + sqlValueType.getName());
  }

  /**
   * construction when failing to parse
   *
   * @param sqlValue
   * @param javaType
   */
  public FieldConversionException(Descriptors.FieldDescriptor.JavaType javaType, Object sqlValue) {
    super("fail to parse sql value, sqlValue=" + toString(sqlValue) + ", javaType=" + javaType);
  }

  private static String toString(Object value) {
    if (value == null) {
      return "null";
    }
    return String.format("`%s`[%s]", value, value.getClass().getName());
  }
}
