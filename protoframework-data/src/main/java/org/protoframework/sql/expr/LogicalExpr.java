package org.protoframework.sql.expr;

import org.protoframework.sql.IExpression;

import javax.annotation.Nonnull;

/**
 * 逻辑表达式：AND OR XOR
 * <p>
 *
 * @author: yuanwq
 * @date: 2018/7/11
 */
public class LogicalExpr extends AbstractBinaryExpr<LogicalOp> {

  public LogicalExpr(IExpression left, @Nonnull LogicalOp op, IExpression right) {
    super(left, op, right);
  }

}
