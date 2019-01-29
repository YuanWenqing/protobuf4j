package protobuf4j.orm.converter;

import com.google.protobuf.Descriptors;

import java.util.Objects;

public class BooleanTypeConverter implements ITypeConverter {
  @Override
  public Descriptors.FieldDescriptor.JavaType getJavaType() {
    return Descriptors.FieldDescriptor.JavaType.BOOLEAN;
  }

  @Override
  public Class<?> getSqlValueType() {
    return Integer.class;
  }

  @Override
  public Object toSqlValue(Object fieldValue) {
    if (fieldValue instanceof Boolean) {
      return (Boolean) fieldValue ? 1 : 0;
    }
    if (fieldValue instanceof Integer) {
      int i = (Integer) fieldValue;
      return i == 0 ? i : 1;
    }
    throw new TypeConversionException(getJavaType(), fieldValue, getSqlValueType());
  }

  @Override
  public Object fromSqlValue(Object sqlValue) {
    if (sqlValue instanceof Integer) {
      return !Objects.equals(sqlValue, 0);
    }
    throw new TypeConversionException(getJavaType(), sqlValue);
  }
}
