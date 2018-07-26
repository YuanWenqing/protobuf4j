package org.protoframework.sql.expr;

import org.protoframework.sql.IExpression;
import org.protoframework.sql.ISqlOperator;
import org.protoframework.sql.ISqlValue;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author: yuanwq
 * @date: 2018/7/11
 */
public class BetweenExpr extends AbstractExpression {
  private static final String WRAP_LEFT = "(";
  private static final String WRAP_RIGHT = ")";

  private final IExpression min;
  private final IExpression max;

  public BetweenExpr(@Nonnull IExpression min, @Nonnull IExpression max) {
    this.min = min;
    this.max = max;
  }

  public IExpression getMin() {
    return min;
  }

  public IExpression getMax() {
    return max;
  }

  @Override
  public int comparePrecedence(@Nonnull ISqlOperator outerOp) {
    return 1;
  }

  @Override
  public StringBuilder toSqlTemplate(@Nonnull StringBuilder sb) {
    boolean needWrap = min.comparePrecedence(LogicalOp.AND) < 0;
    if (needWrap) {
      sb.append(WRAP_LEFT);
    }
    min.toSqlTemplate(sb);
    if (needWrap) {
      sb.append(WRAP_RIGHT);
    }
    sb.append(" AND ");
    needWrap = max.comparePrecedence(LogicalOp.AND) < 0;
    if (needWrap) {
      sb.append(WRAP_LEFT);
    }
    max.toSqlTemplate(sb);
    if (needWrap) {
      sb.append(WRAP_RIGHT);
    }
    return sb;
  }

  @Override
  public StringBuilder toSolidSql(@Nonnull StringBuilder sb) {
    boolean needWrap = min.comparePrecedence(LogicalOp.AND) < 0;
    if (needWrap) {
      sb.append(WRAP_LEFT);
    }
    min.toSolidSql(sb);
    if (needWrap) {
      sb.append(WRAP_RIGHT);
    }
    sb.append(" AND ");
    needWrap = max.comparePrecedence(LogicalOp.AND) < 0;
    if (needWrap) {
      sb.append(WRAP_LEFT);
    }
    max.toSolidSql(sb);
    if (needWrap) {
      sb.append(WRAP_RIGHT);
    }
    return sb;
  }

  @Override
  public List<ISqlValue> collectSqlValue(@Nonnull List<ISqlValue> sqlValues) {
    min.collectSqlValue(sqlValues);
    max.collectSqlValue(sqlValues);
    return sqlValues;
  }

}
