/**
 * @author yuanwq, date: 2017年4月27日
 */
package org.protoframework.dao;

import com.google.common.base.CaseFormat;
import com.google.common.base.Splitter;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.Descriptors.FieldDescriptor.JavaType;
import com.google.protobuf.Internal;
import com.google.protobuf.MapEntry;
import com.google.protobuf.Message;
import com.google.protobuf.ProtocolMessageEnum;
import org.apache.commons.lang3.text.translate.EntityArrays;
import org.apache.commons.lang3.text.translate.LookupTranslator;
import org.protoframework.core.ProtoMessageHelper;
import org.springframework.dao.TypeMismatchDataAccessException;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 */
public class ProtoSqlConverter implements ISqlConverter<Message> {
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
  protected static Splitter MAP_ENTRY_SPLITTER = Splitter.on(MAP_ENTRY_SEP).omitEmptyStrings();
  protected static Splitter.MapSplitter MAP_SPLITTER =
      MAP_ENTRY_SPLITTER.withKeyValueSeparator(MAP_KV_SEP);

  @Override
  public String tableName(Class<? extends Message> messageClass) {
    return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, messageClass.getSimpleName());
  }

  @Override
  public <B extends Message> Object toSqlValue(Class<B> messageClass, String field, Object value) {
    ProtoMessageHelper<B> helper = ProtoMessageHelper.getHelper(messageClass);
    FieldDescriptor fd = helper.checkFieldDescriptor(field);
    return toSqlValue(fd, value);
  }

  protected Object toSqlValue(FieldDescriptor fd, Object value) {
    if (fd.isMapField()) {
      return map2str(fd, value);
    } else if (fd.isRepeated()) {
      return list2str(fd, value);
    } else if (isTimestamp(fd)) {
      return toSqlTimestamp(value);
    } else {
      return toSqlValue(fd.getJavaType(), value);
    }
  }

  protected boolean isTimestamp(FieldDescriptor fd) {
    return JavaType.LONG.equals(fd.getJavaType()) && fd.getName().endsWith("_time");
  }

  /**
   * 字段值映射：{@code proto type -> sql type}
   */
  protected Object toSqlValue(JavaType javaType, Object value) {
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
  protected String map2str(FieldDescriptor fd, Object value) {
    FieldDescriptor keyFd = fd.getMessageType().findFieldByName("key");
    FieldDescriptor valFd = fd.getMessageType().findFieldByName("value");
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

  protected String list2str(FieldDescriptor fd, Object value) {
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
    // TODO:
    return null;
  }

}
