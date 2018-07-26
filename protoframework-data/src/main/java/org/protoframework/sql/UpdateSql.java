package org.protoframework.sql;

import org.protoframework.sql.clause.SetClause;
import org.protoframework.sql.clause.WhereClause;

import javax.annotation.Nonnull;
import java.util.List;

import static com.google.common.base.Preconditions.*;

/**
 * @author: yuanwq
 * @date: 2018/7/15
 */
public class UpdateSql extends AbstractSqlObject {
  private final ITableRef table;
  private SetClause set;
  private WhereClause where;

  public UpdateSql(@Nonnull ITableRef table) {
    checkNotNull(table);
    this.table = table;
  }

  public UpdateSql(@Nonnull ITableRef table, SetClause set, WhereClause where) {
    this(table);
    this.set = set;
    this.where = where;
  }

  public ITableRef getTable() {
    return table;
  }

  public SetClause getSet() {
    return set;
  }

  public UpdateSql setSet(SetClause set) {
    this.set = set;
    return this;
  }

  public SetClause set() {
    this.set = new SetClause();
    return this.set;
  }

  public WhereClause getWhere() {
    return where;
  }

  public UpdateSql setWhere(WhereClause where) {
    this.where = where;
    return this;
  }

  public WhereClause where() {
    this.where = new WhereClause();
    return this.where;
  }

  @Override
  public StringBuilder toSqlTemplate(@Nonnull StringBuilder sb) {
    checkNotNull(set);
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
    if (set != null && !set.isEmpty()) {
      sb.append(" ");
      set.toSolidSql(sb);
    }
    if (where != null) {
      sb.append(" ");
      where.toSolidSql(sb);
    }
    return sb;
  }

  @Override
  public List<ISqlValue> collectSqlValue(@Nonnull List<ISqlValue> sqlValues) {
    if (set != null) {
      set.collectSqlValue(sqlValues);
    }
    if (where != null) {
      where.collectSqlValue(sqlValues);
    }
    return sqlValues;
  }
}
