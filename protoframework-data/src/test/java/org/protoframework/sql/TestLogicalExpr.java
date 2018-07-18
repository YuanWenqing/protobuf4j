package org.protoframework.sql;

import com.google.common.collect.Lists;
import org.junit.Test;
import org.protoframework.sql.expr.Column;
import org.protoframework.sql.expr.LogicalExpr;
import org.protoframework.sql.expr.LogicalOp;
import org.protoframework.sql.expr.Value;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author: yuanwq
 * @date: 2018/7/17
 */
public class TestLogicalExpr {

  @Test
  public void testFieldField() {
    LogicalExpr expr;
    List<ISqlValue> sqlValues;

    {
      // and
      expr = FieldFields.and("a", "b");
      System.out.println(expr);
      assertTrue(expr.getLeft() instanceof Column);
      assertTrue(expr.getRight() instanceof Column);
      assertEquals(LogicalOp.AND, expr.getOp());
      assertEquals("a AND b", expr.toSqlTemplate(new StringBuilder()).toString());
      assertEquals("a AND b", expr.toSolidSql(new StringBuilder()).toString());
      sqlValues = expr.collectSqlValue(Lists.newArrayList());
      assertEquals(0, sqlValues.size());
    }
    {
      // or
      expr = FieldFields.or("a", "b");
      System.out.println(expr);
      assertEquals(LogicalOp.OR, expr.getOp());
      assertEquals("a OR b", expr.toSqlTemplate(new StringBuilder()).toString());
      assertEquals("a OR b", expr.toSolidSql(new StringBuilder()).toString());
    }
    {
      // xor
      expr = FieldFields.xor("a", "b");
      System.out.println(expr);
      assertEquals(LogicalOp.XOR, expr.getOp());
      assertEquals("a XOR b", expr.toSqlTemplate(new StringBuilder()).toString());
      assertEquals("a XOR b", expr.toSolidSql(new StringBuilder()).toString());
    }
    {
      // not
      expr = FieldFields.not("a");
      System.out.println(expr);
      assertEquals(LogicalOp.NOT, expr.getOp());
      assertEquals("NOT a", expr.toSqlTemplate(new StringBuilder()).toString());
      assertEquals("NOT a", expr.toSolidSql(new StringBuilder()).toString());
    }
  }

  @Test
  public void testEmbedding() {
    IExpression expr = LogicalExpr.and(FieldValues.add("a", 1), FieldFields.add("a", "b"));
    System.out.println(expr);
    assertEquals("(a+?) AND (a+b)", expr.toSqlTemplate(new StringBuilder()).toString());
    assertEquals("(a+1) AND (a+b)", expr.toSolidSql(new StringBuilder()).toString());
    List<ISqlValue> sqlValues = expr.collectSqlValue(Lists.newArrayList());
    assertEquals(1, sqlValues.size());
    assertEquals("a", sqlValues.get(0).getField());
    assertEquals(1, sqlValues.get(0).getValue());

    expr = LogicalExpr.and(FieldFields.and("a", "b"), FieldFields.and("c", "d"));
    System.out.println(expr);
    assertEquals("a AND b AND c AND d", expr.toSqlTemplate(new StringBuilder()).toString());

    expr = LogicalExpr.and(FieldFields.or("a", "b"), FieldFields.and("c", "d"));
    System.out.println(expr);
    assertEquals("(a OR b) AND c AND d", expr.toSqlTemplate(new StringBuilder()).toString());

    expr = LogicalExpr.and(FieldFields.gt("a", "b"), FieldFields.and("c", "d"));
    System.out.println(expr);
    assertEquals("a>b AND c AND d", expr.toSqlTemplate(new StringBuilder()).toString());

    expr = FieldValues.gt("a", 1);
    expr = expr.and(Column.of("b"));
    System.out.println(expr);
    assertEquals("a>? AND b", expr.toSqlTemplate(new StringBuilder()).toString());
    expr = expr.or(Value.of(true));
    System.out.println(expr);
    assertEquals("(a>? AND b) OR ?", expr.toSqlTemplate(new StringBuilder()).toString());
    expr = expr.not();
    System.out.println(expr);
    assertEquals("NOT ((a>? AND b) OR ?)", expr.toSqlTemplate(new StringBuilder()).toString());
    expr = expr.xor(Column.of("c"));
    System.out.println(expr);
    assertEquals("(NOT ((a>? AND b) OR ?)) XOR c",
        expr.toSqlTemplate(new StringBuilder()).toString());
  }
}