package org.protoframework.sql.expr;

import org.protoframework.sql.IExpression;

import javax.annotation.Nonnull;

/**
 * 算术表达式：{@code + - * / DIV MOD | ^ ~}
 * <p>
 *
 * @author: yuanwq
 * @date: 2018/7/11
 */
public class ArithmeticExpr extends AbstractBinaryExpr<ArithmeticOp> {
  protected ArithmeticExpr(IExpression left, @Nonnull ArithmeticOp op, IExpression right) {
    super(left, op, right);
  }

  public static ArithmeticExpr add(IExpression left, IExpression right) {
    return new ArithmeticExpr(left, ArithmeticOp.ADD, right);
  }

  public static ArithmeticExpr sub(IExpression left, IExpression right) {
    return new ArithmeticExpr(left, ArithmeticOp.SUBSTRACT, right);
  }

  public static ArithmeticExpr multi(IExpression left, IExpression right) {
    return new ArithmeticExpr(left, ArithmeticOp.MULTIPLY, right);
  }

  public static ArithmeticExpr div(IExpression left, IExpression right) {
    return new ArithmeticExpr(left, ArithmeticOp.DIVIDE, right);
  }

  public static ArithmeticExpr divRound(IExpression left, IExpression right) {
    return new ArithmeticExpr(left, ArithmeticOp.DIV_ROUND, right);
  }

  public static ArithmeticExpr mod(IExpression left, IExpression right) {
    return new ArithmeticExpr(left, ArithmeticOp.MOD, right);
  }
}
