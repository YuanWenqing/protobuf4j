package protobuf4j.orm.converter;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Timestamp;
import com.google.protobuf.util.Timestamps;

public class TimestampFieldConverter implements IFieldConverter {
  @Override
  public boolean supports(Descriptors.FieldDescriptor fieldDescriptor) {
    return fieldDescriptor.getJavaType() == Descriptors.FieldDescriptor.JavaType.MESSAGE;
  }

  @Override
  public Class<?> getSqlValueType() {
    return java.sql.Timestamp.class;
  }

  @Override
  public Object toSqlValue(Descriptors.FieldDescriptor fieldDescriptor, Object fieldValue) {
    if (fieldValue instanceof Timestamp) {
      return new java.sql.Timestamp(Timestamps.toMillis((Timestamp) fieldValue));
    } else if (fieldValue instanceof Long || fieldValue instanceof Integer) {
      return new java.sql.Timestamp(((Number) fieldValue).longValue());
    } else if (fieldValue instanceof java.sql.Timestamp) {
      return fieldValue;
    }
    throw new FieldConversionException(
        "fail to convert to sql value for timestamp field, fieldValue=" +
            FieldConversionException.toString(fieldValue) + ", sqlValueType=" +
            getSqlValueType().getName());
  }

  @Override
  public Object fromSqlValue(Descriptors.FieldDescriptor fieldDescriptor, Object sqlValue) {
    if (sqlValue == null) {
      return Timestamps.fromMillis(0L);
    } else if (sqlValue instanceof java.sql.Timestamp) {
      return Timestamps.fromMillis(((java.sql.Timestamp) sqlValue).getTime());
    } else if (sqlValue instanceof Timestamp) {
      return sqlValue;
    }
    throw new FieldConversionException("fail to parse sql value for timestamp field, sqlValue=" +
        FieldConversionException.toString(sqlValue));
  }
}
