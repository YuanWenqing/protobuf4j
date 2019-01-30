package protobuf4j.orm.converter;

import com.google.protobuf.Descriptors;

public class IntFieldConverter implements IFieldConverter {
  @Override
  public boolean supportConversion(Descriptors.FieldDescriptor fieldDescriptor, Object fieldValue) {
    return fieldDescriptor.getJavaType() == Descriptors.FieldDescriptor.JavaType.INT &&
        fieldValue instanceof Integer;
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
    throw new FieldConversionException(Descriptors.FieldDescriptor.JavaType.INT, fieldValue,
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
    throw new FieldConversionException(Descriptors.FieldDescriptor.JavaType.INT, sqlValue);
  }
}
