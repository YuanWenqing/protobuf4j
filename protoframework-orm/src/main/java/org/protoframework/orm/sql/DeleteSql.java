package org.protoframework.orm.sql;

import com.google.common.base.Preconditions;
import org.protoframework.orm.sql.clause.FromClause;
import org.protoframework.orm.sql.clause.WhereClause;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author: yuanwq
 * @date: 2018/7/15
 */
public class DeleteSql extends AbstractSqlObject implements ISqlStatement {
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

  public WhereClause where() {
    this.where = new WhereClause();
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
