package protobuf4j.orm.converter;

import com.google.protobuf.Descriptors;

public class StringTypeConverter implements ITypeConverter {
  @Override
  public Descriptors.FieldDescriptor.JavaType getJavaType() {
    return Descriptors.FieldDescriptor.JavaType.STRING;
  }

  @Override
  public Class<?> getSqlValueType() {
    return String.class;
  }

  @Override
  public Object toSqlValue(Object fieldValue) {
    if (fieldValue instanceof String) {
      return fieldValue;
    }
    return String.valueOf(fieldValue);
  }

  @Override
  public Object fromSqlValue(Object sqlValue) {
    if (sqlValue instanceof String) {
      return sqlValue;
    }
    throw new TypeConversionException(getJavaType(), sqlValue);
  }
}
