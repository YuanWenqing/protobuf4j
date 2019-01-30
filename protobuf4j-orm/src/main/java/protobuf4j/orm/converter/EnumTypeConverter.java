package protobuf4j.orm.converter;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Internal;

public class EnumTypeConverter implements ITypeConverter {
  @Override
  public boolean supports(Descriptors.FieldDescriptor fieldDescriptor) {
    return fieldDescriptor.getJavaType() == Descriptors.FieldDescriptor.JavaType.ENUM;
  }

  @Override
  public Class<?> getSqlValueType() {
    return Integer.class;
  }

  @Override
  public Object toSqlValue(Object fieldValue) {
    if (fieldValue instanceof Internal.EnumLite) {
      return ((Internal.EnumLite) fieldValue).getNumber();
    }
    if (fieldValue instanceof Integer) {
      return fieldValue;
    }
    throw new TypeConversionException(Descriptors.FieldDescriptor.JavaType.ENUM, fieldValue,
        getSqlValueType());
  }

  @Override
  public Object fromSqlValue(Object sqlValue) {
    if (sqlValue == null) {
      return 0;
    }
    if (sqlValue instanceof Integer) {
      return sqlValue;
    }
    throw new TypeConversionException(Descriptors.FieldDescriptor.JavaType.ENUM, sqlValue);
  }
}