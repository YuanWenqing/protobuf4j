package protobuf4j.orm.converter;

import com.google.protobuf.Descriptors;

public class IntTypeConverter implements ITypeConverter {
  @Override
  public Descriptors.FieldDescriptor.JavaType getJavaType() {
    return Descriptors.FieldDescriptor.JavaType.INT;
  }

  @Override
  public Class<?> getSqlValueType() {
    return Integer.class;
  }

  @Override
  public Object toSqlValue(Object fieldValue) {
    if (fieldValue instanceof Integer) {
      return fieldValue;
    }
    throw new TypeConversionException(getJavaType(), fieldValue, getSqlValueType());
  }

  @Override
  public Object fromSqlValue(Object sqlValue) {
    if (sqlValue instanceof Integer) {
      return sqlValue;
    }
    throw new TypeConversionException(getJavaType(), sqlValue);
  }
}
