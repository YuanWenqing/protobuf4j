package protobuf4j.orm.dao;

import org.junit.Test;
import protobuf4j.orm.sql.SqlUtil;
import protobuf4j.test.proto.TestModel;

import static org.junit.Assert.*;

/**
 * author: yuanwq
 * date: 2018/7/23
 */
public class TestOrmUtils {
  @Test
  public void testTableName() {
    assertEquals("msg_a", SqlUtil.defaultTableName(TestModel.MsgA.class));
  }

}
