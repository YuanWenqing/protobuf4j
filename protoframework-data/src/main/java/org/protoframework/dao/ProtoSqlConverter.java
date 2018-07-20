/**
 * @author yuanwq, date: 2017年4月27日
 */
package org.protoframework.dao;

import com.google.common.base.CaseFormat;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.protobuf.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.translate.EntityArrays;
import org.apache.commons.lang3.text.translate.LookupTranslator;
import org.protoframework.core.ProtoMessageHelper;
import org.springframework.dao.TypeMismatchDataAccessException;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 */
public class ProtoSqlConverter implements IProtoSqlConverter {
  protected static final String LIST_SEP = ",";
  protected static final String MAP_KV_SEP = ":";
  protected static final String MAP_ENTRY_SEP = ";";
  protected static final String[][] LIST_LOOKUP = {{",", "%2c"}, {"%", "%25"}};
  protected static final LookupTranslator LIST_VALUE_ESCAPE = new LookupTranslator(LIST_LOOKUP);
  protected static final LookupTranslator LIST_VALUE_UNESCAPE =
      new LookupTranslator(EntityArrays.invert(LIST_LOOKUP));
  protected static final String[][] MAP_LOOKUP = {{":", "%3a"}, {";", "%3b"}, {"%", "%25"}};
  protected static final LookupTranslator MAP_VALUE_ESCAPE = new LookupTranslator(MAP_LOOKUP);
  protected static final LookupTranslator MAP_VALUE_UNESCAPE =
      new LookupTranslator(EntityArrays.invert(MAP_LOOKUP));
  // not omit empty string
  protected static Splitter LIST_SPLITTER = Splitter.on(LIST_SEP);
  // omit empty string, 因为entry至少包含一个“:”分隔符，不可能为空
  protected static final Splitter MAP_ENTRY_SPLITTER =
      Splitter.on(MAP_ENTRY_SEP).omitEmptyStrings();
  protected static final Splitter.MapSplitter MAP_SPLITTER =
      MAP_ENTRY_SPLITTER.withKeyValueSeparator(MAP_KV_SEP);

  private static final IProtoSqlConverter instance = new ProtoSqlConverter();

  public static IProtoSqlConverter getInstance() {
    return instance;
  }

  protected ProtoSqlConverter() {
  }

