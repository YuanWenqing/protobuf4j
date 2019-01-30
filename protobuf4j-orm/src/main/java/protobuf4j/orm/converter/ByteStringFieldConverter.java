package protobuf4j.orm.converter;

import com.google.protobuf.ByteString;
import com.google.protobuf.Descriptors;

public class ByteStringFieldConverter implements IFieldConverter {
  @Override
  public boolean supportConversion(Descriptors.FieldDescriptor fieldDescriptor, Object fieldValue) {
    return fieldDescriptor.getJavaType() == Descriptors.FieldDescriptor.JavaType.BYTE_STRING &&
        fieldValue instanceof ByteString;
  }

  @Override
  public Class<?> getSqlValueType() {
    return String.class;
  }

  @Override
  public Object toSqlValue(Object fieldValue) {
    if (fieldValue instanceof ByteString) {
      return ((ByteString) fieldValue).toStringUtf8();
    }
    throw new TypeConversionException(Descriptors.FieldDescriptor.JavaType.BYTE_STRING, fieldValue,
        getSqlValueType());
  }

  @Override
  public Object fromSqlValue(Object sqlValue) {
    if (sqlValue == null) {
      return ByteString.EMPTY;
    }
    if (sqlValue instanceof String) {
      return ByteString.copyFromUtf8((String) sqlValue);
    }
    throw new TypeConversionException(Descriptors.FieldDescriptor.JavaType.BYTE_STRING, sqlValue);
  }
}
