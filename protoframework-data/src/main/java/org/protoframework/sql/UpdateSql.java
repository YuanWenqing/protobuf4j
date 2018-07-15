package org.protoframework.sql;

import com.google.common.base.Preconditions;
import org.protoframework.sql.clause.SetClause;
import org.protoframework.sql.clause.WhereClause;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * author: yuanwq
 * date: 2018/7/15
 */
public class UpdateSql extends AbstractSqlStatement implements ISqlStatement {
  private final ITableRef table;
  private final SetClause set;
  private WhereClause where;

  public UpdateSql(@Nonnull ITableRef table, @Nonnull SetClause set) {
    Preconditions.checkNotNull(table);
    Preconditions.checkNotNull(set);
    this.table = table;
    this.set = set;
  }

  public UpdateSql(@Nonnull ITableRef table, @Nonnull SetClause set, WhereClause where) {
    this(table, set);
    this.setWhere(where);
  }

  public ITableRef getTable() {
    return table;
  }

  public SetClause getSet() {
    return set;
  }

  public WhereClause getWhere() {
    return where;
  }

  public UpdateSql setWhere(WhereClause where) {
    this.where = where;
    return this;
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
  public List<Object> collectSqlValue(@Nonnull List<Object> collectedValues) {
    set.collectSqlValue(collectedValues);
    if (where != null) {
      where.collectSqlValue(collectedValues);
    }
    return collectedValues;
  }
}
