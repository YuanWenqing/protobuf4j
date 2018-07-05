package org.protoframework.core.codec;

import javax.annotation.Nullable;

/**
 * @author: yuanwq
 * @date: 2018/7/5
 */
public class FloatCodec implements ICodec<Float> {
  public static final FloatCodec INSTANCE = new FloatCodec();

  private FloatCodec() {
  }

  private StringCodec nativeCodec() {
    return StringCodec.INSTANCE;
  }

  @Override
  public Float decode(@Nullable byte[] data) {
    if (data == null) return null;
    return Float.valueOf(nativeCodec().decode(data));
  }

  @Override
  public byte[] encode(@Nullable Float v) {
    if (v == null) {
      return null;
    }
    return nativeCodec().encode(String.valueOf(v));
  }

}
