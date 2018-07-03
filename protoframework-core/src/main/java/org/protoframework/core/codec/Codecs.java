package org.protoframework.core.codec;

import com.google.common.base.Preconditions;
import com.google.protobuf.Descriptors;
import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.ProtocolMessageEnum;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by tuqc on 15-5-24.
 */
public abstract class Codecs {

  private static ConcurrentHashMap<Class<?>, ProtoMessageCodec<?>> protoCodecMap =
      new ConcurrentHashMap<>();
  private static ConcurrentHashMap<Class<?>, ProtoEnumCodec<?>> protoEnumCodecMap =
      new ConcurrentHashMap<>();

  @SuppressWarnings("unchecked")
  public static <T extends GeneratedMessageV3> ProtoMessageCodec<T> getProtoMessageCodec(
      @Nonnull Class<T> cls) {
    Preconditions.checkNotNull(cls);
    if (!protoCodecMap.contains(cls)) {
      ProtoMessageCodec<T> codec = new ProtoMessageCodec<>(cls);
      protoCodecMap.putIfAbsent(cls, codec);
    }
    return (ProtoMessageCodec<T>) protoCodecMap.get(cls);
  }

  @SuppressWarnings("unchecked")
  public static <T extends ProtocolMessageEnum> ProtoEnumCodec<T> getProtoEnumCodec(
      @Nonnull Class<T> cls) {
    Preconditions.checkNotNull(cls);
    if (!protoEnumCodecMap.contains(cls)) {
      ProtoEnumCodec<T> codec = new ProtoEnumCodec<>(cls);
      protoEnumCodecMap.putIfAbsent(cls, codec);
    }
    return (ProtoEnumCodec<T>) protoEnumCodecMap.get(cls);
  }

  @SuppressWarnings("unchecked")
  public static <T> ICodec<T> getCodec(@Nonnull Class<? extends T> cls) {
    Preconditions.checkNotNull(cls);
    if (cls.equals(String.class)) {
      return (ICodec<T>) StringCodec.INSTANCE;
    } else if (GeneratedMessageV3.class.isAssignableFrom(cls)) {
      Class<GeneratedMessageV3> X = (Class<GeneratedMessageV3>) cls;
      return (ICodec<T>) getProtoMessageCodec(X);
    } else if (ProtocolMessageEnum.class.isAssignableFrom(cls)) {
      Class<ProtocolMessageEnum> X = (Class<ProtocolMessageEnum>) cls;
      return (ICodec<T>) getProtoEnumCodec(X);
    } else if (cls.equals(Long.class)) {
      return (ICodec<T>) LongCodec.INSTANCE;
    } else if (cls.equals(Integer.class)) {
      return (ICodec<T>) IntegerCodec.INSTANCE;
    } else if (cls.equals(Boolean.class)) {
      return (ICodec<T>) BooleanCodec.INSTANCE;
    }
    throw new RuntimeException("Can't find codec for " + cls.getName());
  }

  @SuppressWarnings("unchecked")
  public static <T> byte[] encode(@Nullable T v) {
    if (v == null) {
      return null;
    }
    if (v.getClass().equals(Descriptors.EnumValueDescriptor.class)) {
      return IntegerCodec.INSTANCE.encode(((Descriptors.EnumValueDescriptor) v).getNumber());
    }
    ICodec<T> codec = (ICodec<T>) getCodec(v.getClass());
    return codec.encode(v);
  }

  public static <T> T decode(@Nullable byte[] data, @Nonnull Class<T> clazz) {
    Preconditions.checkNotNull(clazz);
    if (data == null) {
      return null;
    }
    ICodec<T> codec = getCodec(clazz);
    return codec.decode(data);
  }
}
