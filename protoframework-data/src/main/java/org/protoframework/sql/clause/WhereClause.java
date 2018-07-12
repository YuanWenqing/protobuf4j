package org.protoframework.sql.clause;

import org.protoframework.sql.IExpression;
import org.protoframework.sql.ISqlStatement;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * author: yuanwq
 * date: 2018/7/12
 */
public class WhereClause implements ISqlStatement {
  private IExpression cond;
  private OrderByClause orderBy;
  private LimitClause limit;

  public IExpression getCond() {
    return cond;
  }

  public void setCond(IExpression cond) {
    this.cond = cond;
  }

  public OrderByClause getOrderBy() {
    return orderBy;
  }

  public WhereClause setOrderBy(OrderByClause orderBy) {
    this.orderBy = orderBy;
    return this;
  }

  public LimitClause getLimit() {
    return limit;
  }

  public WhereClause setLimit(LimitClause limit) {
    this.limit = limit;
    return this;
  }

  @Override
  public StringBuilder toSqlTemplate(@Nonnull StringBuilder sb) {
    if (cond != null) {
      sb.append("WHERE ");
      cond.toSqlTemplate(sb);
    }
    if (orderBy != null) {
      if (!sb.toString().endsWith(" ")) {
        sb.append(" ");
      }
      orderBy.toSqlTemplate(sb);
    }
    if (limit != null) {
      if (!sb.toString().endsWith(" ")) {
        sb.append(" ");
      }
      limit.toSqlTemplate(sb);
    }
    return sb;
  }

  @Override
  public StringBuilder toSolidSql(@Nonnull StringBuilder sb) {
    if (cond != null) {
      sb.append("WHERE ");
      cond.toSolidSql(sb);
    }
    if (orderBy != null) {
      if (!sb.toString().endsWith(" ")) {
        sb.append(" ");
      }
      orderBy.toSolidSql(sb);
    }
    if (limit != null) {
      if (!sb.toString().endsWith(" ")) {
        sb.append(" ");
      }
      limit.toSolidSql(sb);
    }
    return sb;
  }

  @Override
  public List<Object> collectSqlValue(@Nonnull List<Object> collectedValues) {
    if (cond != null) {
      cond.collectSqlValue(collectedValues);
    }
    if (orderBy != null) {
      orderBy.collectSqlValue(collectedValues);
    }
    if (limit != null) {
      limit.collectSqlValue(collectedValues);
    }
    return collectedValues;
  }
}
