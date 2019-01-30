package protobuf4j.orm.dao;

import com.google.common.collect.Maps;
import com.google.protobuf.Descriptors;
import protobuf4j.orm.converter.*;

import java.util.Map;

/**
 * 基本类型的字段值转换器：
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
public class BasicTypeSqlHandler implements IProtoSqlHandler {
  private final Map<Descriptors.FieldDescriptor.JavaType, IFieldConverter> typeConverterMap;

  public BasicTypeSqlHandler() {
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
  public Object toSqlValue(Descriptors.FieldDescriptor fd, Object value) {
    return findFieldConverter(fd).toSqlValue(value);
  }

  @Override
  public Object fromSqlValue(Descriptors.FieldDescriptor fd, Object sqlValue) {
    return findFieldConverter(fd).fromSqlValue(sqlValue);
  }

  @Override
  public Class<?> resolveSqlValueType(Descriptors.FieldDescriptor fd) {
    return findFieldConverter(fd).getSqlValueType();
  }
}
