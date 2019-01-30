package protobuf4j.orm.converter;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Timestamp;
import com.google.protobuf.util.Timestamps;

public class TimestampFieldConverter implements IFieldTypeConverter {
  @Override
  public boolean supportConversion(Descriptors.FieldDescriptor fieldDescriptor, Object fieldValue) {
    return fieldDescriptor.getJavaType() == Descriptors.FieldDescriptor.JavaType.MESSAGE &&
        (fieldValue instanceof Timestamp || fieldValue instanceof Long ||
            fieldValue instanceof Integer);
  }

  @Override
  public Class<?> getSqlValueType() {
    return java.sql.Timestamp.class;
  }

  @Override
  public Object toSqlValue(Object fieldValue) {
    if (fieldValue instanceof Timestamp) {
      return new java.sql.Timestamp(Timestamps.toMillis((Timestamp) fieldValue));
    } else if (fieldValue instanceof Long || fieldValue instanceof Integer) {
      return new java.sql.Timestamp(((Number) fieldValue).longValue());
    }
    throw new FieldConversionException(Descriptors.FieldDescriptor.JavaType.DOUBLE, fieldValue,
        getSqlValueType());
  }

  @Override
  public Object fromSqlValue(Object sqlValue) {
    if (sqlValue == null) {
      return Timestamps.fromMillis(0L);
    }
    if (sqlValue instanceof java.sql.Timestamp) {
      return Timestamps.fromMillis(((java.sql.Timestamp) sqlValue).getTime());
    }
    throw new FieldConversionException(Descriptors.FieldDescriptor.JavaType.DOUBLE, sqlValue);
  }
}
