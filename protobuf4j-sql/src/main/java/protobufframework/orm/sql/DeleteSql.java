package protobufframework.orm.sql;

import lombok.Data;
import lombok.NonNull;
import protobufframework.orm.sql.clause.FromClause;
import protobufframework.orm.sql.clause.WhereClause;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * author: yuanwq
 * date: 2018/7/15
 */
@Data
public class DeleteSql extends AbstractSqlObject implements ISqlStatement {
  @NonNull
  private final FromClause from;
  private WhereClause where;

  /**
   * create {@link WhereClause} if necessary
   */
  public WhereClause where() {
    if (where == null) {
      this.where = new WhereClause();
    }
    return this.where;
  }

  @Override
  public StringBuilder toSqlTemplate(@Nonnull StringBuilder sb) {
    sb.append("DELETE ");
    from.toSqlTemplate(sb);
    if (where != null) {
      sb.append(" ");
      where.toSqlTemplate(sb);
    }
    return sb;
  }

  @Override
  public StringBuilder toSolidSql(@Nonnull StringBuilder sb) {
    sb.append("DELETE ");
    from.toSolidSql(sb);
    if (where != null) {
      sb.append(" ");
      where.toSolidSql(sb);
    }
    return sb;
  }

  @Override
  public List<ISqlValue> collectSqlValue(@Nonnull List<ISqlValue> sqlValues) {
    from.collectSqlValue(sqlValues);
    if (where != null) {
      where.collectSqlValue(sqlValues);
    }
    return sqlValues;
  }
}
