package protobufframework.orm.sql.expr;

import org.junit.Test;

/**
 * author: yuanwq
 * date: 2018/10/13
 */
public class TestOpCheck {
  @Test(expected = IllegalArgumentException.class)
  public void testIsNull() {
    new RelationalExpr(Value.of(1), RelationalOp.IS_NULL, Value.of(2));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsNotNull() {
    new RelationalExpr(Value.of(1), RelationalOp.IS_NOT_NULL, Value.of(2));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNot() {
    new LogicalExpr(Value.of(1), LogicalOp.NOT, Value.of(2));
  }
}
