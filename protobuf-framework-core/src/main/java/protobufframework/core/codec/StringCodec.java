package protobufframework.core.codec;

import com.google.common.base.Charsets;

import javax.annotation.Nullable;

/**
 * Created by tuqc on 15-5-24.
 */
public class StringCodec implements ICodec<String> {
  public static final StringCodec INSTANCE = new StringCodec();

  private StringCodec() {
  }

  @Override
  public Class<String> getValueType() {
    return String.class;
  }

  @Override
  public String decode(@Nullable byte[] data) {
    if (data == null) return null;
    return new String(data, Charsets.UTF_8);
  }

  @Override
  public byte[] encode(@Nullable String v) {
    if (v == null) {
      return null;
    }
    return v.getBytes(Charsets.UTF_8);
  }

}
