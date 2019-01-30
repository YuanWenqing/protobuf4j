package protobuf4j.orm.converter;

import com.google.protobuf.Descriptors;

public class StringFieldConverter implements IFieldValueConverter {
  @Override
  public Class<?> getSqlValueType() {
    return String.class;
  }

  @Override
  public Object toSqlValue(Object fieldValue) {
    if (fieldValue == null) {
      return "";
    }
    if (fieldValue instanceof String) {
      return fieldValue;
    }
    throw new FieldConversionException(Descriptors.FieldDescriptor.JavaType.STRING, fieldValue,
        getSqlValueType());
  }

  @Override
  public Object fromSqlValue(Object sqlValue) {
    if (sqlValue == null) {
      return "";
    }
    if (sqlValue instanceof String) {
      return sqlValue;
    }
    throw new FieldConversionException(Descriptors.FieldDescriptor.JavaType.STRING, sqlValue);
  }
}
