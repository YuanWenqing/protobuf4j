package org.protoframework.sql.clause;

import org.protoframework.sql.AbstractSqlStatement;
import org.protoframework.sql.IExpression;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author: yuanwq
 * @date: 2018/7/12
 */
public class WhereClause extends AbstractSqlStatement {
  private IExpression cond;
  private OrderByClause orderBy;
  private GroupByClause groupBy;
  private PaginationClause pagination;

  public IExpression getCond() {
    return cond;
  }

  public WhereClause setCond(IExpression cond) {
    this.cond = cond;
    return this;
  }

  public OrderByClause getOrderBy() {
    return orderBy;
  }

  public WhereClause setOrderBy(OrderByClause orderBy) {
    this.orderBy = orderBy;
    return this;
  }

  public GroupByClause getGroupBy() {
    return groupBy;
  }

  public void setGroupBy(GroupByClause groupBy) {
    this.groupBy = groupBy;
  }

  public PaginationClause getPagination() {
    return pagination;
  }

  public WhereClause setPagination(PaginationClause pagination) {
    this.pagination = pagination;
    return this;
  }

  public WhereClause limit(int limit) {
    return setPagination(PaginationClause.newBuilder(limit).build());
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
    if (groupBy != null) {
      if (!sb.toString().endsWith(" ")) {
        sb.append(" ");
      }
      groupBy.toSqlTemplate(sb);
    }
    if (pagination != null) {
      if (!sb.toString().endsWith(" ")) {
        sb.append(" ");
      }
      pagination.toSqlTemplate(sb);
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
    if (groupBy != null) {
      if (!sb.toString().endsWith(" ")) {
        sb.append(" ");
      }
      groupBy.toSolidSql(sb);
    }
    if (pagination != null) {
      if (!sb.toString().endsWith(" ")) {
        sb.append(" ");
      }
      pagination.toSolidSql(sb);
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
    if (pagination != null) {
      pagination.collectSqlValue(collectedValues);
    }
    return collectedValues;
  }

}
