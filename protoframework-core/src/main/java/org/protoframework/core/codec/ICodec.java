package org.protoframework.core.codec;

import javax.annotation.Nullable;

/**
 * Created by tuqc on 15-5-24.
 */
public interface ICodec<T> {

  T decode(@Nullable byte[] data);

  byte[] encode(@Nullable T v);

}
