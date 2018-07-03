/**
 * @author yuanwq, date: 2016年7月8日
 */
package org.protoframework.core.codec;

import com.google.common.base.Preconditions;
import com.google.protobuf.ProtocolMessageEnum;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author yuanwq
 */
public class ProtoEnumCodec<T extends ProtocolMessageEnum> implements ICodec<T> {
  Class<T> cls;
  Method parseMethod;

  public ProtoEnumCodec(@Nonnull Class<T> cls) {
    Preconditions.checkNotNull(cls);
    this.cls = cls;
    try {
      parseMethod = cls.getDeclaredMethod("valueOf", int.class);
    } catch (NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
  }

  private ICodec<Integer> nativeCodec() {
    return IntegerCodec.INSTANCE;
  }

  @SuppressWarnings("unchecked")
  @Override
  public T decode(@Nullable byte[] data) {
    if (data == null) return null;
    Integer num = nativeCodec().decode(data);
    try {
      return (T) parseMethod.invoke(cls, num.intValue());
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public byte[] encode(@Nullable T v) {
    if (v == null) return null;
    return nativeCodec().encode(v.getNumber());
  }

}
