package org.protoframework.core;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.protobuf.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 处理Protobuf Message反射的辅助类
 *
 * @author: yuanwq
 * @date: 2018/7/2
 */
public class ProtoMessageHelper<T extends Message> implements IBeanHelper<T> {
  @SuppressWarnings("rawtypes")
  private static ConcurrentHashMap<String, ProtoMessageHelper> helperMap =
      new ConcurrentHashMap<>();

  @SuppressWarnings("unchecked")
  public static <T extends Message> ProtoMessageHelper<T> getHelper(@Nonnull Class<T> cls) {
    Preconditions.checkNotNull(cls);
    if (!helperMap.contains(cls.getName())) {
      ProtoMessageHelper<T> helper = new ProtoMessageHelper<>(cls);
      helperMap.putIfAbsent(cls.getName(), helper);
    }
    return helperMap.get(cls.getName());
  }

  @SuppressWarnings("unchecked")
  public static <T extends Message> ProtoMessageHelper<T> getHelper(
      @Nonnull MessageOrBuilder messageOrBuilder) {
    Preconditions.checkNotNull(messageOrBuilder);
    return (ProtoMessageHelper<T>) getHelper(
        messageOrBuilder.getDefaultInstanceForType().getClass());
  }

  private static final String METHOD_GET_DESCRIPTOR = "getDescriptor";
  private static final String METHOD_NEW_BUILDER = "newBuilder";

  private final Class<T> cls;
  private Descriptors.Descriptor descriptor;
  private Map<String, Descriptors.FieldDescriptor> field2descriptor;
  private Map<String, Class<?>> field2type;
  private Message.Builder internalBuilder;

  private ProtoMessageHelper(Class<T> cls) {
    Preconditions.checkNotNull(cls);
    this.cls = cls;
    doInit();
  }

  private void doInit() {
    this.descriptor = (Descriptors.Descriptor) invokeStaticMethodUnchecked(METHOD_GET_DESCRIPTOR);
    this.internalBuilder = newBuilder();
    ImmutableMap.Builder<String, Descriptors.FieldDescriptor> field2descriptorBuilder =
        ImmutableMap.builder();
    ImmutableMap.Builder<String, Class<?>> field2typeBuilder = ImmutableMap.builder();

    for (Descriptors.FieldDescriptor fd : this.descriptor.getFields()) {
      String name = fd.getName();
      field2descriptorBuilder.put(name, fd);
      field2typeBuilder.put(name, resolveFieldType(fd));
    }
    this.field2descriptor = field2descriptorBuilder.build();
    this.field2type = field2typeBuilder.build();
  }

  /**
   * 获取字段的类型，repeated字段返回其集合类型
   *
   * @see #resolveRepeatedFieldValueType(Descriptors.FieldDescriptor)
   * @see #resolveMapFieldKeyType(Descriptors.FieldDescriptor)
   * @see #resolveMapFieldValueType(Descriptors.FieldDescriptor)
   */
  private Class<?> resolveFieldType(Descriptors.FieldDescriptor fd) {
    if (fd.isMapField()) {
      // first check map, because map field is also repeated
      return Map.class;
    } else if (fd.isRepeated()) {
      return List.class;
    }
    switch (fd.getJavaType()) {
      case MESSAGE:
        return internalBuilder.newBuilderForField(fd).build().getClass();
      case ENUM:
        // 通过反射取返回值类型的方式取enum field的类型
        String methodName = "get" + StringUtils.capitalize(fd.getJsonName());
        try {
          Method method = internalBuilder.getClass().getMethod(methodName);
          return method.getReturnType();
        } catch (Exception e) {
          throw new RuntimeException(
              "fail to resolve type of enum field `" + fd.getFullName() + "`, method: " +
                  methodName, e);
        }
        // 下面的方式取得的 enum field的类型依然是EnumValueDescriptor
        // this.mappedValueTypes.put(name, fd.getEnumType().findValueByNumber(0).getClass());
      default:
        return resolveBasicValueType(fd);
    }
  }

  static Class<?> resolveBasicValueType(Descriptors.FieldDescriptor fd) {
    switch (fd.getJavaType()) {
      case INT:
        return Integer.class;
      case LONG:
        return Long.class;
      case FLOAT:
        return Float.class;
      case DOUBLE:
        return Double.class;
      case BOOLEAN:
        return Boolean.class;
      case STRING:
        return String.class;
      case BYTE_STRING:
        return ByteString.class;
      default:
        throw new IllegalArgumentException(
            "not a basic type field: " + fd.getFullName() + ", javaType=" + fd.getJavaType());
    }
  }

  public Class<?> resolveRepeatedFieldValueType(Descriptors.FieldDescriptor fd) {
    Preconditions.checkArgument(fd.isRepeated(), "not a repeated field: " + fd.getFullName());
    if (fd.isMapField()) {
      return MapEntry.class;
    }
    switch (fd.getJavaType()) {
      case MESSAGE:
        return internalBuilder.newBuilderForField(fd).build().getClass();
      case ENUM:
        // 通过反射取返回值类型的方式取enum field的类型
        String methodName = "get" + StringUtils.capitalize(fd.getJsonName());
        try {
          Method method = internalBuilder.getClass().getMethod(methodName, int.class);
          return method.getReturnType();
        } catch (Exception e) {
          throw new RuntimeException(
              "fail to resolve type of enum field `" + fd.getFullName() + "`, method: " +
                  methodName, e);
        }
      default:
        return resolveBasicValueType(fd);
    }
  }

