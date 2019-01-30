package protobuf4j.orm.converter;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Timestamp;
import com.google.protobuf.util.Timestamps;

public class TimestampFieldConverter implements IFieldValueConverter {
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
    } else if (fieldValue instanceof java.sql.Timestamp) {
      return fieldValue;
    }
    throw new FieldConversionException(Descriptors.FieldDescriptor.JavaType.DOUBLE, fieldValue,
        getSqlValueType());
  }

  @Override
  public Object fromSqlValue(Object sqlValue) {
    if (sqlValue == null) {
      return Timestamps.fromMillis(0L);
    } else if (sqlValue instanceof java.sql.Timestamp) {
      return Timestamps.fromMillis(((java.sql.Timestamp) sqlValue).getTime());
    } else if (sqlValue instanceof Timestamp) {
      return sqlValue;
    }
    throw new FieldConversionException(Descriptors.FieldDescriptor.JavaType.DOUBLE, sqlValue);
  }
}
