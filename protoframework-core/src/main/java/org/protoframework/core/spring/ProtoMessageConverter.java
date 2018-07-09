/**
 * @author yuanwq, date: 2017年2月17日
 */
package org.protoframework.core.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.GeneratedMessageV3;
import org.apache.commons.lang3.StringUtils;
import org.protoframework.core.ProtoMessageHelper;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

import java.io.IOException;

/**
 * @author yuanwq
 */
public class ProtoMessageConverter implements ConverterFactory<String, GeneratedMessageV3> {
  private ObjectMapper objectMapper;

  public void setObjectMapper(ObjectMapper ObjectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public <T extends GeneratedMessageV3> Converter<String, T> getConverter(Class<T> targetType) {
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

}
