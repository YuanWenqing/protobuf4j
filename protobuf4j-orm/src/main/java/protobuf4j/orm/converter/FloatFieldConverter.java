package protobuf4j.orm.converter;

import com.google.protobuf.Descriptors;

public class FloatFieldConverter implements IFieldValueConverter {
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
    } else if (sqlValue instanceof Number) {
      return ((Number) sqlValue).floatValue();
    }
    throw new FieldConversionException(Descriptors.FieldDescriptor.JavaType.FLOAT, sqlValue);
  }
}
