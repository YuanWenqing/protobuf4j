/**
 * author yuanwq, date: 2017年4月27日
 */
package protobuf4j.orm.converter;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;
import com.google.protobuf.Timestamp;
import protobuf4j.core.ProtoMessageHelper;

import static com.google.common.base.Preconditions.*;

/**
 * resolve value type and handle value conversion for fields of specified Message class
 */
public class FieldResolver<M extends Message> implements IFieldResolver {
  private static final TimestampFieldConverter timestampFieldConverter =
      new TimestampFieldConverter();

  private final ProtoMessageHelper<M> messageHelper;
  private final BasicTypeFieldResolver basicTypeFieldResolver;
  private final MapFieldConverter mapFieldConverter;
  private final RepeatedFieldConverter repeatedFieldConverter;
  private final MessageFiledConverter messageFiledConverter;

  public FieldResolver(Class<M> messageClass) {
    checkNotNull(messageClass);
    this.messageHelper = ProtoMessageHelper.getHelper(messageClass);
    this.basicTypeFieldResolver = new BasicTypeFieldResolver();
    this.mapFieldConverter = new MapFieldConverter(messageHelper, basicTypeFieldResolver);
    this.repeatedFieldConverter = new RepeatedFieldConverter(basicTypeFieldResolver);
    this.messageFiledConverter = new MessageFiledConverter(messageHelper);
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
    } else if (fieldDescriptor.getJavaType() == Descriptors.FieldDescriptor.JavaType.MESSAGE) {
      return messageFiledConverter;
    }
    return basicTypeFieldResolver.findFieldConverter(fieldDescriptor);
  }

  private boolean isTimestampField(Descriptors.FieldDescriptor fieldDescriptor) {
    return Descriptors.FieldDescriptor.JavaType.MESSAGE == fieldDescriptor.getJavaType() &&
        messageHelper.getFieldType(fieldDescriptor.getName()).equals(Timestamp.class);
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
    if (fieldDescriptor.isMapField() || fieldDescriptor.isRepeated()) {
      return String.class;
    } else if (isTimestampField(fieldDescriptor)) {
      return java.sql.Timestamp.class;
    }
    IFieldConverter fieldConverter = findFieldConverter(fieldDescriptor);
    return fieldConverter.getSqlValueType();
  }

}
