package org.protoframework.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.cfg.MutableConfigOverride;
import com.hubspot.jackson.datatype.protobuf.ProtobufModule;

/**
 * author: yuanwq
 * date: 2018/7/6
 */
public abstract class ProtobufObjectMapper extends ObjectMapper {
  public ProtobufObjectMapper() {
    config();
  }

  protected abstract void config();

  public static final ProtobufObjectMapper DEFAULT = new ProtobufObjectMapper() {
    @Override
    protected void config() {
      this.registerModule(new ProtobufModule());
      this.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
      this.setSerializationInclusion(JsonInclude.Include.ALWAYS);
      this.enable(SerializationFeature.WRITE_ENUMS_USING_INDEX);
      this.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    @Override
    public MutableConfigOverride configOverride(Class<?> type) {
      return super.configOverride(type);
    }
  };

}
