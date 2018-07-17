package org.protoframework.sql.clause;

import org.protoframework.sql.AbstractSqlStatement;
import org.protoframework.sql.Direction;
import org.protoframework.sql.IExpression;
import org.protoframework.sql.ISqlValue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * @author: yuanwq
 * @date: 2018/7/12
 */
public class GroupByExpr extends AbstractSqlStatement {
  private final IExpression expression;
  private final Direction direction;

  public GroupByExpr(@Nonnull IExpression expression, @Nullable Direction direction) {
    this.expression = expression;
    this.direction = direction;
  }

  public GroupByExpr(@Nonnull IExpression expression) {
    this(expression, null);
  }

  public IExpression getExpression() {
    return expression;
  }

  public Direction getDirection() {
    return direction;
  }

  @Override
  public StringBuilder toSqlTemplate(@Nonnull StringBuilder sb) {
    this.expression.toSqlTemplate(sb);
    if (direction != null) {
      sb.append(" ").append(direction.name());
    }
    return sb;
  }

  @Override
  public StringBuilder toSolidSql(@Nonnull StringBuilder sb) {
    this.expression.toSolidSql(sb);
    if (direction != null) {
      sb.append(" ").append(direction.name());
    }
    return sb;
  }

  @Override
  public List<ISqlValue> collectSqlValue(@Nonnull List<ISqlValue> sqlValues) {
    return expression.collectSqlValue(sqlValues);
  }

}
