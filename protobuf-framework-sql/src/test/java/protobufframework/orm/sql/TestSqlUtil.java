package protobufframework.orm.sql;

import com.google.common.collect.Lists;
import org.junit.Test;
import protobufframework.orm.sql.expr.ArithmeticOp;

import static org.junit.Assert.*;

/**
 * author: yuanwq
 * date: 2018/7/17
 */
public class TestSqlUtil {
  @Test
  public void testLike() {
    assertEquals("%a", SqlUtil.likeSuffix("a"));
    assertEquals("a%", SqlUtil.likePrefix("a"));
    assertEquals("%a%", SqlUtil.likeSub("a"));

    assertEquals("%%a%", SqlUtil.likePrefix("%a"));
  }

  @Test
  public void testStar() {
    try {
      System.out.println(SqlUtil.SELECT_STAR);
      SqlUtil.SELECT_STAR.setAlias("a");
      fail();
    } catch (UnsupportedOperationException e) {
      System.out.println(e.getMessage());
    }
  }

  @Test
  public void testCount() {
    try {
      System.out.println(SqlUtil.SELECT_COUNT);
      SqlUtil.SELECT_COUNT.setAlias("a");
      fail();
    } catch (UnsupportedOperationException e) {
      System.out.println(e.getMessage());
    }
  }

  @Test
  public void testAggregateWrap() {
    IExpression expr = SqlUtil.aggregateWrap("sum", QueryCreator.column("a"));
    System.out.println(expr);
    assertEquals("sum(a)", expr.toSqlTemplate(new StringBuilder()).toString());
    assertEquals("sum(a)", expr.toSolidSql(new StringBuilder()).toString());
    assertTrue(expr.comparePrecedence(ArithmeticOp.ADD) > 0);
    assertTrue(expr.collectSqlValue(Lists.newArrayList()).isEmpty());
  }
}
