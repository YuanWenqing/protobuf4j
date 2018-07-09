package org.protoframework.core.spring;

import com.google.protobuf.ProtocolMessageEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalConverter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

import java.lang.reflect.Method;

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
      source = StringUtils.strip(source);
      try {
        int num = Integer.parseInt(source);
        Method method = targetType.getDeclaredMethod("forNumber", int.class);
        return (T) method.invoke(targetType, num);
      } catch (Exception e) {
        throw new ConversionFailedException(TypeDescriptor.forObject(source),
            TypeDescriptor.valueOf(targetType), source, e);
      }
    };
  }

  @Override
  public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
    return ProtocolMessageEnum.class.isAssignableFrom(targetType.getType());
  }
}
