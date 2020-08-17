package protobuf4j.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import protobuf4j.test.MsgsForTest;
import protobuf4j.test.proto.TestModel;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * author: yuanwq
 * date: 2018/7/6
 */
public class TestObjectMapper {
  @Test
  public void testMapperDefault() throws IOException {
    ObjectMapper mapper = ProtobufObjectMapper.DEFAULT;
    testJson(mapper);
    testDecode(mapper);
    testEnum(mapper);
  }

  @Test
  public void testMapperCopy() throws IOException {
    ObjectMapper mapper2 = ProtobufObjectMapper.DEFAULT.copy();
    testJson(mapper2);
    testDecode(mapper2);
    testEnum(mapper2);
  }

  private void testJson(ObjectMapper mapper) throws IOException {
    System.out.println(
        mapper.writerWithDefaultPrettyPrinter().writeValueAsString(MsgsForTest.allSetMsgA));
    System.out
        .println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(MsgsForTest.emtpyMsgA));
  }

  private void testDecode(ObjectMapper mapper) throws IOException {
    assertEquals(MsgsForTest.emtpyMsgA,
        mapper.readValue(mapper.writeValueAsString(MsgsForTest.emtpyMsgA), TestModel.MsgA.class));
    assertEquals(MsgsForTest.allSetMsgA,
        mapper.readValue(mapper.writeValueAsString(MsgsForTest.allSetMsgA), TestModel.MsgA.class));

    assertEquals(MsgsForTest.emtpyMsgA,
        mapper.readValue(mapper.writeValueAsBytes(MsgsForTest.emtpyMsgA), TestModel.MsgA.class));
    assertEquals(MsgsForTest.allSetMsgA,
        mapper.readValue(mapper.writeValueAsBytes(MsgsForTest.allSetMsgA), TestModel.MsgA.class));
  }

  private void testEnum(ObjectMapper mapper) throws IOException {
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
