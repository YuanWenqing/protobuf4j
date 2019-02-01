package protobuf4j.orm.converter;

import com.google.protobuf.Descriptors;

public class DoubleFieldConverter implements IFieldConverter {
  @Override
  public boolean supports(Descriptors.FieldDescriptor fieldDescriptor) {
    return fieldDescriptor.getJavaType() == Descriptors.FieldDescriptor.JavaType.DOUBLE;
  }

  @Override
  public Class<?> getSqlValueType() {
    return Double.class;
  }

  @Override
  public Object toSqlValue(Descriptors.FieldDescriptor fieldDescriptor, Object fieldValue) {
    if (fieldValue instanceof Number) {
      return ((Number) fieldValue).doubleValue();
    }
    throw new FieldConversionException(Descriptors.FieldDescriptor.JavaType.DOUBLE, fieldValue,
        getSqlValueType());
  }

  @Override
  public Object fromSqlValue(Descriptors.FieldDescriptor fieldDescriptor, Object sqlValue) {
    if (sqlValue == null) {
      return 0d;
    } else if (sqlValue instanceof Number) {
      return ((Number) sqlValue).doubleValue();
    }
    throw new FieldConversionException(Descriptors.FieldDescriptor.JavaType.DOUBLE, sqlValue);
  }
}
