/**
 * @author yuanwq, date: 2016年7月8日
 */
package protobufframework.core.codec;

import com.google.common.base.Preconditions;
import com.google.protobuf.ProtocolMessageEnum;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author yuanwq
 */
public class ProtoEnumCodec<T extends ProtocolMessageEnum> implements ICodec<T> {
  private final Class<T> cls;
  private final Method parseMethod;
  private final T unrecognized;

  @SuppressWarnings("unchecked")
  public ProtoEnumCodec(@Nonnull Class<T> cls) {
    Preconditions.checkNotNull(cls);
    this.cls = cls;
    try {
      parseMethod = cls.getDeclaredMethod("forNumber", int.class);
      unrecognized = (T) cls.getField("UNRECOGNIZED").get(null);
    } catch (NoSuchMethodException | NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Class<T> getValueType() {
    return cls;
  }

  private ICodec<Integer> nativeCodec() {
    return IntegerCodec.INSTANCE;
  }

  @SuppressWarnings("unchecked")
  @Override
  public T decode(@Nullable byte[] data) throws IOException {
    if (data == null) return null;
    Integer num = nativeCodec().decode(data);
    try {
      T ret = (T) parseMethod.invoke(cls, num);
      return ret == null ? unrecognized : ret;
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new IOException(e);
    }
  }

  @Override
  public byte[] encode(@Nullable T v) {
    if (v == null) return null;
    if ("UNRECOGNIZED".equals(String.valueOf(v))) {
      return nativeCodec().encode(-1);
    }
    return nativeCodec().encode(v.getNumber());
  }

}
