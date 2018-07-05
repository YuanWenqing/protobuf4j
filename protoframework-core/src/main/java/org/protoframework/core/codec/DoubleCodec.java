package org.protoframework.core.codec;

import javax.annotation.Nullable;

/**
 * @author: yuanwq
 * @date: 2018/7/5
 */
public class DoubleCodec implements ICodec<Double> {
  public static final DoubleCodec INSTANCE = new DoubleCodec();

  private DoubleCodec() {
  }

  private StringCodec nativeCodec() {
    return StringCodec.INSTANCE;
  }

  @Override
  public Double decode(@Nullable byte[] data) {
    if (data == null) return null;
    return Double.valueOf(nativeCodec().decode(data));
  }

  @Override
  public byte[] encode(@Nullable Double v) {
    if (v == null) {
      return null;
    }
    return nativeCodec().encode(String.valueOf(v));
  }

}
