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

    sql = QueryCreator.selectFrom("t");
    System.out.println(sql);
    assertNotNull(sql.getFrom());
    assertNull(sql.getSelect());
    assertNull(sql.getWhere());
    assertEquals("FROM t", sql.toSolidSql(new StringBuilder()).toString());

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
  }

  @Test
  public void testDelete() {

  }

  @Test
  public void testRaw() {

  }
}
