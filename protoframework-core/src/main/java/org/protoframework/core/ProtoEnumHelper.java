package org.protoframework.core;

import com.google.common.base.Preconditions;
import com.google.protobuf.Descriptors;
import com.google.protobuf.Internal;
import com.google.protobuf.ProtocolMessageEnum;
import org.apache.commons.lang3.reflect.MethodUtils;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * author: yuanwq
 * date: 2018/7/9
 */
public class ProtoEnumHelper<T extends ProtocolMessageEnum> implements IBeanHelper<T> {
  @SuppressWarnings("rawtypes")
  private static ConcurrentHashMap<String, ProtoEnumHelper> helperMap = new ConcurrentHashMap<>();

  @SuppressWarnings("unchecked")
  public static <T extends ProtocolMessageEnum> ProtoEnumHelper<T> getHelper(
      @Nonnull Class<T> cls) {
    Preconditions.checkNotNull(cls);
    if (!helperMap.contains(cls.getName())) {
      ProtoEnumHelper<T> helper = new ProtoEnumHelper<>(cls);
      helperMap.putIfAbsent(cls.getName(), helper);
    }
    return helperMap.get(cls.getName());
  }

  @SuppressWarnings("unchecked")
  public static <T extends ProtocolMessageEnum> ProtoEnumHelper<T> getHelper(@Nonnull T enumValue) {
    Preconditions.checkNotNull(enumValue);
    return (ProtoEnumHelper<T>) getHelper(enumValue.getClass());
  }

  private final Class<T> cls;
  private final Descriptors.EnumDescriptor descriptor;
  private final Function<Integer, T> forNumberFunc;
  private final T unrecognized;

  @SuppressWarnings("unchecked")
  public ProtoEnumHelper(Class<T> cls) {
    this.cls = cls;
    this.descriptor = (Descriptors.EnumDescriptor) invokeStaticMethodUnchecked("getDescriptor");
    Internal.EnumLiteMap<T> enumLiteMap =
        (Internal.EnumLiteMap<T>) invokeStaticMethodUnchecked("internalGetValueMap");
    this.forNumberFunc = enumLiteMap::findValueByNumber;
    try {
      this.unrecognized = (T) this.cls.getField("UNRECOGNIZED").get(null);
    } catch (IllegalAccessException | NoSuchFieldException e) {
      throw new RuntimeException(e);
    }
  }

  private Object invokeStaticMethodUnchecked(String method) {
    try {
      return MethodUtils.invokeStaticMethod(this.cls, method);
    } catch (Exception e) {
      throw new RuntimeException("fail to invoke static method `" + method + "` on " + this.cls, e);
    }
  }

  public Descriptors.EnumDescriptor getDescriptor() {
    return descriptor;
  }

  public T getUnrecognizedValue() {
    return unrecognized;
  }

  public T byName(String name) {
    Descriptors.EnumValueDescriptor valueDescriptor = descriptor.findValueByName(name);
    if (valueDescriptor == null) {
      return null;
    }
    return byNumber(valueDescriptor.getNumber());
  }

  public T byNumber(int number) {
    return forNumberFunc.apply(number);
  }

  @Override
  public Class<? extends T> getType() {
    return cls;
  }

  @Override
  public T defaultValue() {
    return byNumber(0);
  }

  @Override
  public boolean isEmpty(T enumValue) {
    if (enumValue == null) {
      return true;
    }
    if (this.unrecognized.equals(enumValue)) {
      return false;
    }
    return enumValue.getNumber() == 0;
  }

  @Override
  public boolean hasField(String enumName) {
    return byName(enumName) != null;
  }

  @Override
  public Set<String> getFieldNames() {
    return descriptor.getValues().stream().map(Descriptors.EnumValueDescriptor::getName)
        .collect(Collectors.toSet());
  }

  @Override
  public Class<?> getFieldType(String enumName) {
    throw new UnsupportedOperationException("cls=" + this.cls.getName() + ", field=" + enumName);
  }

  @Override
  public Map<String, Class<?>> getFieldTypeMap() {
    throw new UnsupportedOperationException(this.cls.getName());
  }

  @Override
  public boolean isFieldSet(T enumValue, String enumName) {
    throw new UnsupportedOperationException("cls=" + this.cls.getName() + ", field=" + enumName);
  }

  @Override
  public Object getFieldValue(T enumValue, String enumName) {
    throw new UnsupportedOperationException("cls=" + this.cls.getName() + ", field=" + enumName);
  }

  @Override
  public T setFieldValue(T enumValue, String enumName, Object fieldValue) {
    throw new UnsupportedOperationException("cls=" + this.cls.getName() + ", field=" + enumName);
  }

  @Override
  public String toString(T enumValue) {
    if (enumValue == null) {
      return "null";
    }
    if (this.unrecognized.equals(enumValue)) {
      return "UNRECOGNIZED[-1]";
    }
    return String.format("%s[%d]", enumValue.getValueDescriptor().getName(), enumValue.getNumber());
  }
}
