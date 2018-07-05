package org.protoframework.core.codec;

import javax.annotation.Nullable;
import java.io.IOException;

/**
 * Created by tuqc on 15-5-24.
 */
public interface ICodec<T> {

  T decode(@Nullable byte[] data) throws IOException;

  byte[] encode(@Nullable T v);

}
