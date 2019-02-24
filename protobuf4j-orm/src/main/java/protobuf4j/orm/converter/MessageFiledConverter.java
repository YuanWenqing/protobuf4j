package protobuf4j.orm.converter;

import com.google.protobuf.Descriptors;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.MessageOrBuilder;
import com.google.protobuf.util.JsonFormat;
import protobuf4j.core.ProtoMessageHelper;

/**
 * 处理Message类型的field
 * <p>
 * Author: yuanwq
 * Date: 2019/2/24
 */
@SuppressWarnings("unchecked")
public class MessageFiledConverter implements IFieldConverter {
  private static final JsonFormat.Printer printer =
      JsonFormat.printer().preservingProtoFieldNames().omittingInsignificantWhitespace();
  private static final JsonFormat.Parser parser = JsonFormat.parser().ignoringUnknownFields();

  private final ProtoMessageHelper<?> messageHelper;

  public MessageFiledConverter(ProtoMessageHelper<?> messageHelper) {
    this.messageHelper = messageHelper;
  }

  @Override
  public boolean supports(Descriptors.FieldDescriptor fieldDescriptor) {
    return fieldDescriptor.getJavaType() == Descriptors.FieldDescriptor.JavaType.MESSAGE;
  }

  @Override
  public Class<?> getSqlValueType() {
    return String.class;
  }

  @Override
  public Object toSqlValue(Descriptors.FieldDescriptor fieldDescriptor, Object fieldValue) {
    if (fieldValue instanceof Message) {
      try {
        return printer.print((MessageOrBuilder) fieldValue);
      } catch (InvalidProtocolBufferException e) {
        throw new FieldConversionException(
            "fail to convert message field, field=" + fieldDescriptor + ", fieldValue=" +
                ProtoMessageHelper.printToString((Message) fieldValue), e);
      }
    }
    throw new FieldConversionException(Descriptors.FieldDescriptor.JavaType.MESSAGE, fieldValue,
        getSqlValueType());
  }

  @Override
  public Object fromSqlValue(Descriptors.FieldDescriptor fieldDescriptor, Object sqlValue) {
    if (sqlValue == null) {
      // cannot use helper.getFieldDefaultValue, may be repeated/map field
      return messageHelper.newBuilderForField(fieldDescriptor).getDefaultInstanceForType();
    }
    Class<? extends Message> fieldType =
        (Class<? extends Message>) messageHelper.getFieldType(fieldDescriptor.getName());
    if (fieldType.isInstance(sqlValue)) {
      return sqlValue;
    } else if (sqlValue instanceof String) {
      Message.Builder builder = messageHelper.newBuilderForField(fieldDescriptor);
      try {
        parser.merge((String) sqlValue, builder);
        return builder.build();
      } catch (InvalidProtocolBufferException e) {
        throw new FieldConversionException(
            "fail to parse message field, field=" + fieldDescriptor + ", sqlValue=" +
                FieldConversionException.toString(sqlValue), e);
      }
    }
    throw new FieldConversionException("fail to parse sql value for message field, sqlValue=" +
        FieldConversionException.toString(sqlValue));
  }
}
