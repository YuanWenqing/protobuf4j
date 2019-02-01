package protobuf4j.orm.converter;

import com.google.protobuf.ByteString;
import com.google.protobuf.Descriptors;

public class ByteStringFieldConverter implements IFieldConverter {
  @Override
  public Class<?> getSqlValueType() {
    return String.class;
  }

  @Override
  public Object toSqlValue(Object fieldValue) {
    if (fieldValue instanceof ByteString) {
      return ((ByteString) fieldValue).toStringUtf8();
    } else if (fieldValue instanceof String) {
      return fieldValue;
    }
    throw new FieldConversionException(Descriptors.FieldDescriptor.JavaType.BYTE_STRING, fieldValue,
        getSqlValueType());
  }

  @Override
  public Object fromSqlValue(Object sqlValue) {
    if (sqlValue == null) {
      return ByteString.copyFromUtf8("");
    } else if (sqlValue instanceof String) {
      return ByteString.copyFromUtf8((String) sqlValue);
    } else if (sqlValue instanceof ByteString) {
      return sqlValue;
    }
    throw new FieldConversionException(Descriptors.FieldDescriptor.JavaType.BYTE_STRING, sqlValue);
  }
}
