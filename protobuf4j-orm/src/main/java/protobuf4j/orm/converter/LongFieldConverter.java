package protobuf4j.orm.converter;

import com.google.protobuf.Descriptors;

public class LongFieldConverter implements IFieldConverter {
  @Override
  public boolean supports(Descriptors.FieldDescriptor fieldDescriptor) {
    return fieldDescriptor.getJavaType() == Descriptors.FieldDescriptor.JavaType.LONG;
  }

  @Override
  public Class<?> getSqlValueType() {
    return Long.class;
  }

  @Override
  public Object toSqlValue(Descriptors.FieldDescriptor fieldDescriptor, Object fieldValue) {
    if (fieldValue instanceof Long || fieldValue instanceof Integer) {
      return ((Number) fieldValue).longValue();
    }
    throw new FieldConversionException(Descriptors.FieldDescriptor.JavaType.LONG, fieldValue,
        getSqlValueType());
  }

  @Override
  public Object fromSqlValue(Descriptors.FieldDescriptor fieldDescriptor, Object sqlValue) {
    if (sqlValue == null) {
      return 0L;
    } else if (sqlValue instanceof Long || sqlValue instanceof Integer) {
      return ((Number) sqlValue).longValue();
    }
    throw new FieldConversionException(Descriptors.FieldDescriptor.JavaType.LONG, sqlValue);
  }
}
