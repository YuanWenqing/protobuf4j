package protobuf4j.spring;

import com.google.protobuf.ProtocolMessageEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalConverter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import protobuf4j.core.ProtoEnumHelper;

/**
 * author: yuanwq
 * date: 2018/5/7
 */
public class ProtoEnumConverter
    implements ConverterFactory<String, ProtocolMessageEnum>, ConditionalConverter {
  @Override
  public <T extends ProtocolMessageEnum> Converter<String, T> getConverter(Class<T> targetType) {
    return source -> {
      if (StringUtils.isBlank(source)) {
        return null;
      }
      ProtoEnumHelper<T> helper = ProtoEnumHelper.getHelper(targetType);
      source = StringUtils.strip(source);
      T value = null;
      if (StringUtils.isNumeric(source)) {
        // not support negative number
        value = helper.forNumber(Integer.parseInt(source));
      }
      if (value == null) {
        value = helper.of(source);
      }
      if (value == null) {
        throw new ConversionFailedException(TypeDescriptor.forObject(source),
            TypeDescriptor.valueOf(targetType), source, null);
      }
      return value;
    };
  }

  @Override
  public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
    return ProtocolMessageEnum.class.isAssignableFrom(targetType.getType());
  }
}
