package protobuf4j.orm.dao;

import com.google.protobuf.AbstractMessage;
import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Message;
import org.junit.Test;
import protobuf4j.test.proto.TestModel;

import static org.junit.Assert.*;

/**
 * author: yuanwq
 * date: 2018/7/23
 */
public class TestSqlConverterRegistry {
  @Test
  public void testRegistry() {
    SqlConverterRegistry registry = SqlConverterRegistry.getInstance();
    IProtoMessageSqlHandler converter = ProtoSqlHandler.getInstance();

    assertEquals(converter, registry.findSqlConverter(Message.class));
    assertEquals(converter, registry.findSqlConverter(AbstractMessage.class));
    assertEquals(converter, registry.findSqlConverter(GeneratedMessageV3.class));
    assertEquals(converter, registry.findSqlConverter(TestModel.MsgA.class));

    converter = new SqlConverterForTest();
    registry.register(TestModel.MsgA.class, converter);
    assertEquals(converter, registry.findSqlConverter(TestModel.MsgA.class));
    registry.register(AbstractMessage.class, converter);
    assertEquals(converter, registry.findSqlConverter(AbstractMessage.class));
    assertEquals(ProtoSqlHandler.getInstance(),
        registry.findSqlConverter(GeneratedMessageV3.class));
  }

  private static class SqlConverterForTest extends ProtoSqlHandler {
  }

}
