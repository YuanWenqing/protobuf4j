package protobufframework.core;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.protobuf.Descriptors;
import com.google.protobuf.Internal;
import com.google.protobuf.ProtocolMessageEnum;
import org.apache.commons.lang3.reflect.MethodUtils;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * author: yuanwq
 * date: 2018/7/9
 */
public class ProtoEnumHelper<T extends ProtocolMessageEnum> implements IEnumHelper<T> {
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
  private final T unrecognized;
  private final Map<Integer, T> numberValueMap = Maps.newLinkedHashMap();
  private final Map<String, T> nameValueMap = Maps.newLinkedHashMap();

  @SuppressWarnings("unchecked")
  public ProtoEnumHelper(Class<T> cls) {
    this.cls = cls;
    this.descriptor = (Descriptors.EnumDescriptor) invokeStaticMethodUnchecked("getDescriptor");
    Internal.EnumLiteMap<T> enumLiteMap =
        (Internal.EnumLiteMap<T>) invokeStaticMethodUnchecked("internalGetValueMap");
    for (Descriptors.EnumValueDescriptor valueDescriptor : descriptor.getValues()) {
      String name = valueDescriptor.getName();
      int number = valueDescriptor.getNumber();
      T value = enumLiteMap.findValueByNumber(number);
      nameValueMap.put(name, value);
      numberValueMap.put(number, value);
    }
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

  @Override
  public Class<? extends T> getType() {
    return cls;
  }

  @Override
  public T defaultValue() {
    return forNumber(0);
  }

  @Override
  public Collection<T> getEnumValues() {
    return numberValueMap.values();
  }

  @Override
  public Set<String> getEnumValueNames() {
    return nameValueMap.keySet();
  }

  @Override
  public Set<Integer> getEnumValueNumbers() {
    return numberValueMap.keySet();
  }

  @Override
  public T of(String name) {
    return nameValueMap.get(name);
  }

  @Override
  public T forNumber(int number) {
    return numberValueMap.get(number);
  }

  @Override
  public String toString(T enumValue) {
    if (enumValue == null) {
      return String.format("%s{null}", descriptor.getFullName());
    }
    if (this.unrecognized.equals(enumValue)) {
      return String.format("%s{UNRECOGNIZED[-1]}", descriptor.getFullName());
    }
    return String
        .format("%s{%s[%d]}", descriptor.getFullName(), enumValue.getValueDescriptor().getName(),
            enumValue.getNumber());
  }
}
