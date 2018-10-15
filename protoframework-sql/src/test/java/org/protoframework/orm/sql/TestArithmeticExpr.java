package org.protoframework.orm.sql;

import com.google.common.collect.Lists;
import org.junit.Test;
import org.protoframework.orm.sql.expr.ArithmeticExpr;
import org.protoframework.orm.sql.expr.ArithmeticOp;
import org.protoframework.orm.sql.expr.Column;
import org.protoframework.orm.sql.expr.Value;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author: yuanwq
 * @date: 2018/7/17
 */
public class TestArithmeticExpr {
  @Test
  public void testFieldValue() {
    ArithmeticExpr expr;
    List<ISqlValue> sqlValues;

    {
      // +
      expr = FieldAndValue.add("a", 1);
      System.out.println(expr);
      assertTrue(expr.getLeft() instanceof Column);
      assertTrue(expr.getRight() instanceof Value);
      assertEquals(ArithmeticOp.ADD, expr.getOp());
      assertEquals("a+?", expr.toSqlTemplate(new StringBuilder()).toString());
      assertEquals("a+1", expr.toSolidSql(new StringBuilder()).toString());
      sqlValues = expr.collectSqlValue(Lists.newArrayList());
      assertEquals(1, sqlValues.size());
      assertEquals("a", sqlValues.get(0).getField());
      assertEquals(1, sqlValues.get(0).getValue());
    }
    {
      // -
      expr = FieldAndValue.subtract("a", 1);
      System.out.println(expr);
      assertEquals(ArithmeticOp.SUBTRACT, expr.getOp());
      assertEquals("a-?", expr.toSqlTemplate(new StringBuilder()).toString());
      assertEquals("a-1", expr.toSolidSql(new StringBuilder()).toString());
      expr = FieldAndValue.subtract(1, "a");
      System.out.println(expr);
      assertEquals("?-a", expr.toSqlTemplate(new StringBuilder()).toString());
      assertEquals("1-a", expr.toSolidSql(new StringBuilder()).toString());
    }
    {
      // *
      expr = FieldAndValue.multiply("a", 1);
      System.out.println(expr);
      assertEquals(ArithmeticOp.MULTIPLY, expr.getOp());
      assertEquals("a*?", expr.toSqlTemplate(new StringBuilder()).toString());
      assertEquals("a*1", expr.toSolidSql(new StringBuilder()).toString());
    }
    {
      // /
      expr = FieldAndValue.divide("a", 1);
      System.out.println(expr);
      assertEquals(ArithmeticOp.DIVIDE, expr.getOp());
      assertEquals("a/?", expr.toSqlTemplate(new StringBuilder()).toString());
      assertEquals("a/1", expr.toSolidSql(new StringBuilder()).toString());
      expr = FieldAndValue.divide(1, "a");
      System.out.println(expr);
      assertEquals("?/a", expr.toSqlTemplate(new StringBuilder()).toString());
      assertEquals("1/a", expr.toSolidSql(new StringBuilder()).toString());
    }
    {
      // div
      expr = FieldAndValue.divRound("a", 1);
      System.out.println(expr);
      assertEquals(ArithmeticOp.DIV_ROUND, expr.getOp());
      assertEquals("a DIV ?", expr.toSqlTemplate(new StringBuilder()).toString());
      assertEquals("a DIV 1", expr.toSolidSql(new StringBuilder()).toString());
      expr = FieldAndValue.divRound(1, "a");
      System.out.println(expr);
      assertEquals("? DIV a", expr.toSqlTemplate(new StringBuilder()).toString());
      assertEquals("1 DIV a", expr.toSolidSql(new StringBuilder()).toString());
    }
    {
      // mod
      expr = FieldAndValue.mod("a", 1);
      System.out.println(expr);
      assertEquals(ArithmeticOp.MOD, expr.getOp());
      assertEquals("a MOD ?", expr.toSqlTemplate(new StringBuilder()).toString());
      assertEquals("a MOD 1", expr.toSolidSql(new StringBuilder()).toString());
      expr = FieldAndValue.mod(1, "a");
      System.out.println(expr);
      assertEquals("? MOD a", expr.toSqlTemplate(new StringBuilder()).toString());
      assertEquals("1 MOD a", expr.toSolidSql(new StringBuilder()).toString());
    }
  }

