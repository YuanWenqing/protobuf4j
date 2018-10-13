package org.protoframework.orm.sql;

import com.google.common.collect.Lists;
import org.junit.Test;
import org.protoframework.orm.sql.clause.*;
import org.protoframework.orm.sql.expr.ArithmeticExpr;
import org.protoframework.orm.sql.expr.Column;
import org.protoframework.orm.sql.expr.Value;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author: yuanwq
 * @date: 2018/7/17
 */
public class TestClause {
  @Test
  public void testSelect() {
    SelectClause clause;

    clause = QueryCreator.select();
    System.out.println(clause);
    assertTrue(clause.isEmpty());
    assertEquals(0, clause.getSelectItems().size());
    try {
      clause.toSqlTemplate(new StringBuilder());
      fail();
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }
    assertEquals("SELECT ", clause.toSolidSql(new StringBuilder()).toString());

    clause.select(Column.of("a"));
    System.out.println(clause);
    assertEquals("SELECT a", clause.toSqlTemplate(new StringBuilder()).toString());
    assertEquals("SELECT a", clause.toSolidSql(new StringBuilder()).toString());
    assertFalse(clause.isEmpty());

    clause.select("b");
    System.out.println(clause);
    assertTrue(clause.getSelectItems().get(1).getExpression() instanceof Column);
    assertNull(clause.getSelectItems().get(1).getAlias());
    assertEquals("SELECT a,b", clause.toSqlTemplate(new StringBuilder()).toString());
    assertEquals("SELECT a,b", clause.toSolidSql(new StringBuilder()).toString());
    assertFalse(clause.isEmpty());

    clause.select(new SelectItem(FieldValues.add("a", 1), "b"));
    System.out.println(clause);
    assertEquals("b", clause.getSelectItems().get(2).getAlias());
    assertEquals("SELECT a,b,a+? AS b", clause.toSqlTemplate(new StringBuilder()).toString());
    assertEquals("SELECT a,b,a+1 AS b", clause.toSolidSql(new StringBuilder()).toString());
    List<ISqlValue> sqlValues = clause.collectSqlValue(Lists.newArrayList());
    assertEquals(1, sqlValues.size());
    assertEquals(1, sqlValues.get(0).getValue());
  }

  @Test
  public void testFrom() {
    FromClause clause = QueryCreator.from("t");

    assertEquals("t", clause.getTableRef().getTableName());
    assertEquals("FROM t", clause.toSqlTemplate(new StringBuilder()).toString());
    assertEquals("FROM t", clause.toSolidSql(new StringBuilder()).toString());
    assertTrue(clause.collectSqlValue(Lists.newArrayList()).isEmpty());
  }

