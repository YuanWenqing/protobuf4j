package protobuf4j.orm.converter;

import com.google.protobuf.Descriptors;

public class IntFieldConverter implements IFieldConverter {
  @Override
  public boolean supports(Descriptors.FieldDescriptor fieldDescriptor) {
    return fieldDescriptor.getJavaType() == Descriptors.FieldDescriptor.JavaType.INT;
  }

  @Override
  public Class<?> getSqlValueType() {
    return Integer.class;
  }

  @Override
  public Object toSqlValue(Descriptors.FieldDescriptor fieldDescriptor, Object fieldValue) {
    if (fieldValue instanceof Integer) {
      return fieldValue;
    }
    throw new FieldConversionException(Descriptors.FieldDescriptor.JavaType.INT, fieldValue,
        getSqlValueType());
  }

  @Override
  public Object fromSqlValue(Descriptors.FieldDescriptor fieldDescriptor, Object sqlValue) {
    if (sqlValue == null) {
      return 0;
    } else if (sqlValue instanceof Integer) {
      return sqlValue;
    }
    throw new FieldConversionException(Descriptors.FieldDescriptor.JavaType.INT, sqlValue);
  }
}
