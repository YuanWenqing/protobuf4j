package org.protoframework.sql.expr;

import org.protoframework.sql.IExpression;
import org.protoframework.sql.ISqlOperator;

import static com.google.common.base.Preconditions.*;

/**
 * @author: yuanwq
 * @date: 2018/7/11
 */

public enum LogicalOp implements ISqlOperator {
  AND(" AND "),
  OR(" OR "),
  XOR(" XOR "),
  /**
   * no left expr
   */
  NOT("NOT ") {
    @Override
    public void checkExpression(IExpression left, IExpression right) {
      checkArgument(left == null, "no left expr for NOT");
      checkNotNull(right, "right expr is null");
    }
  };

  private final String op;

  LogicalOp(String op) {
    this.op = op;
  }

  @Override
  public String getOp() {
    return op;
  }

}
