package org.protoframework.core.codec;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;
import org.protoframework.test.MsgsForTest;
import org.protoframework.core.proto.data.TestModel;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * @author: yuanwq
 * @date: 2018/7/5
 */
@SuppressWarnings("unchecked")
public class TestCodec {
  @Test
  public void testCodecs() throws IOException {
    ICodec codec = Codecs.getCodec(TestModel.MsgA.class);
    assertTrue(codec instanceof ProtoMessageCodec);
    assertEquals(TestModel.MsgA.class, codec.getValueType());

    codec = Codecs.getCodec(TestModel.EnumA.class);
    assertTrue(codec instanceof ProtoEnumCodec);
    assertEquals(TestModel.EnumA.class, codec.getValueType());

    assertTrue(Codecs.getCodec(String.class) instanceof StringCodec);
    assertTrue(Codecs.getCodec(Integer.class) instanceof IntegerCodec);
    assertTrue(Codecs.getCodec(Long.class) instanceof LongCodec);
    assertTrue(Codecs.getCodec(Float.class) instanceof FloatCodec);
    assertTrue(Codecs.getCodec(Double.class) instanceof DoubleCodec);
    assertTrue(Codecs.getCodec(Boolean.class) instanceof BooleanCodec);
    for (Class<?> cls : Lists
        .newArrayList(Integer.class, Long.class, Float.class, Double.class, Boolean.class,
            String.class, TestModel.MsgA.class, TestModel.EnumA.class)) {
      assertEquals(cls, Codecs.getCodec(cls).getValueType());
    }

    assertNull(Codecs.decode(Codecs.encode(null), Integer.class));
    assertEquals("A", Codecs.decode(Codecs.encode("A"), String.class));
  }

  @Test
  public void testBoolean() throws IOException {
    ICodec<Boolean> codec = BooleanCodec.INSTANCE;
    assertNull(codec.decode(codec.encode(null)));
    assertEquals(true, BooleanCodec.INSTANCE.decode(BooleanCodec.INSTANCE.encode(true)));
    assertEquals(false, BooleanCodec.INSTANCE.decode(BooleanCodec.INSTANCE.encode(false)));
  }

  @Test
  public void testInteger() throws IOException {
    ICodec<Integer> codec = IntegerCodec.INSTANCE;
    assertNull(codec.decode(codec.encode(null)));
    for (int i = 0; i < 1000; i++) {
      Integer value = RandomUtils.nextInt(0, Integer.MAX_VALUE);
      assertEquals(value, codec.decode(codec.encode(value)));
      value = 0 - value;
      assertEquals(value, codec.decode(codec.encode(value)));
    }
  }

  @Test
  public void testLong() throws IOException {
    ICodec<Long> codec = LongCodec.INSTANCE;
    assertNull(codec.decode(codec.encode(null)));
    for (int i = 0; i < 1000; i++) {
      Long value = RandomUtils.nextLong(0, Long.MAX_VALUE);
      assertEquals(value, LongCodec.INSTANCE.decode(LongCodec.INSTANCE.encode(value)));
      value = 0 - value;
      assertEquals(value, LongCodec.INSTANCE.decode(LongCodec.INSTANCE.encode(value)));
    }
  }

  @Test
  public void testFloat() throws IOException {
    ICodec<Float> codec = FloatCodec.INSTANCE;
    assertNull(codec.decode(codec.encode(null)));
    for (int i = 0; i < 1000; i++) {
      Float value = RandomUtils.nextFloat(0, Float.MAX_VALUE);
      assertEquals(value, FloatCodec.INSTANCE.decode(FloatCodec.INSTANCE.encode(value)));
      value = 0 - value;
      assertEquals(value, FloatCodec.INSTANCE.decode(FloatCodec.INSTANCE.encode(value)));
    }
  }

  @Test
  public void testDouble() throws IOException {
    ICodec<Double> codec = DoubleCodec.INSTANCE;
    assertNull(codec.decode(codec.encode(null)));
    for (int i = 0; i < 1000; i++) {
      Double value = RandomUtils.nextDouble(0, Double.MAX_VALUE);
      assertEquals(value, DoubleCodec.INSTANCE.decode(DoubleCodec.INSTANCE.encode(value)));
      value = 0 - value;
      assertEquals(value, DoubleCodec.INSTANCE.decode(DoubleCodec.INSTANCE.encode(value)));
    }
  }

  @Test
  public void testString() throws IOException {
    ICodec<String> codec = StringCodec.INSTANCE;
    assertNull(codec.decode(codec.encode(null)));
    for (int i = 0; i < 1000; i++) {
      String value = RandomStringUtils.random(100);
      assertEquals(value, StringCodec.INSTANCE.decode(StringCodec.INSTANCE.encode(value)));
    }
  }

  @Test
  public void testEnum() throws IOException {
    ProtoEnumCodec<TestModel.EnumA> codec = Codecs.getProtoEnumCodec(TestModel.EnumA.class);
    assertNull(codec.decode(codec.encode(null)));
    for (TestModel.EnumA value : TestModel.EnumA.values()) {
      assertEquals(value, codec.decode(codec.encode(value)));
    }
  }

  @Test
  public void testMessage() throws IOException {
    ProtoMessageCodec<TestModel.MsgA> codec = Codecs.getProtoMessageCodec(TestModel.MsgA.class);

    assertNull(codec.decode(codec.encode(null)));

    assertEquals(MsgsForTest.allSetMsgA, codec.decode(codec.encode(MsgsForTest.allSetMsgA)));

    assertEquals(TestModel.MsgA.getDefaultInstance(),
        codec.decode(codec.encode(TestModel.MsgA.getDefaultInstance())));
  }
}
