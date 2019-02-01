/**
 * author yuanwq, date: 2017年4月27日
 */
package protobuf4j.orm.converter;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;
import protobuf4j.core.ProtoMessageHelper;

import static com.google.common.base.Preconditions.*;

/**
 */
public class MessageFieldResolver<M extends Message> implements IFieldResolver {
  private static final TimestampFieldConverter timestampFieldConverter =
      new TimestampFieldConverter();

  private final ProtoMessageHelper<M> messageHelper;
  private final BasicTypeFieldResolver basicTypeFieldResolver;
  private final MapFieldConverter mapFieldConverter;
  private final RepeatedFieldConverter repeatedFieldConverter;

  public MessageFieldResolver(Class<M> messageClass) {
    checkNotNull(messageClass);
    this.messageHelper = ProtoMessageHelper.getHelper(messageClass);
    this.basicTypeFieldResolver = new BasicTypeFieldResolver();
    this.mapFieldConverter = new MapFieldConverter(messageHelper, basicTypeFieldResolver);
    this.repeatedFieldConverter = new RepeatedFieldConverter(basicTypeFieldResolver);
  }

  @Override
  public boolean supports(Descriptors.FieldDescriptor fieldDescriptor) {
    return true;
  }

  @Override
  public Class<?> getSqlValueType() {
    return Object.class;
  }

  @Override
  public Object toSqlValue(Descriptors.FieldDescriptor fieldDescriptor, Object value) {
    IFieldConverter fieldConverter = findFieldConverter(fieldDescriptor);
    return fieldConverter.toSqlValue(fieldDescriptor, value);
  }

  private IFieldConverter findFieldConverter(Descriptors.FieldDescriptor fieldDescriptor) {
    if (fieldDescriptor.isMapField()) {
      // check map first, because map field is also repeated
      return mapFieldConverter;
    } else if (fieldDescriptor.isRepeated()) {
      return repeatedFieldConverter;
    } else if (isTimestampField(fieldDescriptor)) {
      return timestampFieldConverter;
    }
    return basicTypeFieldResolver.findFieldConverter(fieldDescriptor);
  }

  public boolean isTimestampField(Descriptors.FieldDescriptor fieldDescriptor) {
    // TODO: check timestamp class
    return !fieldDescriptor.isRepeated() &&
        Descriptors.FieldDescriptor.JavaType.LONG.equals(fieldDescriptor.getJavaType()) &&
        fieldDescriptor.getName().endsWith("_time");
  }

  @Override
  public Object fromSqlValue(Descriptors.FieldDescriptor fieldDescriptor, Object sqlValue) {
    IFieldConverter converter = findFieldConverter(fieldDescriptor);
    if (converter == null) {
      throw new FieldConversionException(
          "no converter found, javaType=" + fieldDescriptor.getJavaType() + ", sqlValue=`" +
              sqlValue + "`, sqlValue.type=" + sqlValue.getClass().getName());
    }
    Object value = converter.fromSqlValue(fieldDescriptor, sqlValue);
//    if (fieldDescriptor.getJavaType() == Descriptors.FieldDescriptor.JavaType.ENUM) {
//      // EnumValueDescriptor
//      return fieldDescriptor.getEnumType().findValueByNumber((Integer) value);
//    }
    return value;
  }

  @Override
  public Class<?> resolveSqlValueType(Descriptors.FieldDescriptor fieldDescriptor) {
    // map/list 使用string拼接
    if (fieldDescriptor.isMapField() || fieldDescriptor.isRepeated()) return String.class;
    IFieldConverter fieldConverter = findFieldConverter(fieldDescriptor);
    return fieldConverter.getSqlValueType();
  }

}
