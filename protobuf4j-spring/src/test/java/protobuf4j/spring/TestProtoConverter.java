package protobuf4j.spring;

import org.junit.Test;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.TypeDescriptor;
import protobuf4j.core.ProtobufObjectMapper;
import protobuf4j.test.MsgsForTest;
import protobuf4j.test.proto.TestModel;

import java.io.IOException;
import java.lang.reflect.Field;

import static org.junit.Assert.*;

/**
 * author: yuanwq
 * date: 2018/7/9
 */
public class TestProtoConverter {
  private TestModel.MsgA msgA;
  private TestModel.MsgB msgB;
  private Long longValue;
  private TestModel.EnumA enumA;

  private Field getField(String name) {
    try {
      return TestProtoConverter.class.getDeclaredField(name);
    } catch (NoSuchFieldException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void testMessage() throws IOException {
    ProtoMessageConverter converter = new ProtoMessageConverter();
    converter.setObjectMapper(ProtobufObjectMapper.DEFAULT);

    assertTrue(converter.matches(null, new TypeDescriptor(getField("msgA"))));
    assertTrue(converter.matches(null, new TypeDescriptor(getField("msgB"))));
    assertFalse(converter.matches(null, new TypeDescriptor(getField("longValue"))));
    assertFalse(converter.matches(null, new TypeDescriptor(getField("enumA"))));

    assertEquals(TestModel.MsgA.getDefaultInstance(),
        converter.getConverter(TestModel.MsgA.class).convert(null));
    assertEquals(TestModel.MsgA.getDefaultInstance(),
        converter.getConverter(TestModel.MsgA.class).convert(""));

    String text = ProtobufObjectMapper.DEFAULT.writeValueAsString(MsgsForTest.allSetMsgA);
    TestModel.MsgA msgA = converter.getConverter(TestModel.MsgA.class).convert(text);
    assertEquals(MsgsForTest.allSetMsgA, msgA);

    text = ProtobufObjectMapper.DEFAULT.writeValueAsString(TestModel.MsgB.getDefaultInstance());
    TestModel.MsgB msgB = converter.getConverter(TestModel.MsgB.class).convert(text);
    assertEquals(TestModel.MsgB.getDefaultInstance(), msgB);

    try {
      converter.getConverter(TestModel.MsgB.class).convert("a");
      fail();
    } catch (ConversionFailedException e) {
      System.out.println(e.getMessage());
    }
  }

  @Test
  public void testEnum() {
    ProtoEnumConverter converter = new ProtoEnumConverter();

    assertTrue(converter.matches(null, new TypeDescriptor(getField("enumA"))));
    assertFalse(converter.matches(null, new TypeDescriptor(getField("msgB"))));
    assertFalse(converter.matches(null, new TypeDescriptor(getField("longValue"))));

    assertEquals(null, converter.getConverter(TestModel.EnumA.class).convert(null));
    assertEquals(null, converter.getConverter(TestModel.EnumA.class).convert(""));

    for (TestModel.EnumA enumA : TestModel.EnumA.values()) {
      if (enumA == TestModel.EnumA.UNRECOGNIZED) {
        continue;
      }
      assertEquals(enumA,
          converter.getConverter(TestModel.EnumA.class).convert(String.valueOf(enumA.getNumber())));
      assertEquals(enumA, converter.getConverter(TestModel.EnumA.class).convert(enumA.name()));
    }
    try {
      converter.getConverter(TestModel.EnumA.class).convert("10000");
      fail();
    } catch (ConversionFailedException e) {
      System.out.println(e.getMessage());
    }
    try {
      converter.getConverter(TestModel.EnumA.class).convert("a");
      fail();
    } catch (ConversionFailedException e) {
      System.out.println(e.getMessage());
    }
  }
}
