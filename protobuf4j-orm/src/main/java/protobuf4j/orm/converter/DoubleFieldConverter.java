package protobuf4j.orm.converter;

import com.google.protobuf.Descriptors;

public class DoubleFieldConverter implements IFieldValueConverter {
  @Override
  public boolean supportConversion(Descriptors.FieldDescriptor fieldDescriptor, Object fieldValue) {
    return fieldDescriptor.getJavaType() == Descriptors.FieldDescriptor.JavaType.DOUBLE &&
        fieldValue instanceof Number;
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
    throw new FieldConversionException(Descriptors.FieldDescriptor.JavaType.DOUBLE, fieldValue,
        getSqlValueType());
  }

  @Override
  public Object fromSqlValue(Object sqlValue) {
    if (sqlValue == null) {
      return 0d;
    }
    if (sqlValue instanceof Number) {
      return ((Number) sqlValue).doubleValue();
    }
    throw new FieldConversionException(Descriptors.FieldDescriptor.JavaType.DOUBLE, sqlValue);
  }
}
