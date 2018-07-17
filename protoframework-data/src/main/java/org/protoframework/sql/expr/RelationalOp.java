package org.protoframework.sql.expr;

import org.protoframework.sql.IExpression;
import org.protoframework.sql.ISqlOperation;

import static com.google.common.base.Preconditions.*;

/**
 * @author: yuanwq
 * @date: 2018/7/11
 */

public enum RelationalOp implements ISqlOperation<RelationalExpr> {
  EQ("="),
  NE("!="),
  GT(">"),
  GTE(">="),
  LT("<"),
  LTE("<="),
  /**
   * only left expr, no right
   */
  IS_NULL(" IS NULL") {
    @Override
    public void checkExpression(IExpression left, IExpression right) {
      checkNotNull(left, "left expr is null");
      checkArgument(right == null, "no right expr for IS_NULL");
    }
  },
  /**
   * only left expr, no right
   */
  IS_NOT_NULL(" IS NOT NULL") {
    @Override
    public void checkExpression(IExpression left, IExpression right) {
      checkNotNull(left, "left expr is null");
      checkArgument(right == null, "no right expr for IS_NOT_NULL");
    }
  },
  LIKE(" LIKE ") {
  },
  /**
   * right expr must be {@link BetweenExpr}
   */
  BETWEEN(" BETWEEN ") {
    @Override
    public void checkExpression(IExpression left, IExpression right) {
      checkNotNull(left, "left expr is null");
      checkNotNull(right, "right expr is null");
      checkArgument(right instanceof BetweenExpr,
          "right expr of BETWEEN must be of type " + BetweenExpr.class.getName() + ", instead of " +
              right.getClass().getName());
    }
  },
  /**
   * right expr must a {@link ValueCollection}
   */
  IN(" IN ") {
    @Override
    public void checkExpression(IExpression left, IExpression right) {
      checkNotNull(left, "left expr is null");
      checkNotNull(right, "right expr is null");
      checkArgument(right instanceof ValueCollection,
          "right expr of IN must be of type " + ValueCollection.class.getName() + ", instead of " +
              right.getClass().getName());
    }
  },
  /**
   * right expr must a {@link ValueCollection}
   */
  NIN(" NOT IN ") {
    @Override
    public void checkExpression(IExpression left, IExpression right) {
      checkNotNull(left, "left expr is null");
      checkNotNull(right, "right expr is null");
      checkArgument(right instanceof ValueCollection,
          "right expr of NIN must be of type " + ValueCollection.class.getName() + ", instead of " +
              right.getClass().getName());
    }
  };

  private final String op;

  RelationalOp(String op) {
    this.op = op;
  }

  @Override
  public String getOp() {
    return op;
  }
}
