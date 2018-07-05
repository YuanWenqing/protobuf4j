package org.protoframework.core.codec;

import com.google.protobuf.ByteString;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;
import org.protoframework.core.proto.data.TestModel;

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

  @Test
  public void testEnum() {
    ProtoEnumCodec<TestModel.EnumA> codec = Codecs.getProtoEnumCodec(TestModel.EnumA.class);
    for (TestModel.EnumA value : TestModel.EnumA.values()) {
      assertEquals(value, codec.decode(codec.encode(value)));
    }
  }

  @Test
  public void testMessage() {
    ProtoMessageCodec<TestModel.MsgA> codec = Codecs.getProtoMessageCodec(TestModel.MsgA.class);
    TestModel.MsgA allSetMsgA =
        TestModel.MsgA.newBuilder().setInt32(1).setInt64(1).setFloat(1).setDouble(1).setBool(true)
            .setString("1").setBytes(ByteString.copyFromUtf8("a")).setEnuma(TestModel.EnumA.EA1)
            .setMsgb(TestModel.MsgB.newBuilder().setId("").build()).addInt32Arr(1).addInt64Arr(1)
            .addFloatArr(1).addDoubleArr(1).addBoolArr(false).addStringArr("a")
            .addBytesArr(ByteString.EMPTY).addEnumaArr(TestModel.EnumA.EA0)
            .addMsgbArr(TestModel.MsgB.getDefaultInstance()).putInt32Map("", 1).putInt64Map("", 1)
            .putFloatMap("", 1).putDoubleMap("", 1).putBoolMap(1, false).putStringMap("", "a")
            .putBytesMap("", ByteString.EMPTY).putEnumaMap("", TestModel.EnumA.EA0)
            .putMsgbMap("", TestModel.MsgB.getDefaultInstance()).build();
    assertEquals(allSetMsgA, codec.decode(codec.encode(allSetMsgA)));

    assertEquals(TestModel.MsgA.getDefaultInstance(),
        codec.decode(codec.encode(TestModel.MsgA.getDefaultInstance())));
  }
}