  @Override
  public String tableName(Class<? extends Message> messageClass) {
    return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, messageClass.getSimpleName());
  }

  @Override
  public <B extends Message> Object toSqlValue(Class<B> messageClass, String field, Object value) {
    ProtoMessageHelper<B> helper = ProtoMessageHelper.getHelper(messageClass);
    Descriptors.FieldDescriptor fd = helper.checkFieldDescriptor(field);
    return toSqlValue(fd, value);
  }

  @Override
  public Object toSqlValue(Descriptors.FieldDescriptor fd, Object value) {
    if (fd.isMapField()) {
      // check map first, for map field is also repeated
      return encodeMapToString(fd, value);
    } else if (fd.isRepeated()) {
      return encodeListToString(fd, value);
    } else if (isTimestampField(fd)) {
      return toSqlTimestamp(value);
    } else {
      return toSqlValue(fd.getJavaType(), value);
    }
  }

  @Override
  public boolean isTimestampField(Descriptors.FieldDescriptor fd) {
    return Descriptors.FieldDescriptor.JavaType.LONG.equals(fd.getJavaType()) &&
        fd.getName().endsWith("_time");
  }

  /**
   * 字段值映射：{@code proto type -> sql type}
   */
  protected Object toSqlValue(Descriptors.FieldDescriptor.JavaType javaType, Object value) {
    switch (javaType) {
      case BOOLEAN:
        return boolToInt(value);
      case ENUM:
        return enumToInt(value);
      case INT:
      case LONG:
      case FLOAT:
      case DOUBLE:
      case STRING:
        return value;
      default:
        throw new TypeMismatchDataAccessException(
            "not support java type: " + javaType + ", value=" + value);
    }
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  protected String encodeMapToString(Descriptors.FieldDescriptor fd, Object value) {
    Descriptors.FieldDescriptor keyFd = fd.getMessageType().findFieldByName("key");
    Descriptors.FieldDescriptor valFd = fd.getMessageType().findFieldByName("value");
    if (value instanceof Map) {
      Map<?, ?> map = (Map<?, ?>) value;
      StringBuilder sb = new StringBuilder();
      for (Map.Entry<?, ?> entry : map.entrySet()) {
        Object k = toSqlValue(keyFd, entry.getKey());
        k = MAP_VALUE_ESCAPE.translate(String.valueOf(k));
        Object v = toSqlValue(valFd, entry.getValue());
        v = MAP_VALUE_ESCAPE.translate(String.valueOf(v));
        sb.append(k).append(MAP_KV_SEP).append(v).append(MAP_ENTRY_SEP);
      }
      return sb.toString();
    } else if (value instanceof List) {
      List<? extends MapEntry> list = (List<? extends MapEntry>) value;
      StringBuilder sb = new StringBuilder();
      for (MapEntry entry : list) {
        Object k = toSqlValue(keyFd, entry.getKey());
        k = MAP_VALUE_ESCAPE.translate(String.valueOf(k));
        Object v = toSqlValue(valFd, entry.getValue());
        v = MAP_VALUE_ESCAPE.translate(String.valueOf(v));
        sb.append(k).append(MAP_KV_SEP).append(v).append(MAP_ENTRY_SEP);
      }
      return sb.toString();
    }
    throw new TypeMismatchDataAccessException(
        "fail to handle toSqlValue, field=" + fd.getFullName() + ", valueType: " +
            value.getClass().getName());
  }

  protected String encodeListToString(Descriptors.FieldDescriptor fd, Object value) {
    if (value instanceof Iterable) {
      StringBuilder sb = new StringBuilder();
      Iterable<?> list = (Iterable<?>) value;
      for (Object item : list) {
        // 只要有一个元素就有一个分隔符，从而保证元素为string类型时，可以添加空字符串值
        Object v = toSqlValue(fd.getJavaType(), item);
        v = LIST_VALUE_ESCAPE.translate(String.valueOf(v));
        sb.append(v).append(LIST_SEP);
      }
      return sb.toString();
    } else {
      return String.valueOf(value);
    }
  }

  protected int boolToInt(Object v) {
    if (v.getClass().equals(boolean.class)) {
      return (boolean) v ? 1 : 0;
    }
    if (v.getClass().equals(Boolean.class)) {
      return (Boolean) v ? 1 : 0;
    }
    if (v.getClass().equals(int.class)) {
      return ((int) v) != 0 ? 1 : 0;
    }
    if (v.getClass().equals(Integer.class)) {
      return ((Integer) v) != 0 ? 1 : 0;
    }
    throw new TypeMismatchDataAccessException(
        "Can't convert bool `" + v + "` of class " + v.getClass().getName());
  }

  protected int enumToInt(Object v) {
    if (v instanceof ProtocolMessageEnum) {
      return ((ProtocolMessageEnum) v).getNumber();
    }
    if (v instanceof Internal.EnumLite) {
      return ((Internal.EnumLite) v).getNumber();
    }
    if (v.getClass().equals(int.class)) {
      return (int) v;
    }
    if (v instanceof Number) {
      return ((Number) v).intValue();
    }
    throw new TypeMismatchDataAccessException(
        "Can't convert enum `" + v + "` of class " + v.getClass().getName());
  }

  protected Object toSqlTimestamp(Object v) {
    if (v.getClass().equals(long.class)) {
      return new Timestamp((long) v);
    } else if (v.getClass().equals(Long.class)) {
      return new Timestamp(((Long) v));
    } else if (v.getClass().equals(Timestamp.class) || v.getClass().equals(java.sql.Date.class) ||
        v.getClass().equals(java.util.Date.class)) {
      return v;
    }
    throw new TypeMismatchDataAccessException(
        "Can't convert timestamp `" + v + "` of " + v.getClass().getName());
  }

  @Override
  public <B extends Message> Object fromSqlValue(Class<B> messageClass, String field,
      Object sqlValue) {
    ProtoMessageHelper<B> helper = ProtoMessageHelper.getHelper(messageClass);
    Descriptors.FieldDescriptor fd = helper.checkFieldDescriptor(field);
    return fromSqlValue(helper, fd, sqlValue);
  }

  @Override
  public <M extends Message> Object fromSqlValue(ProtoMessageHelper<M> helper,
      Descriptors.FieldDescriptor fd, Object sqlValue) {
    if (fd.isMapField()) {
      // check map first, for map field is also repeated
      return parseMapFromString(helper, fd, sqlValue);
    } else if (fd.isRepeated()) {
      return parseListFromString(fd, sqlValue);
    } else if (fd.getJavaType().equals(Descriptors.FieldDescriptor.JavaType.BOOLEAN)) {
      return parseInt(sqlValue) != 0;
    } else if (fd.getJavaType().equals(Descriptors.FieldDescriptor.JavaType.ENUM)) {
      // EnumValueDescriptor
      return fd.getEnumType().findValueByNumber(parseInt(sqlValue));
    } else if (isTimestampField(fd)) {
      // 处理时间，把Timestamp映射成long
      Timestamp ts = (Timestamp) sqlValue;
      return ts.getTime();
    } else {
      return sqlValue;
    }
  }

  protected int parseInt(Object sqlValue) {
    if (sqlValue.getClass().equals(int.class) || sqlValue.getClass().equals(Integer.class)) {
      return (int) sqlValue;
    } else {
      throw new TypeMismatchDataAccessException(
          "should be an int value, sqlValue=" + sqlValue + ", type=" + sqlValue.getClass());
    }
  }

  /**
   * @return a list of {@link MapEntry}, because setter of a map field only accept value of this kind
   */
  protected List<MapEntry<?, ?>> parseMapFromString(ProtoMessageHelper<?> helper,
      Descriptors.FieldDescriptor fd, Object value) {
    if (value == null) {
      return Collections.emptyList();
    }
    if (!(value instanceof String)) {
      throw new TypeMismatchDataAccessException(
          "value to parse must be string for a map field: " + fd.getFullName() + ", value=" +
              value + ", valCls=" + value.getClass());
    }
    String text = (String) value;
    if (StringUtils.isBlank(text)) {
      return Collections.emptyList();
    }
    Descriptors.FieldDescriptor keyFd = fd.getMessageType().findFieldByName("key");
    Descriptors.FieldDescriptor valFd = fd.getMessageType().findFieldByName("value");
    List<MapEntry<?, ?>> mapEntries = Lists.newArrayList();
    Map<String, String> map = MAP_SPLITTER.split(text);
    for (Map.Entry<String, String> entry : map.entrySet()) {
      Object k = lookupTransform(keyFd).apply(MAP_VALUE_UNESCAPE.translate(entry.getKey()));
      Object v = lookupTransform(valFd).apply(MAP_VALUE_UNESCAPE.translate(entry.getValue()));
      MapEntry.Builder<?, ?> entryBuilder = (MapEntry.Builder<?, ?>) helper.newBuilderForField(fd);
      entryBuilder.setField(keyFd, k).setField(valFd, v);
      mapEntries.add(entryBuilder.build());
    }
    return mapEntries;
  }

  protected List<?> parseListFromString(Descriptors.FieldDescriptor fd, Object value) {
    if (value == null) {
      return Collections.emptyList();
    }
    if (!(value instanceof String)) {
      throw new TypeMismatchDataAccessException(
          "value to parse must be string for a repeated field: " + fd.getFullName() + ", value=" +
              value + ", valCls=" + value.getClass());
    }
    String text = (String) value;
    if (StringUtils.isBlank(text)) {
      return Collections.emptyList();
    }
    // 忽略最后一个分隔符
    if (text.endsWith(LIST_SEP)) text = text.substring(0, text.length() - LIST_SEP.length());
    List<String> list = LIST_SPLITTER.splitToList(text);
    return list.stream().map(LIST_VALUE_UNESCAPE::translate).map(lookupTransform(fd))
        .collect(Collectors.toList());
  }

  private Function<String, Object> lookupTransform(Descriptors.FieldDescriptor fd) {
    switch (fd.getJavaType()) {
      case BOOLEAN:
        return text -> (Integer.parseInt(Objects.requireNonNull(text)) != 0);
      case ENUM:
        return text -> fd.getEnumType()
            .findValueByNumber(Integer.parseInt(Objects.requireNonNull(text)));
      case INT:
        return Integer::parseInt;
      case LONG:
        return Long::parseLong;
      case FLOAT:
        return Float::parseFloat;
      case DOUBLE:
        return Double::parseDouble;
      case STRING:
        return text -> text;
      default:
        throw new TypeMismatchDataAccessException(
            "not support java type: " + fd.getJavaType() + ", field=" + fd.getFullName());
    }
  }

  @Override
  public Class<?> resolveSqlValueType(Descriptors.FieldDescriptor fd) {
    // map/list 使用string拼接
    if (fd.isMapField() || fd.isRepeated()) return String.class;
    switch (fd.getJavaType()) {
      case BOOLEAN:
        return int.class;
      case STRING:
        return String.class;
      case DOUBLE:
        return double.class;
      case FLOAT:
        return float.class;
      case INT:
        return int.class;
      case LONG:
        // 特殊处理时间field
        if (isTimestampField(fd)) {
          return Timestamp.class;
        }
        return long.class;
      case ENUM:
        return int.class;
      case MESSAGE:
      case BYTE_STRING:
      default:
        throw new TypeMismatchDataAccessException(
            "Not support " + fd.getJavaType() + " of " + fd.getFullName());
    }
  }

}
