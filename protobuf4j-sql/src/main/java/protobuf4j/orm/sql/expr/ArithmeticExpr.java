package protobuf4j.orm.sql.expr;

import protobuf4j.orm.sql.IExpression;

import javax.annotation.Nonnull;

/**
 * 算术表达式：{@code + - * / DIV MOD | ^ ~}
 * <p>
 *
 * author: yuanwq
 * date: 2018/7/11
 */
public class ArithmeticExpr extends AbstractBinaryExpr<ArithmeticOp> {
  protected ArithmeticExpr(IExpression left, @Nonnull ArithmeticOp op, IExpression right) {
    super(left, op, right);
  }


}
