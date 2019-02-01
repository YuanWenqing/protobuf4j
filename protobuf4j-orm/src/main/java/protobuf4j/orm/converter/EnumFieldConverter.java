package protobuf4j.orm.converter;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Internal;

public class EnumFieldConverter implements IFieldConverter {
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
    } else if (sqlValue instanceof Integer) {
      return sqlValue;
    } else if (sqlValue instanceof Internal.EnumLite) {
      return sqlValue;
    }
    throw new FieldConversionException(Descriptors.FieldDescriptor.JavaType.ENUM, sqlValue);
  }
}
