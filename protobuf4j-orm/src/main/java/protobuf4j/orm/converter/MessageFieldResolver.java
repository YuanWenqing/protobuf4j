/**
 * author yuanwq, date: 2017年4月27日
 */
package protobuf4j.orm.converter;

import com.google.common.collect.Maps;
import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;
import protobuf4j.core.ProtoMessageHelper;

import java.util.Map;

import static com.google.common.base.Preconditions.*;

/**
 */
public class MessageFieldResolver<M extends Message> implements IFieldResolver {
  private static final TimestampFieldConverter timestampFieldConverter =
      new TimestampFieldConverter();

  private final ProtoMessageHelper<M> messageHelper;
  private final BasicTypeFieldResolver basicTypeFieldResolver;

  private final Map<String, IFieldValueConverter> fieldValueConverterMap;

  public MessageFieldResolver(Class<M> messageClass) {
    checkNotNull(messageClass);
    this.messageHelper = ProtoMessageHelper.getHelper(messageClass);
    this.basicTypeFieldResolver = new BasicTypeFieldResolver();
    this.fieldValueConverterMap = Maps.newConcurrentMap();
  }

  @Override
  public Object toSqlValue(Descriptors.FieldDescriptor fd, Object value) {
    IFieldValueConverter fieldConverter = findFieldConverter(fd);
    return fieldConverter.toSqlValue(value);
  }

  private IFieldValueConverter findFieldConverter(Descriptors.FieldDescriptor fieldDescriptor) {
    if (fieldDescriptor.isMapField()) {
      // check map first, because map field is also repeated
      return this.fieldValueConverterMap.computeIfAbsent(fieldDescriptor.getName(),
          fd -> new MapFieldConverter(messageHelper, fieldDescriptor, basicTypeFieldResolver));
    } else if (fieldDescriptor.isRepeated()) {
      return this.fieldValueConverterMap.computeIfAbsent(fieldDescriptor.getName(),
          fd -> new RepeatedFieldConverter(fieldDescriptor, basicTypeFieldResolver));
    } else if (isTimestampField(fieldDescriptor)) {
      return timestampFieldConverter;
    }
    return basicTypeFieldResolver.findFieldConverter(fieldDescriptor);
  }

  public boolean isTimestampField(Descriptors.FieldDescriptor fd) {
    // TODO: check timestamp class
    return !fd.isRepeated() && Descriptors.FieldDescriptor.JavaType.LONG.equals(fd.getJavaType()) &&
        fd.getName().endsWith("_time");
  }

  @Override
  public Object fromSqlValue(Descriptors.FieldDescriptor fd, Object sqlValue) {
    IFieldValueConverter converter = findFieldConverter(fd);
    if (converter == null) {
      throw new FieldConversionException(
          "no converter found, javaType=" + fd.getJavaType() + ", sqlValue=`" + sqlValue +
              "`, sqlValue.type=" + sqlValue.getClass().getName());
    }
    Object value = converter.fromSqlValue(sqlValue);
    if (fd.getJavaType() == Descriptors.FieldDescriptor.JavaType.ENUM) {
      // EnumValueDescriptor
      return fd.getEnumType().findValueByNumber((Integer) value);
    } else {
      return value;
    }
  }

  @Override
  public Class<?> resolveSqlValueType(Descriptors.FieldDescriptor fd) {
    // map/list 使用string拼接
    if (fd.isMapField() || fd.isRepeated()) return String.class;
    IFieldValueConverter fieldConverter = findFieldConverter(fd);
    return fieldConverter.getSqlValueType();
  }

}
