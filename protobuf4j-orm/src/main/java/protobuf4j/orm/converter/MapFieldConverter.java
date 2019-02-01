package protobuf4j.orm.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.protobuf.Descriptors;
import com.google.protobuf.MapEntry;
import org.apache.commons.lang3.StringUtils;
import protobuf4j.core.ProtoMessageHelper;
import protobuf4j.core.ProtobufObjectMapper;

import java.io.IOException;
import java.util.*;

@SuppressWarnings({"unchecked", "rawtypes"})
public class MapFieldConverter implements IFieldConverter {
  private static final ObjectMapper OBJECT_MAPPER = ProtobufObjectMapper.DEFAULT;

  private final ProtoMessageHelper<?> messageHelper;
  private final BasicTypeFieldResolver basicTypeFieldResolver;

  public MapFieldConverter(ProtoMessageHelper<?> protoMessageHelper,
      BasicTypeFieldResolver basicTypeFieldResolver) {
    this.messageHelper = protoMessageHelper;
    this.basicTypeFieldResolver = basicTypeFieldResolver;
  }

  @Override
  public boolean supports(Descriptors.FieldDescriptor fieldDescriptor) {
    if (!fieldDescriptor.isMapField()) {
      return false;
    }
    Descriptors.FieldDescriptor keyFd = fieldDescriptor.getMessageType().findFieldByName("key");
    Descriptors.FieldDescriptor valFd = fieldDescriptor.getMessageType().findFieldByName("value");
    return basicTypeFieldResolver.supports(keyFd) && basicTypeFieldResolver.supports(valFd);
  }

  @Override
  public Class<?> getSqlValueType() {
    return String.class;
  }

  @Override
  public Object toSqlValue(Descriptors.FieldDescriptor fieldDescriptor, Object fieldValue) {
    Descriptors.FieldDescriptor keyFd = fieldDescriptor.getMessageType().findFieldByName("key");
    Descriptors.FieldDescriptor valFd = fieldDescriptor.getMessageType().findFieldByName("value");
    // fail fast
    BasicTypeFieldResolver.lookupTransform(keyFd);
    BasicTypeFieldResolver.lookupTransform(valFd);
    if (valFd.getJavaType() == Descriptors.FieldDescriptor.JavaType.MESSAGE) {
      throw new FieldConversionException(
          "not support map field with message value type, field=" + fieldDescriptor +
              ", javaType=" + fieldDescriptor.getJavaType());
    }
    Map<Object, Object> map;
    if (fieldValue instanceof Collection) {
      map = collectionToMap((Collection<? extends MapEntry>) fieldValue, keyFd, valFd);
    } else if (fieldValue instanceof Map) {
      map = mapToMap((Map) fieldValue, keyFd, valFd);
    } else {
      throw new FieldConversionException(
          "fail to convert map field, field=" + fieldDescriptor + ", keyType=" +
              keyFd.getJavaType() + ", valueType=" + valFd.getJavaType() + ", fieldValue=" +
              FieldConversionException.toString(fieldValue));
    }
    try {
      return OBJECT_MAPPER.writeValueAsString(map);
    } catch (IOException e) {
      throw new FieldConversionException(
          "fail to convert map field, field=" + fieldDescriptor + ", keyType=" +
              keyFd.getJavaType() + ", valueType=" + valFd.getJavaType() + ", fieldValue=" +
              FieldConversionException.toString(fieldValue), e);
    }
  }

  private Map<Object, Object> mapToMap(Map<?, ?> map, Descriptors.FieldDescriptor keyFd,
      Descriptors.FieldDescriptor valFd) {
    Map<Object, Object> newMap = Maps.newLinkedHashMapWithExpectedSize(map.size());
    for (Map.Entry entry : map.entrySet()) {
      Object key = basicTypeFieldResolver.toSqlValue(keyFd, entry.getKey());
      Object value = basicTypeFieldResolver.toSqlValue(valFd, entry.getValue());
      newMap.put(key, value);
    }
    return newMap;
  }

  private Map<Object, Object> collectionToMap(Collection<? extends MapEntry> valueCollection,
      Descriptors.FieldDescriptor keyFd, Descriptors.FieldDescriptor valFd) {
    Map<Object, Object> map = Maps.newLinkedHashMapWithExpectedSize(valueCollection.size());
    for (MapEntry entry : valueCollection) {
      Object key = basicTypeFieldResolver.toSqlValue(keyFd, entry.getKey());
      Object value = basicTypeFieldResolver.toSqlValue(valFd, entry.getValue());
      map.put(key, value);
    }
    return map;
  }

  @Override
  public Object fromSqlValue(Descriptors.FieldDescriptor fieldDescriptor, Object sqlValue) {
    Descriptors.FieldDescriptor keyFd = fieldDescriptor.getMessageType().findFieldByName("key");
    Descriptors.FieldDescriptor valFd = fieldDescriptor.getMessageType().findFieldByName("value");
    // fail fast
    BasicTypeFieldResolver.lookupTransform(keyFd);
    BasicTypeFieldResolver.lookupTransform(valFd);
    if (valFd.getJavaType() == Descriptors.FieldDescriptor.JavaType.MESSAGE) {
      throw new FieldConversionException(
          "not support map field with message value type, field=" + fieldDescriptor +
              ", javaType=" + fieldDescriptor.getJavaType());
    }
    if (sqlValue == null) {
      return Collections.emptyList();
    }
    if (sqlValue instanceof String) {
      if (StringUtils.isBlank((String) sqlValue)) {
        return Collections.emptyList();
      }
      try {
        LinkedHashMap<String, Object> map =
            OBJECT_MAPPER.readValue((String) sqlValue, LinkedHashMap.class);
        List<MapEntry<?, ?>> mapEntries = Lists.newArrayList();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
          Object k = BasicTypeFieldResolver.lookupTransform(keyFd).apply(entry.getKey());
          Object v =
              BasicTypeFieldResolver.lookupTransform(valFd).apply(String.valueOf(entry.getValue()));
          k = basicTypeFieldResolver.fromSqlValue(keyFd, k);
          v = basicTypeFieldResolver.fromSqlValue(valFd, v);
          MapEntry.Builder<?, ?> entryBuilder =
              (MapEntry.Builder<?, ?>) messageHelper.newBuilderForField(fieldDescriptor);
          entryBuilder.setField(keyFd, k).setField(valFd, v);
          mapEntries.add(entryBuilder.build());
        }
        return mapEntries;
      } catch (IOException e) {
        throw new FieldConversionException(
            "fail to parse map field, field=" + fieldDescriptor + ", keyType=" +
                keyFd.getJavaType() + ", valueType=" + valFd.getJavaType() + ", sqlValue=" +
                FieldConversionException.toString(sqlValue), e);
      }
    }
    throw new FieldConversionException(
        "fail to parse map field, field=" + fieldDescriptor + ", keyType=" + keyFd.getJavaType() +
            ", valueType=" + valFd.getJavaType() + ", sqlValue=" +
            FieldConversionException.toString(sqlValue));
  }

}
