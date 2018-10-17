package protobufframework.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.MutableConfigOverride;
import com.fasterxml.jackson.databind.deser.Deserializers;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleSerializers;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import com.google.protobuf.ProtocolMessageEnum;
import com.hubspot.jackson.datatype.protobuf.ProtobufModule;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author: yuanwq
 * @date: 2018/7/6
 */
public abstract class ProtobufObjectMapper extends ObjectMapper {
  public ProtobufObjectMapper() {
    config();
  }

  protected abstract void config();

  public static final ProtobufObjectMapper DEFAULT = new ProtobufObjectMapper() {
    @Override
    protected void config() {
      this.registerModule(new ProtobufModule2());
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

  private static class ProtobufModule2 extends ProtobufModule {
    @Override
    public void setupModule(SetupContext context) {
      super.setupModule(context);

      // support for enum (de)serializing by number
      SimpleSerializers serializers = new SimpleSerializers();
      serializers.addSerializer(new ProtoEnumSerializer());
      context.addSerializers(serializers);

      context.addDeserializers(new ProtoEnumDeserializerFactory());
    }
  }

  private static class ProtoEnumSerializer extends StdScalarSerializer<ProtocolMessageEnum> {

    protected ProtoEnumSerializer() {
      super(ProtocolMessageEnum.class);
    }

    @Override
    public void serialize(ProtocolMessageEnum value, JsonGenerator gen, SerializerProvider provider)
        throws IOException {
      if (provider.getConfig().isEnabled(SerializationFeature.WRITE_ENUMS_USING_INDEX)) {
        gen.writeNumber(value.getNumber());
      } else {
        gen.writeNumber(((Enum) value).name());
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static class ProtoEnumDeserializerFactory extends Deserializers.Base {
    private final ConcurrentMap<Class<? extends ProtocolMessageEnum>, ProtoEnumDeserializer<?>>
        deserializerCache = new ConcurrentHashMap<>();

    @Override
    public JsonDeserializer<?> findEnumDeserializer(Class<?> type, DeserializationConfig config,
        BeanDescription beanDesc) throws JsonMappingException {
      if (ProtocolMessageEnum.class.isAssignableFrom(type)) {
        return getDeserializer((Class<? extends ProtocolMessageEnum>) type);
      }
      return super.findEnumDeserializer(type, config, beanDesc);
    }

    private <T extends ProtocolMessageEnum> ProtoEnumDeserializer<T> getDeserializer(
        Class<? extends ProtocolMessageEnum> enumType) {
      ProtoEnumDeserializer<?> deserializer = deserializerCache.get(enumType);
      if (deserializer == null) {
        ProtoEnumDeserializer<T> newDeserializer = new ProtoEnumDeserializer<T>(enumType);
        ProtoEnumDeserializer<?> previousDeserializer =
            deserializerCache.putIfAbsent(enumType, newDeserializer);
        deserializer = previousDeserializer == null ? newDeserializer : previousDeserializer;
      }
      return (ProtoEnumDeserializer<T>) deserializer;
    }
  }

  @SuppressWarnings("unchecked")
  private static class ProtoEnumDeserializer<T extends ProtocolMessageEnum>
      extends StdDeserializer<T> {
    private final Class<T> cls;

    protected ProtoEnumDeserializer(Class<?> vc) {
      super(vc);
      this.cls = (Class<T>) vc;
    }

    @Override
    public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
      String text = p.getText();
      if (StringUtils.isNumeric(text)) {
        return ProtoEnumHelper.getHelper(this.cls).forNumber(Integer.parseInt(text));
      }
      return ProtoEnumHelper.getHelper(this.cls).of(text);
    }
  }

}
