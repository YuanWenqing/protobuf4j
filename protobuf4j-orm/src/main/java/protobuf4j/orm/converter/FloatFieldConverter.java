package protobuf4j.orm.converter;

import com.google.protobuf.Descriptors;

public class FloatFieldConverter implements IFieldConverter {
  @Override
  public boolean supportConversion(Descriptors.FieldDescriptor fieldDescriptor, Object fieldValue) {
    return fieldDescriptor.getJavaType() == Descriptors.FieldDescriptor.JavaType.FLOAT &&
        fieldValue instanceof Number;
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
    throw new FieldConversionException(Descriptors.FieldDescriptor.JavaType.FLOAT, fieldValue,
        getSqlValueType());
  }

  @Override
  public Object fromSqlValue(Object sqlValue) {
    if (sqlValue == null) {
      return 0f;
    }
    if (sqlValue instanceof Number) {
      return ((Number) sqlValue).floatValue();
    }
    throw new FieldConversionException(Descriptors.FieldDescriptor.JavaType.FLOAT, sqlValue);
  }
}
