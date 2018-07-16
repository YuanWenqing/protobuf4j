package org.protoframework.sql.expr;

import org.protoframework.sql.IBinaryExpr;
import org.protoframework.sql.IExpression;
import org.protoframework.sql.ISqlOperation;

import javax.annotation.Nonnull;
import java.util.List;

import static com.google.common.base.Preconditions.*;

/**
 * @author: yuanwq
 * @date: 2018/7/16
 */
public abstract class AbstractBinaryExpr<T extends ISqlOperation> extends AbstractExpression
    implements IBinaryExpr<T> {
  private static final String WRAP_LEFT = "(";
  private static final String WRAP_RIGHT = ")";

  protected final IExpression left;
  protected final T op;
  protected final IExpression right;

  public AbstractBinaryExpr(IExpression left, T op, IExpression right) {
    checkNotNull(op);
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
  public T getOp() {
    return op;
  }

  @Override
  public StringBuilder toSqlTemplate(@Nonnull StringBuilder sb) {
    if (this.getLeft() != null) {
      boolean needWrap = this.getLeft().comparePrecedence(this.getOp()) < 0;
      if (needWrap) {
        sb.append(WRAP_LEFT);
      }
      this.getLeft().toSqlTemplate(sb);
      if (needWrap) {
        sb.append(WRAP_RIGHT);
      }
    }
    sb.append(this.getOp().getOp());
    if (this.getRight() != null) {
      boolean needWrap = this.getRight().comparePrecedence(this.getOp()) < 0;
      if (needWrap) {
        sb.append(WRAP_LEFT);
      }
      this.getRight().toSqlTemplate(sb);
      if (needWrap) {
        sb.append(WRAP_RIGHT);
      }
    }
    return sb;
  }

  @Override
  public StringBuilder toSolidSql(@Nonnull StringBuilder sb) {
    if (this.getLeft() != null) {
      boolean needWrap = this.getLeft().comparePrecedence(this.getOp()) < 0;
      if (needWrap) {
        sb.append(WRAP_LEFT);
      }
      this.getLeft().toSolidSql(sb);
      if (needWrap) {
        sb.append(WRAP_RIGHT);
      }
    }
    sb.append(this.getOp().getOp());
    if (this.getRight() != null) {
      boolean needWrap = this.getRight().comparePrecedence(this.getOp()) < 0;
      if (needWrap) {
        sb.append(WRAP_LEFT);
      }
      this.getRight().toSolidSql(sb);
      if (needWrap) {
        sb.append(WRAP_RIGHT);
      }
    }
    return sb;
  }

  @Override
  public List<Object> collectSqlValue(@Nonnull List<Object> collectedValues) {
    if (this.getLeft() != null) {
      this.getLeft().collectSqlValue(collectedValues);
    }
    if (this.getRight() != null) {
      this.getRight().collectSqlValue(collectedValues);
    }
    return collectedValues;
  }
}
