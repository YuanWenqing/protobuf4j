package protobuf4j.orm.converter;

import com.google.protobuf.Descriptors;

public class LongTypeConverter implements ITypeConverter {
  @Override
  public boolean supports(Descriptors.FieldDescriptor fieldDescriptor) {
    return fieldDescriptor.getJavaType() == Descriptors.FieldDescriptor.JavaType.LONG;
  }

  @Override
  public Class<?> getSqlValueType() {
    return Long.class;
  }

  @Override
  public Object toSqlValue(Object fieldValue) {
    if (fieldValue instanceof Long || fieldValue instanceof Integer) {
      return ((Number) fieldValue).longValue();
    }
    throw new TypeConversionException(Descriptors.FieldDescriptor.JavaType.LONG, fieldValue, getSqlValueType());
  }

  @Override
  public Object fromSqlValue(Object sqlValue) {
    if (sqlValue == null) {
      return 0L;
    }
    if (sqlValue instanceof Long || sqlValue instanceof Integer) {
      return ((Number) sqlValue).longValue();
    }
    throw new TypeConversionException(Descriptors.FieldDescriptor.JavaType.LONG, sqlValue);
  }
}
