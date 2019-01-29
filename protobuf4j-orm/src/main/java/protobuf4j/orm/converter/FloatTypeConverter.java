package protobuf4j.orm.converter;

import com.google.protobuf.Descriptors;

public class FloatTypeConverter implements ITypeConverter {
  @Override
  public Descriptors.FieldDescriptor.JavaType getJavaType() {
    return Descriptors.FieldDescriptor.JavaType.FLOAT;
  }

  @Override
  public Class<?> getSqlValueType() {
    return Float.class;
  }

  @Override
  public Object toSqlValue(Object fieldValue) {
    if (fieldValue instanceof Number) {
      return ((Number) fieldValue).floatValue();
    }
    throw new TypeConversionException(getJavaType(), fieldValue, getSqlValueType());
  }

  @Override
  public Object fromSqlValue(Object sqlValue) {
    if (sqlValue instanceof Float) {
      return sqlValue;
    }
    throw new TypeConversionException(getJavaType(), sqlValue);
  }
}
