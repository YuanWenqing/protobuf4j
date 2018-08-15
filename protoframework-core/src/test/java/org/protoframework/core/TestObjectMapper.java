package org.protoframework.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.protoframework.core.proto.data.TestModel;
import org.protoframework.test.MsgsForTest;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * @author: yuanwq
 * @date: 2018/7/6
 */
public class TestObjectMapper {
  ObjectMapper mapper = ProtobufObjectMapper.DEFAULT;

  @Test
  public void testJson() throws IOException {
    System.out.println(
        mapper.writerWithDefaultPrettyPrinter().writeValueAsString(MsgsForTest.allSetMsgA));
    System.out
        .println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(MsgsForTest.emtpyMsgA));
  }

  @Test
  public void testDecode() throws IOException {
    assertEquals(MsgsForTest.emtpyMsgA,
        mapper.readValue(mapper.writeValueAsString(MsgsForTest.emtpyMsgA), TestModel.MsgA.class));
    assertEquals(MsgsForTest.allSetMsgA,
        mapper.readValue(mapper.writeValueAsString(MsgsForTest.allSetMsgA), TestModel.MsgA.class));

    assertEquals(MsgsForTest.emtpyMsgA,
        mapper.readValue(mapper.writeValueAsBytes(MsgsForTest.emtpyMsgA), TestModel.MsgA.class));
    assertEquals(MsgsForTest.allSetMsgA,
        mapper.readValue(mapper.writeValueAsBytes(MsgsForTest.allSetMsgA), TestModel.MsgA.class));
  }

  @Test
  public void testEnum() throws IOException {
    for (TestModel.EnumA enumA : TestModel.EnumA.values()) {
      if (enumA == TestModel.EnumA.UNRECOGNIZED) {
        continue;
      }
      String number = String.valueOf(enumA.getNumber());
      assertEquals(number, mapper.writeValueAsString(enumA));
      assertEquals(enumA, mapper.readValue(number, TestModel.EnumA.class));
      assertEquals(enumA,
          mapper.readValue(String.format("\"%s\"", enumA.name()), TestModel.EnumA.class));
    }
  }
}
