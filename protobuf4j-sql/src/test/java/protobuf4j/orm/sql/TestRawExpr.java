package protobuf4j.orm.sql;

import com.google.common.collect.Lists;
import org.junit.Test;
import protobuf4j.orm.sql.expr.ArithmeticOp;
import protobuf4j.orm.sql.expr.RawExpr;

import static org.junit.Assert.*;

/**
 * author: yuanwq
 * date: 2018/7/17
 */
public class TestRawExpr {

  @Test
  public void testRawExpr() {
    RawExpr expr;

    expr = new RawExpr("a");
    System.out.println(expr);
    assertEquals("a", expr.getSql());
    assertTrue(expr.getValues().isEmpty());
    assertEquals("a", expr.toSqlTemplate(new StringBuilder()).toString());
    assertEquals("a", expr.toSolidSql(new StringBuilder()).toString());
    assertTrue(expr.collectSqlValue(Lists.newArrayList()).isEmpty());
    assertTrue(expr.comparePrecedence(ArithmeticOp.ADD) > 0);

    expr = new RawExpr("a+b");
    System.out.println(expr);
    assertEquals("a+b", expr.getSql());
    assertTrue(expr.getValues().isEmpty());
    assertEquals("a+b", expr.toSqlTemplate(new StringBuilder()).toString());
    assertEquals("a+b", expr.toSolidSql(new StringBuilder()).toString());
    assertTrue(expr.collectSqlValue(Lists.newArrayList()).isEmpty());
    assertTrue(expr.comparePrecedence(ArithmeticOp.ADD) < 0);

    expr = new RawExpr("?", Lists.newArrayList(1));
    System.out.println(expr);
    assertEquals("?", expr.getSql());
    assertEquals("?", expr.toSqlTemplate(new StringBuilder()).toString());
    assertEquals("1", expr.toSolidSql(new StringBuilder()).toString());

    expr = new RawExpr("(? AND ?)", Lists.newArrayList(1));
    System.out.println(expr);
    assertEquals("(? AND ?)", expr.getSql());
    assertEquals("(? AND ?)", expr.toSqlTemplate(new StringBuilder()).toString());
    assertEquals("(1 AND ?)", expr.toSolidSql(new StringBuilder()).toString());
    assertTrue(expr.comparePrecedence(ArithmeticOp.ADD) > 0);

    expr = new RawExpr("a+?", Lists.newArrayList(1));
    System.out.println(expr);
    assertEquals("a+?", expr.getSql());
    assertEquals("a+?", expr.toSqlTemplate(new StringBuilder()).toString());
    assertEquals("a+1", expr.toSolidSql(new StringBuilder()).toString());

    expr = new RawExpr("?+a", Lists.newArrayList(1));
    System.out.println(expr);
    assertEquals("?+a", expr.getSql());
    assertEquals("?+a", expr.toSqlTemplate(new StringBuilder()).toString());
    assertEquals("1+a", expr.toSolidSql(new StringBuilder()).toString());

    expr = new RawExpr("? AND a", Lists.newArrayList(1));
    System.out.println(expr);
    assertEquals("? AND a", expr.getSql());
    assertEquals("? AND a", expr.toSqlTemplate(new StringBuilder()).toString());
    assertEquals("1 AND a", expr.toSolidSql(new StringBuilder()).toString());
    assertTrue(expr.comparePrecedence(ArithmeticOp.ADD) < 0);
  }
}
