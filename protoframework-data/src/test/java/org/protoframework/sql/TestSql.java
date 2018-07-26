package org.protoframework.sql;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author: yuanwq
 * @date: 2018/7/17
 */
public class TestSql {
  @Test
  public void testSelect() {
    SelectSql sql;

    sql = QueryCreator.selectFrom("t ");
    System.out.println(sql);
    assertNotNull(sql.getFrom());
    assertNull(sql.getSelect());
    assertNull(sql.getWhere());
    assertEquals("FROM t", sql.toSolidSql(new StringBuilder()).toString());
    assertTrue(sql.collectSqlValue(Lists.newArrayList()).isEmpty());

    sql.select();
    System.out.println(sql);
    assertNotNull(sql.getSelect());
    assertEquals("SELECT FROM t", sql.toSolidSql(new StringBuilder()).toString());
    sql.select().star();
    System.out.println(sql);
    assertEquals("SELECT * FROM t", sql.toSqlTemplate(new StringBuilder()).toString());
    assertEquals("SELECT * FROM t", sql.toSolidSql(new StringBuilder()).toString());

    sql.where();
    System.out.println(sql);
    assertEquals("SELECT * FROM t ", sql.toSqlTemplate(new StringBuilder()).toString());
    assertEquals("SELECT * FROM t ", sql.toSolidSql(new StringBuilder()).toString());
    sql.where().setCond(FieldValues.eq("a", 1));
    System.out.println(sql);
    assertEquals("SELECT * FROM t WHERE a=?", sql.toSqlTemplate(new StringBuilder()).toString());
    assertEquals("SELECT * FROM t WHERE a=1", sql.toSolidSql(new StringBuilder()).toString());

    List<ISqlValue> sqlValues = sql.collectSqlValue(Lists.newArrayList());
    assertEquals(1, sqlValues.size());

    sql.setWhere(null);
    System.out.println(sql);
    sql.setSelect(null);
    System.out.println(sql);
    assertNull(sql.getSelect());
    assertNull(sql.getWhere());
    assertEquals("FROM t", sql.toSolidSql(new StringBuilder()).toString());

    sql = new SelectSql(null, QueryCreator.from("t"), null);
    assertNull(sql.getSelect());
    assertNull(sql.getWhere());
  }

  @Test
  public void testUpdate() {
    UpdateSql sql;

    sql = QueryCreator.updateSql("t");
    System.out.println(sql);
    assertNotNull(sql.getTable());
    assertNull(sql.getSet());
    assertNull(sql.getWhere());
    assertEquals("UPDATE t", sql.toSolidSql(new StringBuilder()).toString());
    assertTrue(sql.collectSqlValue(Lists.newArrayList()).isEmpty());

    sql.set();
    System.out.println(sql);
    assertNotNull(sql.getSet());
    assertEquals("UPDATE t", sql.toSolidSql(new StringBuilder()).toString());
    sql.set().setValue("a", "b");
    System.out.println(sql);
    assertEquals("UPDATE t SET a=?", sql.toSqlTemplate(new StringBuilder()).toString());
    assertEquals("UPDATE t SET a='b'", sql.toSolidSql(new StringBuilder()).toString());

    sql.where();
    System.out.println(sql);
    assertEquals("UPDATE t SET a=? ", sql.toSqlTemplate(new StringBuilder()).toString());
    assertEquals("UPDATE t SET a='b' ", sql.toSolidSql(new StringBuilder()).toString());
    sql.where().setCond(FieldValues.eq("a", 1));
    System.out.println(sql);
    assertEquals("UPDATE t SET a=? WHERE a=?", sql.toSqlTemplate(new StringBuilder()).toString());
    assertEquals("UPDATE t SET a='b' WHERE a=1", sql.toSolidSql(new StringBuilder()).toString());

    List<ISqlValue> sqlValues = sql.collectSqlValue(Lists.newArrayList());
    assertEquals(2, sqlValues.size());

    sql.setWhere(null);
    System.out.println(sql);
    sql.setSet(null);
    System.out.println(sql);
    assertNull(sql.getSet());
    assertNull(sql.getWhere());
    assertEquals("UPDATE t", sql.toSolidSql(new StringBuilder()).toString());

    sql = new UpdateSql(QueryCreator.table("t"), null, null);
    assertNull(sql.getSet());
    assertNull(sql.getWhere());
  }

