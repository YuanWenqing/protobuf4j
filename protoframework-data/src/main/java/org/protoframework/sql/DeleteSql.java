package org.protoframework.sql;

import com.google.common.base.Preconditions;
import org.protoframework.sql.clause.FromClause;
import org.protoframework.sql.clause.WhereClause;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * author: yuanwq
 * date: 2018/7/15
 */
public class DeleteSql implements ISqlStatement {
  private final FromClause from;
  private WhereClause where;

  public DeleteSql(@Nonnull FromClause from) {
    Preconditions.checkNotNull(from);
    this.from = from;
  }

  public DeleteSql(@Nonnull FromClause from, WhereClause where) {
    this(from);
    setWhere(where);
  }

  public FromClause getFrom() {
    return from;
  }

  public WhereClause getWhere() {
    return where;
  }

  public DeleteSql setWhere(WhereClause where) {
    this.where = where;
    return this;
  }

  @Override
  public StringBuilder toSqlTemplate(@Nonnull StringBuilder sb) {
    sb.append("DELETE ");
    from.toSqlTemplate(sb);
    if (where != null) {
      where.toSqlTemplate(sb);
    }
    return sb;
  }

  @Override
  public StringBuilder toSolidSql(@Nonnull StringBuilder sb) {
    sb.append("DELETE ");
    from.toSolidSql(sb);
    if (where != null) {
      where.toSolidSql(sb);
    }
    return sb;
  }

  @Override
  public List<Object> collectSqlValue(@Nonnull List<Object> collectedValues) {
    from.collectSqlValue(collectedValues);
    if (where != null) {
      where.collectSqlValue(collectedValues);
    }
    return collectedValues;
  }
}
