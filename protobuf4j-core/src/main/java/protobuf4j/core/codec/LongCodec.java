package protobuf4j.core.codec;

import javax.annotation.Nullable;

/**
 * Created by tuqc on 15-5-24.
 */
public class LongCodec implements ICodec<Long> {
  public static final LongCodec INSTANCE = new LongCodec();

  private LongCodec() {
  }

  @Override
  public Class<Long> getValueType() {
    return Long.class;
  }

  private StringCodec nativeCodec() {
    return StringCodec.INSTANCE;
  }

  @Override
  public Long decode(@Nullable byte[] data) {
    if (data == null) return null;
    return Long.valueOf(nativeCodec().decode(data));
  }

  @Override
  public byte[] encode(@Nullable Long v) {
    if (v == null) {
      return null;
    }
    return nativeCodec().encode(String.valueOf(v));
  }
}
