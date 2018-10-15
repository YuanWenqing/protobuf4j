package protobufframework.core.codec;

import javax.annotation.Nullable;
import java.io.IOException;

/**
 * Created by tuqc on 15-5-24.
 */
public interface ICodec<T> {
  /**
   * 获取该编码器处理的值的类型
   */
  Class<T> getValueType();

  T decode(@Nullable byte[] data) throws IOException;

  byte[] encode(@Nullable T v);

}
