package protobuf4j.orm.sql;

import lombok.Data;
import lombok.NonNull;
import protobuf4j.orm.sql.clause.FromClause;
import protobuf4j.orm.sql.clause.SelectClause;
import protobuf4j.orm.sql.clause.WhereClause;

import javax.annotation.Nonnull;
import java.util.List;

import static com.google.common.base.Preconditions.*;

/**
 * author: yuanwq
 * date: 2018/7/15
 */
@Data
public class SelectSql extends AbstractSqlObject implements ISqlStatement {
  @NonNull
  private final SelectClause select;
  @NonNull
  private final FromClause from;
  private WhereClause where;

  /**
   * create {@link WhereClause} if necessary
   */
  public WhereClause where() {
    if (this.where == null) {
      this.where = new WhereClause();
    }
    return this.where;
  }

  @Override
  public StringBuilder toSqlTemplate(@Nonnull StringBuilder sb) {
    checkNotNull(select);
    checkNotNull(from);
    select.toSqlTemplate(sb);
    sb.append(" ");
    from.toSqlTemplate(sb);
    if (where != null) {
      sb.append(" ");
      where.toSqlTemplate(sb);
    }
    return sb;
  }

  @Override
  public StringBuilder toSolidSql(@Nonnull StringBuilder sb) {
    select.toSolidSql(sb);
    if (sb.length() > 0 && sb.charAt(sb.length() - 1) != ' ') {
      sb.append(" ");
    }
    from.toSolidSql(sb);
    if (where != null) {
      sb.append(" ");
      where.toSolidSql(sb);
    }
    return sb;
  }

  @Override
  public List<ISqlValue> collectSqlValue(@Nonnull List<ISqlValue> sqlValues) {
    select.collectSqlValue(sqlValues);
    from.collectSqlValue(sqlValues);
    if (where != null) {
      where.collectSqlValue(sqlValues);
    }
    return sqlValues;
  }
}
