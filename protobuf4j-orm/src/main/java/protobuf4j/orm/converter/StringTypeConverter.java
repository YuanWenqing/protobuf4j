package protobuf4j.orm.converter;

import com.google.protobuf.Descriptors;

public class StringTypeConverter implements ITypeConverter {
  @Override
  public boolean supports(Descriptors.FieldDescriptor fieldDescriptor) {
    return fieldDescriptor.getJavaType() == Descriptors.FieldDescriptor.JavaType.STRING;
  }

  @Override
  public Class<?> getSqlValueType() {
    return String.class;
  }

  @Override
  public Object toSqlValue(Object fieldValue) {
    if (fieldValue == null) {
      return "";
    }
    if (fieldValue instanceof String) {
      return fieldValue;
    }
    throw new TypeConversionException(Descriptors.FieldDescriptor.JavaType.STRING, fieldValue,
        getSqlValueType());
  }

  @Override
  public Object fromSqlValue(Object sqlValue) {
    if (sqlValue == null) {
      return "";
    }
    if (sqlValue instanceof String) {
      return sqlValue;
    }
    throw new TypeConversionException(Descriptors.FieldDescriptor.JavaType.STRING, sqlValue);
  }
}
