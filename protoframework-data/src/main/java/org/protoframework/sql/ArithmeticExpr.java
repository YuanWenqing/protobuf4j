package org.protoframework.sql;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * 算术表达式：{@code + - * / DIV MOD | ^ ~}
 * <p>
 * author: yuanwq
 * date: 2018/7/11
 */
public class ArithmeticExpr implements IBinaryExpr<ArithmeticOp> {
  private final IExpression left;
  private final ArithmeticOp op;
  private final IExpression right;

  public ArithmeticExpr(IExpression left, @Nonnull ArithmeticOp op, IExpression right) {
    this.left = left;
    this.op = op;
    this.right = right;
  }

  @Override
  public IExpression getLeft() {
    return left;
  }

  @Override
  public IExpression getRight() {
    return right;
  }

  @Override
  public ArithmeticOp getOp() {
    return op;
  }

  @Override
  public StringBuilder toSqlTemplate(@Nonnull StringBuilder sb) {
    this.op.toSqlTemplate(this, sb);
    return sb;
  }

  @Override
  public StringBuilder toSolidSql(@Nonnull StringBuilder sb) {
    this.op.toSolidSql(this, sb);
    return sb;
  }

  @Override
  public List<Object> collectSqlValue(@Nonnull List<Object> collectedValues) {
    this.left.collectSqlValue(collectedValues);
    this.right.collectSqlValue(collectedValues);
    return collectedValues;
  }

  @Override
  public String toString() {
    return toSolidSql(new StringBuilder()).toString();
  }
}
