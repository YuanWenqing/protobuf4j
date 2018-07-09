package org.protoframework.core.spring;

import com.google.protobuf.ProtocolMessageEnum;
import org.apache.commons.lang3.StringUtils;
import org.protoframework.core.ProtoEnumHelper;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalConverter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

/**
 * @author: yuanwq
 * @date: 2018/5/7
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
        value = helper.byNumber(Integer.parseInt(source));
      }
      if (value == null) {
        value = helper.byName(source);
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
