package org.protoframework.orm.sql.expr;

import lombok.Data;
import lombok.NonNull;
import org.protoframework.orm.sql.IExpression;
import org.protoframework.orm.sql.ISqlOperator;
import org.protoframework.orm.sql.ISqlValue;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author: yuanwq
 * @date: 2018/7/11
 */
@Data
public class BetweenExpr extends AbstractExpression {
  private static final String WRAP_LEFT = "(";
  private static final String WRAP_RIGHT = ")";

  @NonNull
  private final IExpression min;
  @NonNull
  private final IExpression max;

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