  @Test
  public void testWhere() {
    WhereClause clause;

    clause = QueryCreator.where();
    System.out.println(clause);
    assertNull(clause.getCond());
    assertNull(clause.getOrderBy());
    assertNull(clause.getGroupBy());
    assertNull(clause.getPagination());
    assertEquals("", clause.toSqlTemplate(new StringBuilder()).toString());
    assertEquals("", clause.toSolidSql(new StringBuilder()).toString());
    assertTrue(clause.collectSqlValue(Lists.newArrayList()).isEmpty());

    clause.setCond(FieldValues.eq("a", 1));
    System.out.println(clause);
    assertNotNull(clause.getCond());
    assertEquals("WHERE a=?", clause.toSqlTemplate(new StringBuilder()).toString());
    assertEquals("WHERE a=1", clause.toSolidSql(new StringBuilder()).toString());

    clause.orderBy();
    System.out.println(clause);
    assertNotNull(clause.getOrderBy());
    assertEquals("WHERE a=?", clause.toSqlTemplate(new StringBuilder()).toString());
    assertEquals("WHERE a=1", clause.toSolidSql(new StringBuilder()).toString());

    clause.orderBy().asc("b");
    System.out.println(clause);
    assertEquals("WHERE a=? ORDER BY b ASC", clause.toSqlTemplate(new StringBuilder()).toString());
    assertEquals("WHERE a=1 ORDER BY b ASC", clause.toSolidSql(new StringBuilder()).toString());

    clause.groupBy();
    System.out.println(clause);
    assertNotNull(clause.getGroupBy());
    assertEquals("WHERE a=? ORDER BY b ASC", clause.toSqlTemplate(new StringBuilder()).toString());
    assertEquals("WHERE a=1 ORDER BY b ASC", clause.toSolidSql(new StringBuilder()).toString());

    clause.groupBy().by("c");
    System.out.println(clause);
    assertEquals("WHERE a=? ORDER BY b ASC GROUP BY c",
        clause.toSqlTemplate(new StringBuilder()).toString());
    assertEquals("WHERE a=1 ORDER BY b ASC GROUP BY c",
        clause.toSolidSql(new StringBuilder()).toString());

    clause.limit(10);
    System.out.println(clause);
    assertEquals("WHERE a=? ORDER BY b ASC GROUP BY c LIMIT 10",
        clause.toSqlTemplate(new StringBuilder()).toString());
    assertEquals("WHERE a=1 ORDER BY b ASC GROUP BY c LIMIT 10",
        clause.toSolidSql(new StringBuilder()).toString());

    List<ISqlValue> sqlValues = clause.collectSqlValue(Lists.newArrayList());
    assertEquals(1, sqlValues.size());
    assertEquals(1, sqlValues.get(0).getValue());

    clause.setCond(null);
    System.out.println(clause);
    assertEquals("ORDER BY b ASC GROUP BY c LIMIT 10",
        clause.toSqlTemplate(new StringBuilder()).toString());
    assertEquals("ORDER BY b ASC GROUP BY c LIMIT 10",
        clause.toSolidSql(new StringBuilder()).toString());

    clause.setGroupBy(null);
    System.out.println(clause);
    assertEquals("ORDER BY b ASC LIMIT 10", clause.toSqlTemplate(new StringBuilder()).toString());
    assertEquals("ORDER BY b ASC LIMIT 10", clause.toSolidSql(new StringBuilder()).toString());

    clause.setOrderBy(null);
    System.out.println(clause);
    assertEquals("LIMIT 10", clause.toSqlTemplate(new StringBuilder()).toString());
    assertEquals("LIMIT 10", clause.toSolidSql(new StringBuilder()).toString());

    clause.setPagination(null);
    clause.groupBy().by("c");
    System.out.println(clause);
    assertEquals("GROUP BY c", clause.toSqlTemplate(new StringBuilder()).toString());
    assertEquals("GROUP BY c", clause.toSolidSql(new StringBuilder()).toString());

    clause = new WhereClause();
    clause.orderBy();
    clause.groupBy();
    assertTrue(clause.collectSqlValue(Lists.newArrayList()).isEmpty());
  }

  @Test
  public void testGroupBy() {
    GroupByClause clause;

    clause = QueryCreator.groupBy();
    System.out.println(clause);
    assertTrue(clause.isEmpty());
    assertEquals(0, clause.getGroupByItems().size());
    assertEquals("", clause.toSqlTemplate(new StringBuilder()).toString());
    assertEquals("", clause.toSolidSql(new StringBuilder()).toString());

    clause.asc("a");
    System.out.println(clause);
    assertEquals("GROUP BY a ASC", clause.toSqlTemplate(new StringBuilder()).toString());
    assertEquals("GROUP BY a ASC", clause.toSolidSql(new StringBuilder()).toString());
    assertFalse(clause.isEmpty());

    clause.desc("b");
    System.out.println(clause);
    assertTrue(clause.getGroupByItems().get(1).getExpression() instanceof Column);
    assertEquals(Direction.DESC, clause.getGroupByItems().get(1).getDirection());
    assertEquals("GROUP BY a ASC,b DESC", clause.toSqlTemplate(new StringBuilder()).toString());
    assertEquals("GROUP BY a ASC,b DESC", clause.toSolidSql(new StringBuilder()).toString());
    assertFalse(clause.isEmpty());

    clause.by("c");
    System.out.println(clause);
    assertEquals("GROUP BY a ASC,b DESC,c", clause.toSqlTemplate(new StringBuilder()).toString());
    assertEquals("GROUP BY a ASC,b DESC,c", clause.toSolidSql(new StringBuilder()).toString());

    clause.clear();
    System.out.println(clause);
    assertTrue(clause.isEmpty());
    assertEquals(0, clause.getGroupByItems().size());
    assertEquals("", clause.toSqlTemplate(new StringBuilder()).toString());
    assertEquals("", clause.toSolidSql(new StringBuilder()).toString());
  }

