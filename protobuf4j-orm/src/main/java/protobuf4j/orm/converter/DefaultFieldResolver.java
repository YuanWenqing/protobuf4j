/**
 * author yuanwq, date: 2017年4月27日
 */
package protobuf4j.orm.converter;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.protobuf.Descriptors;
import com.google.protobuf.Internal;
import com.google.protobuf.MapEntry;
import com.google.protobuf.Message;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.translate.EntityArrays;
import org.apache.commons.text.translate.LookupTranslator;
import org.springframework.dao.TypeMismatchDataAccessException;
import protobuf4j.core.ProtoMessageHelper;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.*;

/**
 */
public class DefaultFieldResolver<M extends Message> implements IFieldResolver {
  protected static final String LIST_SEP = ",";
  protected static final String MAP_KV_SEP = ":";
  protected static final String MAP_ENTRY_SEP = ";";
  protected static final Map<CharSequence, CharSequence> LIST_LOOKUP;

  static {
    ImmutableMap.Builder<CharSequence, CharSequence> builder = ImmutableMap.builder();
    builder.put(",", "%2c");
    builder.put("%", "%25");
    LIST_LOOKUP = builder.build();
  }

  protected static final LookupTranslator LIST_VALUE_ESCAPE = new LookupTranslator(LIST_LOOKUP);
  protected static final LookupTranslator LIST_VALUE_UNESCAPE =
      new LookupTranslator(EntityArrays.invert(LIST_LOOKUP));
  protected static final Map<CharSequence, CharSequence> MAP_LOOKUP;

  static {
    ImmutableMap.Builder<CharSequence, CharSequence> builder = ImmutableMap.builder();
    builder.put(":", "%3a");
    builder.put(";", "%3b");
    builder.put("%", "%25");
    MAP_LOOKUP = builder.build();
  }

  protected static final LookupTranslator MAP_VALUE_ESCAPE = new LookupTranslator(MAP_LOOKUP);
  protected static final LookupTranslator MAP_VALUE_UNESCAPE =
      new LookupTranslator(EntityArrays.invert(MAP_LOOKUP));
  // not omit empty string
  protected static final Splitter LIST_SPLITTER = Splitter.on(LIST_SEP);
  // omit empty string, 因为entry至少包含一个“:”分隔符，不可能为空
  protected static final Splitter MAP_ENTRY_SPLITTER =
      Splitter.on(MAP_ENTRY_SEP).omitEmptyStrings();
  protected static final Splitter.MapSplitter MAP_SPLITTER =
      MAP_ENTRY_SPLITTER.withKeyValueSeparator(MAP_KV_SEP);

  private final Class<M> messageClass;
  private final ProtoMessageHelper<M> messageHelper;
  private final BasicTypeFieldResolver basicTypeFieldResolver;
  private final TimestampFieldConverter timestampFieldConverter;

  public DefaultFieldResolver(Class<M> messageClass) {
    checkNotNull(messageClass);
    this.messageClass = messageClass;
    this.messageHelper = ProtoMessageHelper.getHelper(messageClass);
    this.basicTypeFieldResolver = new BasicTypeFieldResolver();
    this.timestampFieldConverter = new TimestampFieldConverter();
  }

  @Override
  public Object toSqlValue(Descriptors.FieldDescriptor fd, Object value) {
    if (fd.isMapField()) {
      // check map first, for map field is also repeated
      return encodeMapToString(fd, value);
    } else if (fd.isRepeated()) {
      return encodeListToString(fd, value);
    } else {
      IFieldValueConverter fieldConverter = findFieldConverter(fd);
      return fieldConverter.toSqlValue(value);
    }
  }

  private IFieldValueConverter findFieldConverter(Descriptors.FieldDescriptor fieldDescriptor) {
    if (fieldDescriptor.isMapField()) {
      return new MapFieldConverter();
    }
    if (isTimestampField(fieldDescriptor)) {
      return timestampFieldConverter;
    }
    return basicTypeFieldResolver.findFieldConverter(fieldDescriptor);
  }

  public boolean isTimestampField(Descriptors.FieldDescriptor fd) {
    return !fd.isRepeated() && Descriptors.FieldDescriptor.JavaType.LONG.equals(fd.getJavaType()) &&
        fd.getName().endsWith("_time");
  }

