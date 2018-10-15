package protobufframework.core.codec;

import com.google.common.base.Preconditions;
import com.google.protobuf.GeneratedMessageV3;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by tuqc on 15-5-24.
 */
public class ProtoMessageCodec<T extends GeneratedMessageV3> implements ICodec<T> {
  private final Class<T> cls;
  private final Method parseMethod;

  public ProtoMessageCodec(@Nonnull Class<T> cls) {
    Preconditions.checkNotNull(cls);
    this.cls = cls;
    try {
      parseMethod = cls.getDeclaredMethod("parseFrom", byte[].class);
    } catch (NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Class<T> getValueType() {
    return cls;
  }

  @SuppressWarnings("unchecked")
  @Override
  public T decode(@Nullable byte[] data) throws IOException {
    if (data == null) return null;
    try {
      return (T) parseMethod.invoke(cls, data);
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new IOException(e);
    }
  }

  @Override
  public byte[] encode(@Nullable T v) {
    if (v == null) return null;
    return v.toByteArray();
  }
}
