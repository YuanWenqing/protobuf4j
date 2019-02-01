package protobuf4j.orm.converter;

import com.google.common.collect.Maps;
import com.google.protobuf.Descriptors;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * 处理基本类型的字段值：
 * <ul>
 * <li>boolean</li>
 * <li>bytes</li>
 * <li>double</li>
 * <li>enum</li>
 * <li>float</li>
 * <li>int</li>
 * <li>long</li>
 * <li>string</li>
 * </ul>
 */
public class BasicTypeFieldResolver implements IFieldResolver {
  private final Map<Descriptors.FieldDescriptor.JavaType, IFieldConverter> typeConverterMap;

  public BasicTypeFieldResolver() {
    typeConverterMap = Maps.newHashMap();
    typeConverterMap.put(Descriptors.FieldDescriptor.JavaType.BOOLEAN, new BooleanFieldConverter());
    typeConverterMap
        .put(Descriptors.FieldDescriptor.JavaType.BYTE_STRING, new ByteStringFieldConverter());
    typeConverterMap.put(Descriptors.FieldDescriptor.JavaType.DOUBLE, new DoubleFieldConverter());
    typeConverterMap.put(Descriptors.FieldDescriptor.JavaType.ENUM, new EnumFieldConverter());
    typeConverterMap.put(Descriptors.FieldDescriptor.JavaType.FLOAT, new FloatFieldConverter());
    typeConverterMap.put(Descriptors.FieldDescriptor.JavaType.INT, new IntFieldConverter());
    typeConverterMap.put(Descriptors.FieldDescriptor.JavaType.LONG, new LongFieldConverter());
    typeConverterMap.put(Descriptors.FieldDescriptor.JavaType.STRING, new StringFieldConverter());
  }

  public IFieldConverter findFieldConverter(Descriptors.FieldDescriptor fieldDescriptor) {
    IFieldConverter fieldConverter = typeConverterMap.get(fieldDescriptor.getJavaType());
    if (fieldConverter == null) {
      throw new FieldConversionException(
          "no converter found, field=" + fieldDescriptor + ", javaType=" +
              fieldDescriptor.getJavaType());
    }
    return fieldConverter;
  }

  @Override
  public boolean supports(Descriptors.FieldDescriptor fieldDescriptor) {
    return typeConverterMap.containsKey(fieldDescriptor.getJavaType());
  }

  @Override
  public Class<?> getSqlValueType() {
    return Object.class;
  }

  @Override
  public Object toSqlValue(Descriptors.FieldDescriptor fieldDescriptor, Object value) {
    return findFieldConverter(fieldDescriptor).toSqlValue(fieldDescriptor, value);
  }

  @Override
  public Object fromSqlValue(Descriptors.FieldDescriptor fieldDescriptor, Object sqlValue) {
    return findFieldConverter(fieldDescriptor).fromSqlValue(fieldDescriptor, sqlValue);
  }

  @Override
  public Class<?> resolveSqlValueType(Descriptors.FieldDescriptor fieldDescriptor) {
    return findFieldConverter(fieldDescriptor).getSqlValueType();
  }

  public static Function<String, Object> lookupTransform(
      Descriptors.FieldDescriptor fieldDescriptor) {
    switch (fieldDescriptor.getJavaType()) {
      case BOOLEAN:
        return text -> Integer.parseInt(Objects.requireNonNull(text));
      case ENUM:
        return text -> fieldDescriptor.getEnumType()
            .findValueByNumber(Integer.parseInt(Objects.requireNonNull(text)));
      case INT:
        return Integer::parseInt;
      case LONG:
        return Long::parseLong;
      case FLOAT:
        return Float::parseFloat;
      case DOUBLE:
        return Double::parseDouble;
      case STRING:
        return text -> text;
      case BYTE_STRING:
        return text -> text;
      default:
        throw new FieldConversionException(
            "not support java type: " + fieldDescriptor.getJavaType() + ", field=" +
                fieldDescriptor.getFullName());
    }
  }
}
