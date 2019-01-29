package protobufframework.orm.sql.clause;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import protobufframework.orm.sql.AbstractSqlObject;
import protobufframework.orm.sql.Direction;
import protobufframework.orm.sql.IExpression;
import protobufframework.orm.sql.ISqlValue;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * author: yuanwq
 * date: 2018/7/12
 */
@Data
@RequiredArgsConstructor
public class GroupByItem extends AbstractSqlObject {
  @NonNull
  private final IExpression expression;
  private Direction direction;

  public GroupByItem(IExpression expression, Direction direction) {
    this.expression = expression;
    this.direction = direction;
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