  @Test
  public void testFieldField() {
    ArithmeticExpr expr;
    List<ISqlValue> sqlValues;

    {
      // +
      expr = FieldAndField.add("a", "b");
      System.out.println(expr);
      assertTrue(expr.getLeft() instanceof Column);
      assertTrue(expr.getRight() instanceof Column);
      assertEquals(ArithmeticOp.ADD, expr.getOp());
      assertEquals("a+b", expr.toSqlTemplate(new StringBuilder()).toString());
      assertEquals("a+b", expr.toSolidSql(new StringBuilder()).toString());
      sqlValues = expr.collectSqlValue(Lists.newArrayList());
      assertEquals(0, sqlValues.size());
    }
    {
      // -
      expr = FieldAndField.subtract("a", "b");
      System.out.println(expr);
      assertEquals(ArithmeticOp.SUBTRACT, expr.getOp());
      assertEquals("a-b", expr.toSqlTemplate(new StringBuilder()).toString());
      assertEquals("a-b", expr.toSolidSql(new StringBuilder()).toString());
    }
    {
      // *
      expr = FieldAndField.multiply("a", "b");
      System.out.println(expr);
      assertEquals(ArithmeticOp.MULTIPLY, expr.getOp());
      assertEquals("a*b", expr.toSqlTemplate(new StringBuilder()).toString());
      assertEquals("a*b", expr.toSolidSql(new StringBuilder()).toString());
    }
    {
      // /
      expr = FieldAndField.divide("a", "b");
      System.out.println(expr);
      assertEquals(ArithmeticOp.DIVIDE, expr.getOp());
      assertEquals("a/b", expr.toSqlTemplate(new StringBuilder()).toString());
      assertEquals("a/b", expr.toSolidSql(new StringBuilder()).toString());
    }
    {
      // div
      expr = FieldAndField.divRound("a", "b");
      System.out.println(expr);
      assertEquals(ArithmeticOp.DIV_ROUND, expr.getOp());
      assertEquals("a DIV b", expr.toSqlTemplate(new StringBuilder()).toString());
      assertEquals("a DIV b", expr.toSolidSql(new StringBuilder()).toString());
    }
    {
      // mod
      expr = FieldAndField.mod("a", "b");
      System.out.println(expr);
      assertEquals(ArithmeticOp.MOD, expr.getOp());
      assertEquals("a MOD b", expr.toSqlTemplate(new StringBuilder()).toString());
      assertEquals("a MOD b", expr.toSolidSql(new StringBuilder()).toString());
    }
  }

  @Test
  public void testEmbedding() {
    ArithmeticExpr expr = ArithmeticExpr.add(
        FieldAndValue.add("a", 1), FieldAndField.add("a", "b"));
    System.out.println(expr);
    assertEquals("a+?+a+b", expr.toSqlTemplate(new StringBuilder()).toString());
    assertEquals("a+1+a+b", expr.toSolidSql(new StringBuilder()).toString());
    List<ISqlValue> sqlValues = expr.collectSqlValue(Lists.newArrayList());
    assertEquals(1, sqlValues.size());
    assertEquals("a", sqlValues.get(0).getField());
    assertEquals(1, sqlValues.get(0).getValue());

    expr = ArithmeticExpr.add(
        FieldAndField.add("a", "b"), FieldAndField.multiply("c", "d"));
    System.out.println(expr);
    assertEquals("a+b+(c*d)", expr.toSqlTemplate(new StringBuilder()).toString());

    expr = ArithmeticExpr.add(
        FieldAndField.eq("a", "b"), FieldAndField.and("c", "d"));
    System.out.println(expr);
    assertEquals("(a=b)+(c AND d)", expr.toSqlTemplate(new StringBuilder()).toString());
  }
}
