package protobuf4j.orm.converter;

import com.google.protobuf.Descriptors;

public class DoubleTypeConverter implements ITypeConverter {
  @Override
  public Descriptors.FieldDescriptor.JavaType getJavaType() {
    return Descriptors.FieldDescriptor.JavaType.DOUBLE;
  }

  @Override
  public Class<?> getSqlValueType() {
    return Double.class;
  }

  @Override
  public Object toSqlValue(Object fieldValue) {
    if (fieldValue instanceof Number) {
      return ((Number) fieldValue).doubleValue();
    }
    throw new TypeConversionException(getJavaType(), fieldValue, getSqlValueType());
  }

  @Override
  public Object fromSqlValue(Object sqlValue) {
    if (sqlValue instanceof Number) {
      return ((Number) sqlValue).doubleValue();
    }
    throw new TypeConversionException(getJavaType(), sqlValue);
  }
}
