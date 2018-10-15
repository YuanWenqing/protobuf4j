/**
 * @author yuanwq, date: 2015年8月13日
 */
package protobufframework.core.codec;

import javax.annotation.Nullable;

/**
 * @author yuanwq
 */
public class BooleanCodec implements ICodec<Boolean> {
  public static final BooleanCodec INSTANCE = new BooleanCodec();
  static final Integer TRUE = 1;
  static final Integer FALSE = 0;

  private BooleanCodec() {
  }

  private IntegerCodec nativeCodec() {
    return IntegerCodec.INSTANCE;
  }

  @Override
  public Class<Boolean> getValueType() {
    return Boolean.class;
  }

  @Override
  public Boolean decode(@Nullable byte[] data) {
    if (data == null) return null;
    return Boolean.valueOf(TRUE.equals(nativeCodec().decode(data)));
  }

  @Override
  public byte[] encode(@Nullable Boolean v) {
    if (v == null) {
      return null;
    }
    return nativeCodec().encode(v.booleanValue() ? TRUE : FALSE);
  }
}
