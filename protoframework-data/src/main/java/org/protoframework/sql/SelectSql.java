package org.protoframework.sql;

import org.protoframework.sql.clause.FromClause;
import org.protoframework.sql.clause.SelectClause;
import org.protoframework.sql.clause.WhereClause;

import javax.annotation.Nonnull;
import java.util.List;

import static com.google.common.base.Preconditions.*;

/**
 * @author: yuanwq
 * @date: 2018/7/15
 */
public class SelectSql extends AbstractSqlStatement {
  private SelectClause select;
  private final FromClause from;
  private WhereClause where;

  public SelectSql(FromClause from) {
    this.from = from;
  }

  public SelectSql(SelectClause select, FromClause from, WhereClause where) {
    this.select = select;
    this.from = from;
    this.where = where;
  }

  public SelectClause getSelect() {
    return select;
  }

  public SelectSql setSelect(SelectClause select) {
    this.select = select;
    return this;
  }

  public SelectClause select() {
    this.select = new SelectClause();
    return this.select;
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

  public WhereClause where() {
    this.where = new WhereClause();
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
    if (select != null) {
      select.toSolidSql(sb);
    }
    if (from != null) {
      if (sb.length() > 0 && sb.charAt(sb.length() - 1) != ' ') {
        sb.append(" ");
      }
      from.toSolidSql(sb);
    }
    if (where != null) {
      if (sb.length() > 0 && sb.charAt(sb.length() - 1) != ' ') {
        sb.append(" ");
      }
      where.toSolidSql(sb);
    }
    return sb;
  }

  @Override
  public List<ISqlValue> collectSqlValue(@Nonnull List<ISqlValue> sqlValues) {
    if (select != null) {
      select.collectSqlValue(sqlValues);
    }
    if (from != null) {
      from.collectSqlValue(sqlValues);
    }
    if (where != null) {
      where.collectSqlValue(sqlValues);
    }
    return sqlValues;
  }
}
