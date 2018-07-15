package org.protoframework.sql.expr;

import org.protoframework.sql.IExpression;
import org.protoframework.sql.ISqlOperation;
import org.protoframework.sql.RawSql;

import javax.annotation.Nonnull;
import java.util.Collection;

/**
 * @author: yuanwq
 * @date: 2018/7/11
 */
public class RawExpr extends RawSql implements IExpression {

  public RawExpr(@Nonnull String rawSqlExpr) {
    super(rawSqlExpr);
  }

  public RawExpr(@Nonnull String rawSqlExpr, @Nonnull Collection<?> values) {
    super(rawSqlExpr, values);
  }

  @Override
  public int comparePrecedence(@Nonnull ISqlOperation outerOp) {
    return 1;
  }

}
