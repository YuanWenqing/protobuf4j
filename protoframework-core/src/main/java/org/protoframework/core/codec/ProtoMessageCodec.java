package org.protoframework.core.codec;

import com.google.common.base.Preconditions;
import com.google.protobuf.GeneratedMessageV3;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by tuqc on 15-5-24.
 */
public class ProtoMessageCodec<T extends GeneratedMessageV3> implements ICodec<T> {

  Class<T> cls;
  Method parseMethod;

  public ProtoMessageCodec(@Nonnull Class<T> cls) {
    Preconditions.checkNotNull(cls);
    this.cls = cls;
    try {
      parseMethod = cls.getDeclaredMethod("parseFrom", byte[].class);
    } catch (NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public T decode(@Nullable byte[] data) {
    if (data == null) return null;
    try {
      return (T) parseMethod.invoke(cls, data);
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public byte[] encode(@Nullable T v) {
    if (v == null) return null;
    return v.toByteArray();
  }
}