  public Class<?> resolveMapFieldKeyType(Descriptors.FieldDescriptor fd) {
    Preconditions.checkArgument(fd.isMapField(), "not a map field: " + fd.getFullName());
    Descriptors.FieldDescriptor keyFd = fd.getMessageType().findFieldByName("key");
    // actually only integral or string type
    return resolveBasicValueType(keyFd);
  }

  public Class<?> resolveMapFieldValueType(Descriptors.FieldDescriptor fd) {
    Preconditions.checkArgument(fd.isMapField(), "not a map field: " + fd.getFullName());
    Descriptors.FieldDescriptor valueFd = fd.getMessageType().findFieldByName("value");
    switch (valueFd.getJavaType()) {
      case MESSAGE:
      case ENUM:
        String methodName = "put" + StringUtils.capitalize(fd.getJsonName());
        for (Method method : internalBuilder.getClass().getMethods()) {
          if (method.getName().equals(methodName)) {
            return method.getParameterTypes()[1];
          }
        }
        throw new RuntimeException("never here: " + fd);
      default:
        return resolveBasicValueType(valueFd);
    }
  }

  private Object invokeStaticMethodUnchecked(String method) {
    try {
      return MethodUtils.invokeStaticMethod(this.cls, method);
    } catch (Exception e) {
      throw new RuntimeException("fail to invoke static method `" + method + "` on " + this.cls, e);
    }
  }

  public Descriptors.Descriptor getDescriptor() {
    return descriptor;
  }

  @SuppressWarnings("unchecked")
  public <R extends Message.Builder> R newBuilder() {
    return (R) invokeStaticMethodUnchecked(METHOD_NEW_BUILDER);
  }

  public Message.Builder newBuilderForField(String fieldName) {
    Descriptors.FieldDescriptor fd = checkField(fieldName);
    return newBuilderForField(fd);
  }

  public Message.Builder newBuilderForField(Descriptors.FieldDescriptor fd) {
    return internalBuilder.newBuilderForField(fd);
  }

  @Override
  public Class<? extends T> getType() {
    return cls;
  }

  @SuppressWarnings("unchecked")
  @Override
  public T defaultValue() {
    return (T) internalBuilder.getDefaultInstanceForType();
  }

  @Override
  public boolean isEmpty(T msg) {
    return msg == null || defaultValue().equals(msg);
  }

  @Override
  public boolean hasField(String fieldName) {
    return field2descriptor.containsKey(fieldName);
  }

  @Override
  public Set<String> getFieldNames() {
    return field2descriptor.keySet();
  }

  @Override
  public Class<?> getFieldType(String fieldName) {
    return field2type.get(fieldName);
  }

  public Descriptors.FieldDescriptor getFieldDescriptor(String fieldName) {
    return field2descriptor.get(fieldName);
  }

  public Map<String, Descriptors.FieldDescriptor> getFieldDescriptorMap() {
    return field2descriptor;
  }

  public List<Descriptors.FieldDescriptor> getFieldDescriptorList() {
    return descriptor.getFields();
  }

  @Override
  public Map<String, Class<?>> getFieldTypeMap() {
    return field2type;
  }

  private Descriptors.FieldDescriptor checkField(String fieldName) {
    Descriptors.FieldDescriptor fd = getFieldDescriptor(fieldName);
    if (fd == null) {
      throw new RuntimeException("no field named as `" + fieldName + "` in " + this.cls);
    }
    return fd;
  }

  @Override
  public boolean isFieldSet(T msg, String fieldName) {
    Descriptors.FieldDescriptor fd = checkField(fieldName);
    if (fd.isRepeated()) {
      return !((Collection<?>) msg.getField(fd)).isEmpty();
    }
    return msg.hasField(fd);
  }

  @Override
  public Object getFieldValue(T msg, String fieldName) {
    Descriptors.FieldDescriptor fd = checkField(fieldName);
    return msg.getField(fd);
  }

  @SuppressWarnings("unchecked")
  @Override
  public T setFieldValue(T msg, String fieldName, Object fieldValue) {
    Descriptors.FieldDescriptor fd = checkField(fieldName);
    return (T) msg.toBuilder().setField(fd, fieldValue).build();
  }

  @Override
  public String toString(T msg) {
    String text;
    if (msg == null) {
      text = "null";
    } else {
      // TextFormat.shortDebugString 中文是类似"\350\212\261\345"这样的编码，无法可视化
      // 所以，我们用下面这种方式，略tricky
      text = TextFormat.printToUnicodeString(msg);
      // 把内容中的\r转义，以免在进行文件读取一行时因为\r分行导致出错
      text = text.replace("\r", "\\r");
      // text format中换行都是\n，把它们变成空格
      text = text.replace("\n", " ");
    }
    return String.format("%s{%s}", descriptor.getFullName(), text);
  }
}