  @Test
  public void testOrderBy() {
    OrderByClause clause;

    clause = QueryCreator.orderBy();
    System.out.println(clause);
    assertTrue(clause.isEmpty());
    assertEquals(0, clause.getOrderByItems().size());
    assertEquals("", clause.toSqlTemplate(new StringBuilder()).toString());
    assertEquals("", clause.toSolidSql(new StringBuilder()).toString());

    clause.asc("a");
    System.out.println(clause);
    assertEquals("ORDER BY a ASC", clause.toSqlTemplate(new StringBuilder()).toString());
    assertEquals("ORDER BY a ASC", clause.toSolidSql(new StringBuilder()).toString());
    assertFalse(clause.isEmpty());

    clause.desc("b");
    System.out.println(clause);
    assertTrue(clause.getOrderByItems().get(1).getExpression() instanceof Column);
    assertEquals(Direction.DESC, clause.getOrderByItems().get(1).getDirection());
    assertEquals("ORDER BY a ASC,b DESC", clause.toSqlTemplate(new StringBuilder()).toString());
    assertEquals("ORDER BY a ASC,b DESC", clause.toSolidSql(new StringBuilder()).toString());
    assertFalse(clause.isEmpty());

    clause.by(new OrderByItem(FieldValues.add("a", 1)));
    System.out.println(clause);
    assertEquals("ORDER BY a ASC,b DESC,a+?",
        clause.toSqlTemplate(new StringBuilder()).toString());
    assertEquals("ORDER BY a ASC,b DESC,a+1",
        clause.toSolidSql(new StringBuilder()).toString());
    List<ISqlValue> sqlValues = clause.collectSqlValue(Lists.newArrayList());
    assertEquals(1, sqlValues.size());
    assertEquals(1, sqlValues.get(0).getValue());

    clause.clear();
    System.out.println(clause);
    assertTrue(clause.isEmpty());
    assertEquals(0, clause.getOrderByItems().size());
    assertEquals("", clause.toSqlTemplate(new StringBuilder()).toString());
    assertEquals("", clause.toSolidSql(new StringBuilder()).toString());
  }

