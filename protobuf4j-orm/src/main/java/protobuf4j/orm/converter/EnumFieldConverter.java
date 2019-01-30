package protobuf4j.orm.converter;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Internal;

public class EnumFieldConverter implements IFieldValueConverter {
  @Override
  public boolean supportConversion(Descriptors.FieldDescriptor fieldDescriptor, Object fieldValue) {
    return fieldDescriptor.getJavaType() == Descriptors.FieldDescriptor.JavaType.ENUM &&
        (fieldValue instanceof Internal.EnumLite || fieldValue instanceof Integer);
  }

  @Override
  public Class<?> getSqlValueType() {
    return Integer.class;
  }

  @Override
  public Object toSqlValue(Object fieldValue) {
    if (fieldValue instanceof Internal.EnumLite) {
      return ((Internal.EnumLite) fieldValue).getNumber();
    } else if (fieldValue instanceof Integer) {
      return fieldValue;
    }
    throw new FieldConversionException(Descriptors.FieldDescriptor.JavaType.ENUM, fieldValue,
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
    throw new FieldConversionException(Descriptors.FieldDescriptor.JavaType.ENUM, sqlValue);
  }
}
