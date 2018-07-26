package org.protoframework.sql.clause;

import org.protoframework.sql.AbstractSqlObject;
import org.protoframework.sql.IExpression;
import org.protoframework.sql.ISqlValue;
import org.protoframework.sql.QueryCreator;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author: yuanwq
 * @date: 2018/7/12
 */
public class WhereClause extends AbstractSqlObject {
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

  public OrderByClause orderBy() {
    if (orderBy == null) {
      setOrderBy(QueryCreator.orderBy());
    }
    return orderBy;
  }

  public GroupByClause getGroupBy() {
    return groupBy;
  }

  public WhereClause setGroupBy(GroupByClause groupBy) {
    this.groupBy = groupBy;
    return this;
  }

  public GroupByClause groupBy() {
    if (groupBy == null) {
      setGroupBy(QueryCreator.groupBy());
    }
    return groupBy;
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
    if (orderBy != null && !orderBy.isEmpty()) {
      if (sb.length() > 0 && sb.charAt(sb.length() - 1) != ' ') {
        sb.append(" ");
      }
      orderBy.toSqlTemplate(sb);
    }
    if (groupBy != null && !groupBy.isEmpty()) {
      if (sb.length() > 0 && sb.charAt(sb.length() - 1) != ' ') {
        sb.append(" ");
      }
      groupBy.toSqlTemplate(sb);
    }
    if (pagination != null) {
      if (sb.length() > 0 && sb.charAt(sb.length() - 1) != ' ') {
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
    if (orderBy != null && !orderBy.isEmpty()) {
      if (sb.length() > 0 && sb.charAt(sb.length() - 1) != ' ') {
        sb.append(" ");
      }
      orderBy.toSolidSql(sb);
    }
    if (groupBy != null && !groupBy.isEmpty()) {
      if (sb.length() > 0 && sb.charAt(sb.length() - 1) != ' ') {
        sb.append(" ");
      }
      groupBy.toSolidSql(sb);
    }
    if (pagination != null) {
      if (sb.length() > 0 && sb.charAt(sb.length() - 1) != ' ') {
        sb.append(" ");
      }
      pagination.toSolidSql(sb);
    }
    return sb;
  }

  @Override
  public List<ISqlValue> collectSqlValue(@Nonnull List<ISqlValue> sqlValues) {
    if (cond != null) {
      cond.collectSqlValue(sqlValues);
    }
    if (orderBy != null && !orderBy.isEmpty()) {
      orderBy.collectSqlValue(sqlValues);
    }
    if (groupBy != null && !groupBy.isEmpty()) {
      groupBy.collectSqlValue(sqlValues);
    }
    if (pagination != null) {
      pagination.collectSqlValue(sqlValues);
    }
    return sqlValues;
  }

}
