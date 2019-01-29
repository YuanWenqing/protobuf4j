package protobufframework.orm.sql.expr;

import lombok.Data;
import lombok.NonNull;
import protobufframework.orm.sql.IBinaryExpr;
import protobufframework.orm.sql.IExpression;
import protobufframework.orm.sql.ISqlOperator;
import protobufframework.orm.sql.ISqlValue;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;

/**
 * author: yuanwq
 * date: 2018/7/16
 */
@Data
public abstract class AbstractBinaryExpr<T extends ISqlOperator> extends AbstractExpression
    implements IBinaryExpr<T> {
  private static final String WRAP_LEFT = "(";
  private static final String WRAP_RIGHT = ")";

  protected final IExpression left;
  @NonNull
  protected final T op;
  protected final IExpression right;

  public AbstractBinaryExpr(IExpression left, T op, IExpression right) {
    op.checkExpression(left, right);
    this.left = left;
    this.op = op;
    this.right = right;
  }

  @Override
  public int comparePrecedence(@Nonnull ISqlOperator outerOp) {
    // 默认：运算符不同，就需要包裹括号
    return Objects.equals(getOp(), outerOp) ? 0 : -1;
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
    if (this.left != null) {
      boolean needWrap = this.left.comparePrecedence(this.op) < 0;
      if (needWrap) {
        sb.append(WRAP_LEFT);
      }
      this.left.toSqlTemplate(sb);
      if (needWrap) {
        sb.append(WRAP_RIGHT);
      }
    }
    sb.append(this.op.getOp());
    if (this.right != null) {
      boolean needWrap = this.right.comparePrecedence(this.op) < 0;
      if (needWrap) {
        sb.append(WRAP_LEFT);
      }
      this.right.toSqlTemplate(sb);
      if (needWrap) {
        sb.append(WRAP_RIGHT);
      }
    }
    return sb;
  }

  @Override
  public StringBuilder toSolidSql(@Nonnull StringBuilder sb) {
    if (this.left != null) {
      boolean needWrap = this.left.comparePrecedence(this.op) < 0;
      if (needWrap) {
        sb.append(WRAP_LEFT);
      }
      this.left.toSolidSql(sb);
      if (needWrap) {
        sb.append(WRAP_RIGHT);
      }
    }
    sb.append(this.op.getOp());
    if (this.right != null) {
      boolean needWrap = this.right.comparePrecedence(this.op) < 0;
      if (needWrap) {
        sb.append(WRAP_LEFT);
      }
      this.right.toSolidSql(sb);
      if (needWrap) {
        sb.append(WRAP_RIGHT);
      }
    }
    return sb;
  }

  @Override
  public List<ISqlValue> collectSqlValue(@Nonnull List<ISqlValue> sqlValues) {
    if (this.left != null) {
      this.left.collectSqlValue(sqlValues);
    }
    if (this.right != null) {
      this.right.collectSqlValue(sqlValues);
    }
    return sqlValues;
  }
}
