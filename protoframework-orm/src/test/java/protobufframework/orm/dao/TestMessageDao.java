package protobufframework.orm.dao;

import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import protobufframework.core.ProtoMessageHelper;
import org.protoframework.core.proto.data.TestModel;
import protobufframework.orm.sql.FieldAndValue;
import protobufframework.orm.sql.IExpression;
import protobufframework.orm.sql.RawSql;
import protobufframework.orm.sql.clause.WhereClause;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

  private PrimaryKeyProtoMessageDao<Long, TestModel.DbMsg> dao;
  private final String primaryKey = "id";
  private TestModel.DbMsg msgTemplate;

  @Before
  public void setup() {
    dao = new PrimaryKeyProtoMessageDao<>(TestModel.DbMsg.class, primaryKey);
    dao.setJdbcTemplate(jdbcTemplate);
    dao.afterPropertiesSet();

    msgTemplate =
        TestModel.DbMsg.newBuilder().setInt32V(32).setInt64V(64).setFloatV(1.23f).setDoubleV(2.345)
            .setBoolV(true).setStringV("string").setEnumaV(TestModel.EnumA.EA2).addInt32Arr(1)
            .addInt32Arr(2).addInt64Arr(100).addInt64Arr(200).addFloatArr(0.123f)
            .addDoubleArr(0.987).addBoolArr(false).addBoolArr(true).addStringArr("%")
            .addStringArr(",").addEnumaArr(TestModel.EnumA.EA0).addEnumaArr(TestModel.EnumA.EA2)
            .addEnumaArr(TestModel.EnumA.EA4).putInt32Map("a", 2).putInt64Map("%", 100)
            .putFloatMap(";", 0333f).putDoubleMap(",", 0.222).putBoolMap(1, true)
            .putBoolMap(0, false).putStringMap("%", "%").putStringMap(";", ";%")
            .putStringMap("%;", "%;").putEnumaMap("a", TestModel.EnumA.EA4).
            setCreateTime(System.currentTimeMillis()).build();
  }

  private int[] prepare(String strValue, int num) {
    List<TestModel.DbMsg> msgs = Lists.newArrayListWithExpectedSize(num);
    for (int i = 0; i < num; i++) {
      TestModel.DbMsg msg = TestModel.DbMsg.newBuilder().setStringV(strValue).setInt32V(i).build();
      msgs.add(msg);
    }
    return dao.insertMulti(msgs);
  }

  @Test
  public void testDao() {
    assertEquals(primaryKey, dao.getPrimaryKey());
    assertEquals(TestModel.DbMsg.class, dao.getMessageType());
    assertEquals("db_msg", dao.getTableName());
    assertTrue(dao.getMessageMapper() instanceof ProtoMessageRowMapper);
    Assert.assertEquals(TestModel.DbMsg.class, dao.getMessageHelper().getType());
    assertEquals(jdbcTemplate, dao.getJdbcTemplate());
  }

  @Test
  public void testSelect() {
    TestModel.DbMsg msg = dao.selectOneByPrimaryKey(1L);
    assertNotNull(msg);
    System.out.println(ProtoMessageHelper.printToString(msg));
    assertEquals(1L, msg.getId());
    assertEquals(10, msg.getInt32V());
    assertEquals(100L, msg.getInt64V());
    assertTrue(0 < msg.getCreateTime() && msg.getCreateTime() <= System.currentTimeMillis());

    assertNull(dao.selectOneByPrimaryKey(-1L));
    assertNotNull(dao.selectOne(new WhereClause()));
  }

  @Test
  public void testUpdate() {
    TestModel.DbMsg oldItem = dao.selectOneByPrimaryKey(1L);
    assertNotNull(oldItem);
    TestModel.DbMsg newItem =
        oldItem.toBuilder().addInt32Arr(1).setCreateTime(oldItem.getCreateTime() + 100).build();
    int rows = dao.updateMessageByPrimaryKey(newItem, oldItem);
    assertEquals(1, rows);
    TestModel.DbMsg msg = dao.selectOneByPrimaryKey(1L);
    assertNotNull(msg);
    assertEquals(oldItem.getInt32ArrCount() + 1, msg.getInt32ArrCount());
    assertEquals(1, msg.getInt32Arr(oldItem.getInt32ArrCount()));
    assertEquals(oldItem.getCreateTime() + 100, msg.getCreateTime());

    assertEquals(0, dao.updateMessageByPrimaryKey(msg, msg));
  }

  @Test
  public void testInsertIgnore() {
    // ignore
    TestModel.DbMsg msg = TestModel.DbMsg.newBuilder().setId(1).build();
    assertEquals(0, dao.insertIgnore(msg));
    int[] row = dao.insertIgnoreMulti(Lists.newArrayList(msg, msg));
    assertEquals(2, row.length);
    assertArrayEquals(new int[]{0, 0}, row);

    // not ignore
    final long now = System.currentTimeMillis();
    msg = TestModel.DbMsg.newBuilder().setInt64V(now).setCreateTime(now)
        .setStringV("testInsertIgnore").build();
    assertEquals(1, dao.insertIgnore(msg));
    row = dao.insertIgnoreMulti(Lists.newArrayList(msg, msg.toBuilder().setId(1).build()));
    // TODO: 这里在h2 db上会插入一个id=0的数据，有问题
    assertEquals(2, row.length);
    assertArrayEquals(new int[]{1, 0}, row);
  }

  @Test
  public void testMulti() {
    // insert multi
    int[] rowArr = prepare("testMulti", 3);
    assertEquals(3, rowArr.length);
    for (int i = 0; i < rowArr.length; i++) {
      assertEquals(1, rowArr[i]);
    }
    // retrieve inserted data
    List<TestModel.DbMsg> msgs = dao.selectCond(FieldAndValue.eq("string_v", "testMulti"));
    assertEquals(3, msgs.size());
    List<Long> ids = Lists.transform(msgs, TestModel.DbMsg::getId);
    // select multi
    Map<Long, TestModel.DbMsg> map = dao.selectMultiByPrimaryKey(ids);
    assertEquals(3, msgs.size());
    // delete multi
    int rows = dao.deleteMultiByPrimaryKey(ids);
    assertEquals(3, rows);
    // assert after delete
    msgs = dao.selectCond(FieldAndValue.eq("string_v", "testMulti"));
    assertEquals(0, msgs.size());
    map = dao.selectMultiByPrimaryKey(ids);
    assertEquals(0, msgs.size());
    rows = dao.delete(FieldAndValue.eq("string_v", "testMulti"));
    assertEquals(0, rows);
  }

  @Test
  public void testInsertAndDelete() {
    int[] rowArr = dao.insertMulti(Collections.emptyList());
    assertEquals(0, rowArr.length);
    int count = dao.selectAll().size();
    long id = dao.insertReturnKey(msgTemplate).longValue();
    TestModel.DbMsg msg = dao.selectOneByPrimaryKey(id);
    assertEquals(msgTemplate, msg.toBuilder().clearId().build());
    assertEquals(count + 1, dao.selectAll().size());
    int rows = dao.deleteByPrimaryKey(id);
    assertEquals(1, rows);
    assertEquals(count, dao.selectAll().size());
    assertEquals(1, dao.insert(TestModel.DbMsg.getDefaultInstance()));
  }

  @Test
  public void testInsertAndFail() {
    assertEquals(1, dao.insert(msgTemplate));
    try {
      dao.insert(TestModel.DbMsg.newBuilder().setId(1).build());
      fail();
    } catch (Exception e) {
      System.out.println(e.getClass().getName() + ": " + e.getMessage());
    }
  }

  @Test
  public void testIterator() {
    prepare("testIterator", 4);

    Iterator<TestModel.DbMsg> iter1 = dao.selectAll().iterator();
    Iterator<TestModel.DbMsg> iter2 = dao.iterator(2);
    while (iter1.hasNext()) {
      assertTrue(iter2.hasNext());
      assertEquals(iter1.next(), iter2.next());
    }
    assertFalse(iter2.hasNext());

    IExpression cond = FieldAndValue.eq("string_v", "testIterator");
    iter1 = dao.selectCond(cond).iterator();
    iter2 = dao.iterator(cond, 2);
    while (iter1.hasNext()) {
      assertTrue(iter2.hasNext());
      assertEquals(iter1.next(), iter2.next());
    }
    assertFalse(iter2.hasNext());

    iter2 = dao.iterator(FieldAndValue.lt("id", 0), 100);
    assertFalse(iter2.hasNext());

    iter2 = dao.iterator(new WhereClause().limit(0));
    assertFalse(iter2.hasNext());
  }

  @Test
  public void testAggregate() {
    int num = 7;
    prepare("testAggregate", num);
    IExpression cond = FieldAndValue.eq("string_v", "testAggregate");
    IExpression expr = FieldAndValue.add("int32_v", 1);

    int expectCount = dao.selectAll().size();
    Assert.assertEquals(expectCount, dao.count(null));

    expectCount = dao.selectCond(cond).size();
    assertEquals(expectCount, dao.count(cond));

    int sum = 0;
    for (int i = 0; i < num; i++) {
      sum += i;
    }
    assertEquals(sum, dao.sum("int32_v", cond));
    assertEquals(sum + num * 1, dao.sum(expr, cond));

    assertEquals(num - 1, (int) dao.max("int32_v", cond));
    assertEquals(num, (int) dao.max(expr, cond, new SingleColumnRowMapper<Integer>()));
    assertNull(dao.max("int32_v", FieldAndValue.eq("string_v", "aaaaaaaaaaaaa")));

    assertEquals(0, (int) dao.min("int32_v", cond));
    assertEquals(1, (int) dao.min(expr, cond, new SingleColumnRowMapper<Integer>()));

    prepare("testAggregate1", 3);
    Map<String, Integer> map = dao.groupCount("string_v");
    System.out.println(map);
    assertEquals(num, map.get("testAggregate").intValue());
    assertEquals(3, map.get("testAggregate1").intValue());
    map = dao.groupCount("string_v", cond);
    System.out.println(map);
    assertEquals(1, map.size());
    assertEquals(num, map.get("testAggregate").intValue());

    // assert no data
    cond = FieldAndValue.eq("string_v", "testAggregate111");
    assertEquals(0, dao.count(cond));
    assertEquals(0, dao.sum("int32_v", cond));
    assertNull(dao.max("int32_v", cond));
    assertNull(dao.min("int32_v", cond));
    map = dao.groupCount("string_v", cond);
    assertTrue(map.isEmpty());
  }

  @Test
  public void testDoSql() {
    String sql = "insert into db_msg (int64_v) values (?)";
    long time = System.currentTimeMillis();
    RawSql rawSql = new RawSql(sql, Lists.newArrayList(time));
    int rows = dao.doRawSql(rawSql);
    assertEquals(1, rows);
    assertTrue(dao.selectCond(FieldAndValue.eq("int64_v", time)).size() > 0);
  }

  @Test
  public void testNotSupport() {
    try {
      dao.max("enuma_v", null);
      fail();
    } catch (UnsupportedOperationException e) {
      System.out.println(e.getMessage());
    }
    try {
      dao.max("int32_arr", null);
      fail();
    } catch (UnsupportedOperationException e) {
      System.out.println(e.getMessage());
    }
  }
}
