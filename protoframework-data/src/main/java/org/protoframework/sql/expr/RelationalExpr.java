package org.protoframework.sql.expr;

import org.protoframework.sql.IBinaryExpr;
import org.protoframework.sql.IExpression;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * 关系表达式：{@code = != < <= > >=}
 * <p>
 * author: yuanwq
 * date: 2018/7/11
 */
public class RelationalExpr implements IBinaryExpr<RelationalOp> {
  private final IExpression left;
  private final RelationalOp op;
  private final IExpression right;

  public RelationalExpr(IExpression left, @Nonnull RelationalOp op, IExpression right) {
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
  public RelationalOp getOp() {
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
