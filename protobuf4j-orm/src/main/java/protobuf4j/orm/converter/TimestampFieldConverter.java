package protobuf4j.orm.converter;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Timestamp;
import com.google.protobuf.util.Timestamps;

public class TimestampFieldConverter implements IFieldConverter {
  @Override
  public boolean supportConversion(Descriptors.FieldDescriptor.JavaType javaType,
      Object fieldValue) {
    return javaType == Descriptors.FieldDescriptor.JavaType.MESSAGE &&
        (fieldValue instanceof Timestamp || fieldValue instanceof Long);
  }

  @Override
  public Class<?> getSqlValueType() {
    return java.sql.Timestamp.class;
  }

  @Override
  public Object toSqlValue(Object fieldValue) {
    if (fieldValue instanceof Timestamp) {
      return new java.sql.Timestamp(Timestamps.toMillis((Timestamp) fieldValue));
    } else if (fieldValue instanceof Long) {
      return new java.sql.Timestamp((Long) fieldValue);
    }
    throw new TypeConversionException(Descriptors.FieldDescriptor.JavaType.DOUBLE, fieldValue,
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
    throw new TypeConversionException(Descriptors.FieldDescriptor.JavaType.DOUBLE, sqlValue);
  }
}