  /**
   * 字段值映射：{@code proto type -> sql type}
   *
   * @param javaType
   * @param value
   */
  protected Object toSqlValue(Descriptors.FieldDescriptor.JavaType javaType, Object value) {
    IFieldValueConverter converter = typeConverterMap.get(javaType);
    if (converter == null) {
      throw new FieldConversionException(
          "no converter found, javaType=" + javaType + ", value=`" + value + "`, valueType=" +
              value.getClass().getName());
    }
    return converter.toSqlValue(value);
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  protected String encodeMapToString(Descriptors.FieldDescriptor fd, Object value) {
    Descriptors.FieldDescriptor keyFd = fd.getMessageType().findFieldByName("key");
    Descriptors.FieldDescriptor valFd = fd.getMessageType().findFieldByName("value");
    try {
      if (value instanceof Map) {
        Map<?, ?> map = (Map<?, ?>) value;
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<?, ?> entry : map.entrySet()) {
          Object k = toSqlValue(keyFd.getJavaType(), entry.getKey());
          k = MAP_VALUE_ESCAPE.translate(String.valueOf(k));
          Object v = toSqlValue(valFd.getJavaType(), entry.getValue());
          v = MAP_VALUE_ESCAPE.translate(String.valueOf(v));
          sb.append(k).append(MAP_KV_SEP).append(v).append(MAP_ENTRY_SEP);
        }
        return sb.toString();
      } else if (value instanceof List) {
        List<? extends MapEntry> list = (List<? extends MapEntry>) value;
        StringBuilder sb = new StringBuilder();
        for (MapEntry entry : list) {
          Object k = toSqlValue(keyFd.getJavaType(), entry.getKey());
          k = MAP_VALUE_ESCAPE.translate(String.valueOf(k));
          Object v = toSqlValue(valFd.getJavaType(), entry.getValue());
          v = MAP_VALUE_ESCAPE.translate(String.valueOf(v));
          sb.append(k).append(MAP_KV_SEP).append(v).append(MAP_ENTRY_SEP);
        }
        return sb.toString();
      }
    } catch (TypeMismatchDataAccessException e) {
      throw new TypeMismatchDataAccessException(
          "fail to encode map value, fd=" + fd + ", value=`" + value + "`, valueType=" +
              value.getClass().getName(), e);
    }
    throw new TypeMismatchDataAccessException(
        "fail to encode map value, fd=" + fd + ", value=`" + value + "`, valueType=" +
            value.getClass().getName());
  }

  protected String encodeListToString(Descriptors.FieldDescriptor fd, Object value) {
    try {
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
      }
    } catch (TypeMismatchDataAccessException e) {
      throw new TypeMismatchDataAccessException(
          "fail to encode list value, fd=" + fd + ", value=`" + value + "`, valueType=" +
              value.getClass().getName(), e);
    }
    throw new TypeMismatchDataAccessException(
        "fail to encode list value, fd=" + fd + ", value=`" + value + "`, valueType=" +
            value.getClass().getName());
  }

  protected int boolToInt(Object v) {
    if (v.getClass().equals(Boolean.class)) {
      return (Boolean) v ? 1 : 0;
    }
    if (v.getClass().equals(Integer.class)) {
      int i = (Integer) v;
      if (i == 0 || i == 1) {
        return i;
      }
    }
    throw new TypeMismatchDataAccessException(
        "fail to convert, fieldType=" + Descriptors.FieldDescriptor.JavaType.BOOLEAN + ", value=`" +
            v + "`, valueType=" + v.getClass().getName());
  }

  protected int enumToInt(Object v) {
//    if (v instanceof ProtocolMessageEnum) {
//      return ((ProtocolMessageEnum) v).getNumber();
//    }
    if (v instanceof Internal.EnumLite) {
      return ((Internal.EnumLite) v).getNumber();
    }
    if (v instanceof Number) {
      return ((Number) v).intValue();
    }
    throw new TypeMismatchDataAccessException(
        "fail to convert, fieldType=" + Descriptors.FieldDescriptor.JavaType.ENUM + ", value=`" +
            v + "`, valueType=" + v.getClass().getName());
  }

  protected Object toSqlTimestamp(Object v) {
    if (v instanceof Long) {
      return new Timestamp(((Long) v));
    } else if (v.getClass().equals(Timestamp.class) || v.getClass().equals(java.sql.Date.class) ||
        v.getClass().equals(java.util.Date.class)) {
      return v;
    }
    throw new TypeMismatchDataAccessException(
        "fail to convert timestamp, value=`" + v + "`, valueType=" + v.getClass().getName());
  }

  @Override
  public Object fromSqlValue(Descriptors.FieldDescriptor fd, Object sqlValue) {
    if (fd.isMapField()) {
      // check map first, for map field is also repeated
      return parseMapFromString(fd, sqlValue);
    } else if (fd.isRepeated()) {
      return parseListFromString(fd, sqlValue);
    } else if (fd.getJavaType().equals(Descriptors.FieldDescriptor.JavaType.ENUM)) {
      // EnumValueDescriptor
      return fd.getEnumType().findValueByNumber(parseInt(sqlValue));
    } else {
      IFieldValueConverter converter = typeConverterMap.get(fd.getJavaType());
      if (converter == null) {
        throw new FieldConversionException(
            "no converter found, javaType=" + fd.getJavaType() + ", sqlValue=`" + sqlValue +
                "`, sqlValue.type=" + sqlValue.getClass().getName());
      }
      return converter.fromSqlValue(sqlValue);
    }
  }

  protected int parseInt(Object sqlValue) {
    if (sqlValue.getClass().equals(Integer.class)) {
      return (int) sqlValue;
    } else {
      throw new TypeMismatchDataAccessException(
          "expect an int value, sqlValue=" + sqlValue + ", type=" + sqlValue.getClass());
    }
  }

  /**
   * @return a list of {@link MapEntry}, because setter of a map field only accept value of this kind
   */
  protected List<MapEntry<?, ?>> parseMapFromString(Descriptors.FieldDescriptor fd, Object value) {
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
      MapEntry.Builder<?, ?> entryBuilder =
          (MapEntry.Builder<?, ?>) messageHelper.newBuilderForField(fd);
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

  protected Function<String, Object> lookupTransform(Descriptors.FieldDescriptor fd) {
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
    IFieldValueConverter fieldConverter = findFieldConverter(fd);
    return fieldConverter.getSqlValueType();
  }

}
