package protobuf4j.orm.converter;

import com.google.protobuf.ByteString;
import com.google.protobuf.Descriptors;

public class ByteStringTypeConverter implements ITypeConverter {
  @Override
  public Descriptors.FieldDescriptor.JavaType getJavaType() {
    return Descriptors.FieldDescriptor.JavaType.BYTE_STRING;
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
    throw new TypeConversionException(getJavaType(), fieldValue, getSqlValueType());
  }

  @Override
  public Object fromSqlValue(Object sqlValue) {
    if (sqlValue instanceof String) {
      return ByteString.copyFromUtf8((String) sqlValue);
    }
    throw new TypeConversionException(getJavaType(), sqlValue);
  }
}
