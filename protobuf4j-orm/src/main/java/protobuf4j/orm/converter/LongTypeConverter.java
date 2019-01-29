package protobuf4j.orm.converter;

import com.google.protobuf.Descriptors;

public class LongTypeConverter implements ITypeConverter {
  @Override
  public Descriptors.FieldDescriptor.JavaType getJavaType() {
    return Descriptors.FieldDescriptor.JavaType.LONG;
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
    throw new TypeConversionException(getJavaType(), fieldValue, getSqlValueType());
  }

  @Override
  public Object fromSqlValue(Object sqlValue) {
    if (sqlValue instanceof Long) {
      return sqlValue;
    }
    throw new TypeConversionException(getJavaType(), sqlValue);
  }
}