  @Test
  public void testDelete() {
    DeleteSql sql;

    sql = QueryCreator.deleteFrom("t");
    System.out.println(sql);
    assertNotNull(sql.getFrom());
    assertNull(sql.getWhere());
    assertEquals("DELETE FROM t", sql.toSqlTemplate(new StringBuilder()).toString());
    assertEquals("DELETE FROM t", sql.toSolidSql(new StringBuilder()).toString());
    assertTrue(sql.collectSqlValue(Lists.newArrayList()).isEmpty());

    sql.where();
    System.out.println(sql);
    assertNotNull(sql.getWhere());
    assertEquals("DELETE FROM t ", sql.toSqlTemplate(new StringBuilder()).toString());
    assertEquals("DELETE FROM t ", sql.toSolidSql(new StringBuilder()).toString());
    sql.where().setCond(FieldValues.eq("a", 1));
    System.out.println(sql);
    assertEquals("DELETE FROM t WHERE a=?", sql.toSqlTemplate(new StringBuilder()).toString());
    assertEquals("DELETE FROM t WHERE a=1", sql.toSolidSql(new StringBuilder()).toString());

    List<ISqlValue> sqlValues = sql.collectSqlValue(Lists.newArrayList());
    assertEquals(1, sqlValues.size());

    sql.setWhere(null);
    System.out.println(sql);
    assertNull(sql.getWhere());
    assertEquals("DELETE FROM t", sql.toSolidSql(new StringBuilder()).toString());

    sql = new DeleteSql(QueryCreator.from("a"), QueryCreator.where());
    assertNotNull(sql.getFrom());
    assertNotNull(sql.getWhere());
  }

  @Test
  public void testRaw() {
    RawSql sql;

    sql = new RawSql("a");
    System.out.println(sql);
    assertEquals("a", sql.getSql());
    assertTrue(sql.getValues().isEmpty());
    assertEquals("a", sql.toSqlTemplate(new StringBuilder()).toString());
    assertEquals("a", sql.toSolidSql(new StringBuilder()).toString());
    assertTrue(sql.collectSqlValue(Lists.newArrayList()).isEmpty());

    sql = new RawSql("a+b");
    System.out.println(sql);
    assertEquals("a+b", sql.getSql());
    assertTrue(sql.getValues().isEmpty());
    assertEquals("a+b", sql.toSqlTemplate(new StringBuilder()).toString());
    assertEquals("a+b", sql.toSolidSql(new StringBuilder()).toString());
    assertTrue(sql.collectSqlValue(Lists.newArrayList()).isEmpty());

    sql = new RawSql("?", Lists.newArrayList(1));
    System.out.println(sql);
    assertEquals("?", sql.getSql());
    assertEquals("?", sql.toSqlTemplate(new StringBuilder()).toString());
    assertEquals("1", sql.toSolidSql(new StringBuilder()).toString());

    sql = new RawSql("(? AND ?)", Lists.newArrayList(1));
    System.out.println(sql);
    assertEquals("(? AND ?)", sql.getSql());
    assertEquals("(? AND ?)", sql.toSqlTemplate(new StringBuilder()).toString());
    assertEquals("(1 AND ?)", sql.toSolidSql(new StringBuilder()).toString());

    sql = new RawSql("a+?", Lists.newArrayList(1));
    System.out.println(sql);
    assertEquals("a+?", sql.getSql());
    assertEquals("a+?", sql.toSqlTemplate(new StringBuilder()).toString());
    assertEquals("a+1", sql.toSolidSql(new StringBuilder()).toString());

    sql = new RawSql("?+a", Lists.newArrayList(1));
    System.out.println(sql);
    assertEquals("?+a", sql.getSql());
    assertEquals("?+a", sql.toSqlTemplate(new StringBuilder()).toString());
    assertEquals("1+a", sql.toSolidSql(new StringBuilder()).toString());

    sql = new RawSql("? AND a", Lists.newArrayList(1));
    System.out.println(sql);
    assertEquals("? AND a", sql.getSql());
    assertEquals("? AND a", sql.toSqlTemplate(new StringBuilder()).toString());
    assertEquals("1 AND a", sql.toSolidSql(new StringBuilder()).toString());
    List<ISqlValue> sqlValues = sql.collectSqlValue(Lists.newArrayList());
    assertEquals(1, sqlValues.size());
  }

  @Test
  public void testInsert() {
    InsertSql sql;

    sql = QueryCreator.insertInto("aa");
    System.out.println(sql);
    assertEquals("aa", sql.getTable().getTableName());
    assertEquals("INSERT INTO aa () VALUES ()", sql.toSqlTemplate(new StringBuilder()).toString());
    assertEquals("INSERT INTO aa () VALUES ()", sql.toSolidSql(new StringBuilder()).toString());
    assertTrue(sql.collectSqlValue(Lists.newArrayList()).isEmpty());

    sql.addField("b", 1);
    System.out.println(sql);
    assertEquals("INSERT INTO aa (b) VALUES (?)",
        sql.toSqlTemplate(new StringBuilder()).toString());
    assertEquals("INSERT INTO aa (b) VALUES (1)", sql.toSolidSql(new StringBuilder()).toString());

    sql.addField("c", FieldValues.add("a", 2));
    System.out.println(sql);
    assertEquals("INSERT INTO aa (b,c) VALUES (?,a+?)",
        sql.toSqlTemplate(new StringBuilder()).toString());
    assertEquals("INSERT INTO aa (b,c) VALUES (1,a+2)",
        sql.toSolidSql(new StringBuilder()).toString());
    List<ISqlValue> sqlValues = sql.collectSqlValue(Lists.newArrayList());
    assertEquals(2, sqlValues.size());
    assertEquals(1, sqlValues.get(0).getValue());
    assertEquals(2, sqlValues.get(1).getValue());
  }
}
