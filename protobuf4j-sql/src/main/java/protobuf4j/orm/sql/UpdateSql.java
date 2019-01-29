package protobuf4j.orm.sql;

import lombok.Data;
import lombok.NonNull;
import protobuf4j.orm.sql.clause.SetClause;
import protobuf4j.orm.sql.clause.WhereClause;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * author: yuanwq
 * date: 2018/7/15
 */
@Data
public class UpdateSql extends AbstractSqlObject implements ISqlStatement {
  @NonNull
  private final ITableRef table;
  @NonNull
  private final SetClause set;
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
    sb.append("UPDATE ");
    table.toSqlTemplate(sb);
    sb.append(" ");
    set.toSqlTemplate(sb);
    if (where != null) {
      sb.append(" ");
      where.toSqlTemplate(sb);
    }
    return sb;
  }

  @Override
  public StringBuilder toSolidSql(@Nonnull StringBuilder sb) {
    sb.append("UPDATE ");
    table.toSolidSql(sb);
    sb.append(" ");
    set.toSolidSql(sb);
    if (where != null) {
      sb.append(" ");
      where.toSolidSql(sb);
    }
    return sb;
  }

  @Override
  public List<ISqlValue> collectSqlValue(@Nonnull List<ISqlValue> sqlValues) {
    set.collectSqlValue(sqlValues);
    if (where != null) {
      where.collectSqlValue(sqlValues);
    }
    return sqlValues;
  }
}
