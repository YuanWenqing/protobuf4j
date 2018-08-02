package org.protoframework.orm.sql.expr;

import org.protoframework.orm.sql.IExpression;

import javax.annotation.Nonnull;

/**
 * 逻辑表达式：AND OR XOR
 * <p>
 *
 * @author: yuanwq
 * @date: 2018/7/11
 */
public class LogicalExpr extends AbstractBinaryExpr<LogicalOp> {

  protected LogicalExpr(IExpression left, @Nonnull LogicalOp op, IExpression right) {
    super(left, op, right);
  }

  public static LogicalExpr and(IExpression left, IExpression right) {
    return new LogicalExpr(left, LogicalOp.AND, right);
  }

  public static LogicalExpr or(IExpression left, IExpression right) {
    return new LogicalExpr(left, LogicalOp.OR, right);
  }

  public static LogicalExpr xor(IExpression left, IExpression right) {
    return new LogicalExpr(left, LogicalOp.XOR, right);
  }

  public static LogicalExpr not(IExpression expr) {
    return new LogicalExpr(null, LogicalOp.NOT, expr);
  }
}
