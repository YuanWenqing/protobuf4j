package org.protoframework.sql;

import org.junit.Test;
import org.protoframework.sql.clause.SelectClause;
import org.protoframework.sql.clause.SelectExpr;
import org.protoframework.sql.expr.TableColumn;

import static org.junit.Assert.*;

/**
 * @author: yuanwq
 * @date: 2018/7/17
 */
public class TestClause {
  @Test
  public void testSelect() {
    SelectClause clause;

    clause = new SelectClause();
    System.out.println(clause);
    assertTrue(clause.isEmpty());
    assertEquals(0, clause.getSelectExprs().size());
    try {
      clause.toSqlTemplate(new StringBuilder());
      fail();
    } catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }
    assertEquals("SELECT ", clause.toSolidSql(new StringBuilder()).toString());

    clause.select(TableColumn.of("a"));
    System.out.println(clause);
    assertEquals("SELECT a", clause.toSqlTemplate(new StringBuilder()).toString());
    assertEquals("SELECT a", clause.toSolidSql(new StringBuilder()).toString());
    assertFalse(clause.isEmpty());

    clause.select("b");
    System.out.println(clause);
    assertTrue(clause.getSelectExprs().get(1).getExpression() instanceof TableColumn);
    assertNull(clause.getSelectExprs().get(1).getAlias());
    assertEquals("SELECT a,b", clause.toSqlTemplate(new StringBuilder()).toString());
    assertEquals("SELECT a,b", clause.toSolidSql(new StringBuilder()).toString());
    assertFalse(clause.isEmpty());

    clause.select(new SelectExpr(FieldValues.add("a", 1), "b"));
    System.out.println(clause);
    assertEquals("b", clause.getSelectExprs().get(2).getAlias());
    assertEquals("SELECT a,b,a+? AS b", clause.toSqlTemplate(new StringBuilder()).toString());
    assertEquals("SELECT a,b,a+1 AS b", clause.toSolidSql(new StringBuilder()).toString());
  }

  @Test
  public void testFrom() {

  }

  @Test
  public void testWhere() {

  }

  @Test
  public void testGroupBy() {

  }

  @Test
  public void testOrderBy() {

  }

  @Test
  public void testPagination() {

  }
}
