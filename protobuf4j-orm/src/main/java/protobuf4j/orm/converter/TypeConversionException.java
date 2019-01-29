package protobuf4j.orm.converter;

import com.google.protobuf.Descriptors;
import org.springframework.dao.TypeMismatchDataAccessException;

public class TypeConversionException extends TypeMismatchDataAccessException {

  /**
   * construction when failing to convert
   *
   * @param javaType
   * @param fieldValue
   * @param sqlValueType
   */
  public TypeConversionException(Descriptors.FieldDescriptor.JavaType javaType, Object fieldValue,
      Class<?> sqlValueType) {
    super("fail to convert to sql value, field.type=" + javaType + ", field.value=`" + fieldValue +
        "`, value.type=" + fieldValue.getClass().getName() + ", sqlValue.type=" +
        sqlValueType.getName());
  }

  /**
   * construction when failing to parse
   *
   * @param sqlValue
   * @param javaType
   */
  public TypeConversionException(Descriptors.FieldDescriptor.JavaType javaType, Object sqlValue) {
    super("fail to parse sql value, sqlValue=`" + sqlValue + "`, sqlValue.type=" +
        sqlValue.getClass().getName() + ", field.type=" + javaType);
  }
}
