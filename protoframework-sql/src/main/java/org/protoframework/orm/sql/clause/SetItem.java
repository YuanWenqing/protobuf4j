package org.protoframework.orm.sql.clause;

import lombok.Data;
import lombok.NonNull;
import org.protoframework.orm.sql.AbstractSqlObject;
import org.protoframework.orm.sql.IExpression;
import org.protoframework.orm.sql.ISqlValue;
import org.protoframework.orm.sql.expr.Column;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author: yuanwq
 * @date: 2018/7/14
 */
@Data
public class SetItem extends AbstractSqlObject {
  @NonNull
  private final Column column;
  @NonNull
  private final IExpression value;

  @Override
  public StringBuilder toSqlTemplate(@Nonnull StringBuilder sb) {
    column.toSqlTemplate(sb);
    sb.append("=");
    value.toSqlTemplate(sb);
    return sb;
  }

  @Override
  public StringBuilder toSolidSql(@Nonnull StringBuilder sb) {
    column.toSolidSql(sb);
    sb.append("=");
    value.toSolidSql(sb);
    return sb;
  }

  @Override
  public List<ISqlValue> collectSqlValue(@Nonnull List<ISqlValue> sqlValues) {
    column.collectSqlValue(sqlValues);
    value.collectSqlValue(sqlValues);
    return sqlValues;
  }
}
