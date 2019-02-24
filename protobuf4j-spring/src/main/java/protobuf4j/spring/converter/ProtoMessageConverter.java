/**
 * author yuanwq, date: 2017年2月17日
 */
package protobuf4j.spring.converter;

import com.google.protobuf.Message;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalConverter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import protobuf4j.core.ProtoMessageHelper;
import protobuf4j.core.ProtobufObjectMapper;

import java.io.IOException;

/**
 * author yuanwq
 */
public class ProtoMessageConverter
    implements ConverterFactory<String, Message>, ConditionalConverter {
  private final ProtobufObjectMapper objectMapper;

  public ProtoMessageConverter(ProtobufObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public <T extends Message> Converter<String, T> getConverter(Class<T> targetType) {
    return source -> {
      if (StringUtils.isBlank(source)) {
        return ProtoMessageHelper.getHelper(targetType).defaultValue();
      }
      try {
        return objectMapper.readValue(source, targetType);
      } catch (IOException e) {
        throw new ConversionFailedException(TypeDescriptor.forObject(source),
            TypeDescriptor.valueOf(targetType), source, e);
      }
    };
  }

  @Override
  public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
    return Message.class.isAssignableFrom(targetType.getType());
  }
}
