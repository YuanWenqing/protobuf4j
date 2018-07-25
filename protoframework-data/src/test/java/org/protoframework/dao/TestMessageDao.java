package org.protoframework.dao;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.protoframework.core.ProtoMessageHelper;
import org.protoframework.core.proto.data.TestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * @author: yuanwq
 * @date: 2018/7/23
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = JdbcConfiguration.class)
public class TestMessageDao {
  @Autowired
  private JdbcTemplate jdbcTemplate;

  private IPrimaryKeyMessageDao<Long, TestModel.DbMsg> dao;
  private final String primaryKey = "id";

  @Before
  public void setup() {
    dao = new PrimaryProtoMessageDao<>(TestModel.DbMsg.class, primaryKey);
    ((PrimaryProtoMessageDao<Long, TestModel.DbMsg>) dao).setJdbcTemplate(jdbcTemplate);
  }

  @Test
  public void testDaoInfo() {
    assertEquals(primaryKey, dao.getPrimaryKey());
  }

  @Test
  public void testSelect() {
    TestModel.DbMsg msg = dao.selectOneByPrimaryKey(1L);
    System.out.println(ProtoMessageHelper.printToString(msg));
    assertEquals(1L, msg.getId());
    assertEquals(10, msg.getInt32V());
    assertEquals(100L, msg.getInt64V());
    assertTrue(0 < msg.getCreateTime() && msg.getCreateTime() <= System.currentTimeMillis());
  }

}
