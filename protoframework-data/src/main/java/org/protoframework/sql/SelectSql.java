package org.protoframework.sql;

import com.google.common.base.Preconditions;
import org.protoframework.sql.clause.FromClause;
import org.protoframework.sql.clause.SelectClause;
import org.protoframework.sql.clause.WhereClause;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * author: yuanwq
 * date: 2018/7/15
 */
public class SelectSql extends AbstractSqlStatement implements ISqlStatement {
  private final SelectClause select;
  private final FromClause from;
  private WhereClause where;

  public SelectSql(@Nonnull SelectClause select, @Nonnull FromClause from) {
    Preconditions.checkNotNull(select);
    Preconditions.checkNotNull(from);
    this.select = select;
    this.from = from;
  }

  public SelectSql(@Nonnull SelectClause select, @Nonnull FromClause from, WhereClause where) {
    this(select, from);
    this.setWhere(where);
  }

  public SelectClause getSelect() {
    return select;
  }

  public FromClause getFrom() {
    return from;
  }

  public WhereClause getWhere() {
    return where;
  }

  public SelectSql setWhere(WhereClause where) {
    this.where = where;
    return this;
  }

  @Override
  public StringBuilder toSqlTemplate(@Nonnull StringBuilder sb) {
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
    sb.append(" ");
    from.toSolidSql(sb);
    if (where != null) {
      sb.append(" ");
      where.toSolidSql(sb);
    }
    return sb;
  }

  @Override
  public List<Object> collectSqlValue(@Nonnull List<Object> collectedValues) {
    select.collectSqlValue(collectedValues);
    from.collectSqlValue(collectedValues);
    if (where != null) {
      where.collectSqlValue(collectedValues);
    }
    return collectedValues;
  }
}
