package org.protoframework.core.codec;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author: yuanwq
 * @date: 2018/7/5
 */
public class TestCodec {
  @Test
  public void testBoolean() {
    assertEquals(true, BooleanCodec.INSTANCE.decode(BooleanCodec.INSTANCE.encode(true)));
    assertEquals(false, BooleanCodec.INSTANCE.decode(BooleanCodec.INSTANCE.encode(false)));
  }

  @Test
  public void testInteger() {
    for (int i = 0; i < 1000; i++) {
      Integer value = RandomUtils.nextInt(0, Integer.MAX_VALUE);
      assertEquals(value, IntegerCodec.INSTANCE.decode(IntegerCodec.INSTANCE.encode(value)));
      value = 0 - value;
      assertEquals(value, IntegerCodec.INSTANCE.decode(IntegerCodec.INSTANCE.encode(value)));
    }
  }

  @Test
  public void testLong() {
    for (int i = 0; i < 1000; i++) {
      Long value = RandomUtils.nextLong(0, Long.MAX_VALUE);
      assertEquals(value, LongCodec.INSTANCE.decode(LongCodec.INSTANCE.encode(value)));
      value = 0 - value;
      assertEquals(value, LongCodec.INSTANCE.decode(LongCodec.INSTANCE.encode(value)));
    }
  }

  @Test
  public void testFloat() {
    for (int i = 0; i < 1000; i++) {
      Float value = RandomUtils.nextFloat(0, Float.MAX_VALUE);
      assertEquals(value, FloatCodec.INSTANCE.decode(FloatCodec.INSTANCE.encode(value)));
      value = 0 - value;
      assertEquals(value, FloatCodec.INSTANCE.decode(FloatCodec.INSTANCE.encode(value)));
    }
  }

  @Test
  public void testDouble() {
    for (int i = 0; i < 1000; i++) {
      Double value = RandomUtils.nextDouble(0, Double.MAX_VALUE);
      assertEquals(value, DoubleCodec.INSTANCE.decode(DoubleCodec.INSTANCE.encode(value)));
      value = 0 - value;
      assertEquals(value, DoubleCodec.INSTANCE.decode(DoubleCodec.INSTANCE.encode(value)));
    }
  }

  @Test
  public void testString() {
    for (int i = 0; i < 1000; i++) {
      String value = RandomStringUtils.random(100);
      assertEquals(value, StringCodec.INSTANCE.decode(StringCodec.INSTANCE.encode(value)));
    }
  }

}
