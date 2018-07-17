package org.protoframework.sql;

import com.google.common.collect.Lists;
import org.junit.Test;
import org.protoframework.sql.clause.FromClause;
import org.protoframework.sql.clause.PaginationClause;
import org.protoframework.sql.clause.SelectClause;
import org.protoframework.sql.clause.SelectExpr;
import org.protoframework.sql.expr.TableColumn;

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

  }

  @Test
  public void testGroupBy() {

  }

  @Test
  public void testOrderBy() {

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

    clause = QueryCreator.pagination(-1).
        setDefaultLimit(10).build();
    assertEquals(10, clause.getLimit());
    assertEquals(0, clause.getOffset());

    clause = QueryCreator.pagination(20).
        setDefaultLimit(12).
        setDefaultPageNo(10).buildByPageNo(0);
    assertEquals(20, clause.getLimit());
    assertEquals(180, clause.getOffset());

    clause = QueryCreator.pagination(10).
        setDefaultOffset(10).buildByOffset(-1);
    assertEquals(10, clause.getLimit());
    assertEquals(10, clause.getOffset());
  }
}