  @Test
  public void testPagination() {
    try {
      QueryCreator.pagination(-1).buildByOffset(10);
      fail();
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }
    try {
      QueryCreator.pagination(-1).buildByPageNo(10);
      fail();
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }
    try {
      QueryCreator.pagination(-1).build();
      fail();
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }
    try {
      QueryCreator.pagination(10).buildByOffset(-1);
      fail();
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }
    try {
      QueryCreator.pagination(10).buildByPageNo(0);
      fail();
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }

    PaginationClause clause;

    clause = QueryCreator.pagination(10).build();
    assertEquals(10, clause.getLimit());
    assertEquals(0, clause.getOffset());
    assertEquals("LIMIT 10", clause.toSqlTemplate(new StringBuilder()).toString());
    assertEquals("LIMIT 10", clause.toSolidSql(new StringBuilder()).toString());
    clause = clause.next();
    assertEquals(10, clause.getLimit());
    assertEquals(10, clause.getOffset());
    assertEquals("LIMIT 10 OFFSET 10", clause.toSqlTemplate(new StringBuilder()).toString());
    assertEquals("LIMIT 10 OFFSET 10", clause.toSolidSql(new StringBuilder()).toString());

    clause = QueryCreator.pagination(20).buildByPageNo(10);
    assertEquals(20, clause.getLimit());
    assertEquals(180, clause.getOffset());
    assertEquals("LIMIT 20 OFFSET 180", clause.toSqlTemplate(new StringBuilder()).toString());
    assertEquals("LIMIT 20 OFFSET 180", clause.toSolidSql(new StringBuilder()).toString());
    clause = clause.next();
    assertEquals(20, clause.getLimit());
    assertEquals(200, clause.getOffset());
    assertEquals("LIMIT 20 OFFSET 200", clause.toSqlTemplate(new StringBuilder()).toString());
    assertEquals("LIMIT 20 OFFSET 200", clause.toSolidSql(new StringBuilder()).toString());

    clause = QueryCreator.pagination(10).buildByOffset(10);
    assertEquals(10, clause.getLimit());
    assertEquals(10, clause.getOffset());
    assertEquals("LIMIT 10 OFFSET 10", clause.toSqlTemplate(new StringBuilder()).toString());
    assertEquals("LIMIT 10 OFFSET 10", clause.toSolidSql(new StringBuilder()).toString());
    clause = clause.next();
    assertEquals(10, clause.getLimit());
    assertEquals(20, clause.getOffset());
    assertEquals("LIMIT 10 OFFSET 20", clause.toSqlTemplate(new StringBuilder()).toString());
    assertEquals("LIMIT 10 OFFSET 20", clause.toSolidSql(new StringBuilder()).toString());

    PaginationClause.Builder builder = QueryCreator.pagination(10);
    clause = builder.build();
    assertEquals(0, clause.totalPages(0));
    assertEquals(10, clause.totalPages(100));
    assertEquals(10, clause.totalPages(91));
    assertEquals(10, clause.totalPages(99));

    clause = QueryCreator.pagination(0).build();
    assertEquals(0, clause.totalPages(1));
  }

  @Test
  public void testSet() {
    SetClause clause;

    clause = QueryCreator.set();
    System.out.println(clause);
    assertTrue(clause.isEmpty());
    assertEquals(0, clause.getSetItems().size());
    try {
      clause.toSqlTemplate(new StringBuilder());
      fail();
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }
    assertEquals("SET ", clause.toSolidSql(new StringBuilder()).toString());

    clause.setValue("a", 1);
    System.out.println(clause);
    assertEquals("a", clause.getSetItems().get(0).getColumn().getColumn());
    assertTrue(clause.getSetItems().get(0).getValue() instanceof Value);
    assertEquals("SET a=?", clause.toSqlTemplate(new StringBuilder()).toString());
    assertEquals("SET a=1", clause.toSolidSql(new StringBuilder()).toString());
    assertFalse(clause.isEmpty());

    clause.setExpression("b", FieldValues.add("c", 2));
    System.out.println(clause);
    assertTrue(clause.getSetItems().get(1).getValue() instanceof ArithmeticExpr);
    assertEquals("SET a=?,b=c+?", clause.toSqlTemplate(new StringBuilder()).toString());
    assertEquals("SET a=1,b=c+2", clause.toSolidSql(new StringBuilder()).toString());
    assertFalse(clause.isEmpty());

    clause.setColumn("d", "e");
    System.out.println(clause);
    assertTrue(clause.getSetItems().get(2).getValue() instanceof Column);
    assertEquals("SET a=?,b=c+?,d=e", clause.toSqlTemplate(new StringBuilder()).toString());
    assertEquals("SET a=1,b=c+2,d=e", clause.toSolidSql(new StringBuilder()).toString());
    List<ISqlValue> sqlValues = clause.collectSqlValue(Lists.newArrayList());
    assertEquals(2, sqlValues.size());
    assertEquals(1, sqlValues.get(0).getValue());
    assertEquals(2, sqlValues.get(1).getValue());
  }

  @Test
  public void testQueryCreator() {
    Column column = QueryCreator.column("a");
    assertEquals("a", column.getColumn());

    ISqlValue value = QueryCreator.sqlValue("s");
    assertEquals("s", value.getValue());

    value = QueryCreator.sqlValue(value);
    assertEquals("s", value.getValue());
  }
}
