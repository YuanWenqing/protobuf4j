package protobuf4j.orm.converter;

import com.google.protobuf.Descriptors;

public class LongFieldConverter implements IFieldConverter {
  @Override
  public boolean supportConversion(Descriptors.FieldDescriptor.JavaType javaType,
      Object fieldValue) {
    return javaType == Descriptors.FieldDescriptor.JavaType.LONG &&
        (fieldValue instanceof Long || fieldValue instanceof Integer);
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
    throw new TypeConversionException(Descriptors.FieldDescriptor.JavaType.LONG, fieldValue,
        getSqlValueType());
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
