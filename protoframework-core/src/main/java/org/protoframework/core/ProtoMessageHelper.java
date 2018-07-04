package org.protoframework.core;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.protobuf.Descriptors;
import com.google.protobuf.Message;
import com.google.protobuf.MessageOrBuilder;
import com.google.protobuf.TextFormat;
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

  private Class<?> resolveFieldType(Descriptors.FieldDescriptor fd) {
    if (fd.isMapField()) {
      // first check map, for map field is also repeated
      return Map.class;
    } else if (fd.isRepeated()) {
      return List.class;
    }
    return resolveFieldValueType(fd);
  }

  /**
   * 获取字段的值类型，如果字段是集合类型，则获取到的是集合元素类型
   */
  public Class<?> resolveFieldValueType(Descriptors.FieldDescriptor fd) {
    switch (fd.getJavaType()) {
      case INT:
      case LONG:
      case FLOAT:
      case DOUBLE:
      case BOOLEAN:
      case STRING:
      case BYTE_STRING:
        return fd.getDefaultValue().getClass();
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
    }
    throw new RuntimeException("never here");
  }

  private Object invokeStaticMethodUnchecked(String method) {
    try {
      return MethodUtils.invokeStaticMethod(this.cls, method);
    } catch (Exception e) {
      throw new RuntimeException("fail to invoke static method `" + method + "` on " + this.cls, e);
    }
  }

  @SuppressWarnings("unchecked")
  public <R extends Message.Builder> R newBuilder() {
    return (R) invokeStaticMethodUnchecked(METHOD_NEW_BUILDER);
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
    if (msg == null) {
      return "null";
    }
    // TextFormat.shortDebugString 中文是类似"\350\212\261\345"这样的编码，无法可视化
    // 所以，我们用下面这种方式，略tricky
    String line = TextFormat.printToUnicodeString(msg);
    // 把内容中的\r转义，以免在进行文件读取一行时因为\r分行导致出错
    line = line.replace("\r", "\\r");
    // text format中换行都是\n，把它们变成空格
    line = line.replace("\n", " ");
    return line;
  }
}
