package org.protoframework.sql.clause;

import org.protoframework.sql.AbstractSqlStatement;
import org.protoframework.sql.Direction;
import org.protoframework.sql.IExpression;
import org.protoframework.sql.ISqlStatement;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * author: yuanwq
 * date: 2018/7/12
 */
public class OrderByExpr extends AbstractSqlStatement implements ISqlStatement {
  private final IExpression expr;
  private final Direction direction;

  public OrderByExpr(@Nonnull IExpression expr, @Nullable Direction direction) {
    this.expr = expr;
    this.direction = direction;
  }

  public OrderByExpr(@Nonnull IExpression expr) {
    this(expr, null);
  }

  public IExpression getExpr() {
    return expr;
  }

  public Direction getDirection() {
    return direction;
  }

  @Override
  public StringBuilder toSqlTemplate(@Nonnull StringBuilder sb) {
    this.expr.toSqlTemplate(sb);
    if (direction != null) {
      sb.append(" ").append(direction.name());
    }
    return sb;
  }

  @Override
  public StringBuilder toSolidSql(@Nonnull StringBuilder sb) {
    this.expr.toSolidSql(sb);
    if (direction != null) {
      sb.append(" ").append(direction.name());
    }
    return sb;
  }

  @Override
  public List<Object> collectSqlValue(@Nonnull List<Object> collectedValues) {
    return expr.collectSqlValue(collectedValues);
  }

}
