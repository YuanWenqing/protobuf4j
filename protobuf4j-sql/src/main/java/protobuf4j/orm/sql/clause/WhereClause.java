package protobuf4j.orm.sql.clause;

import lombok.Data;
import protobuf4j.orm.sql.AbstractSqlObject;
import protobuf4j.orm.sql.IExpression;
import protobuf4j.orm.sql.ISqlValue;
import protobuf4j.orm.sql.QueryCreator;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * author: yuanwq
 * date: 2018/7/12
 */
@Data
public class WhereClause extends AbstractSqlObject {
  private IExpression cond;
  private OrderByClause orderBy;
  private GroupByClause groupBy;
  private PaginationClause pagination;

  /**
   * create {@link OrderByClause} if necessary
   */
  public OrderByClause orderBy() {
    if (orderBy == null) {
      setOrderBy(QueryCreator.orderBy());
    }
    return orderBy;
  }

  /**
   * create {@link GroupByClause} if necessary
   */
  public GroupByClause groupBy() {
    if (groupBy == null) {
      setGroupBy(QueryCreator.groupBy());
    }
    return groupBy;
  }

  /**
   * {@code limit <limit> offset 0}
   */
  public WhereClause limit(int limit) {
    setPagination(PaginationClause.newBuilder(limit).build());
    return this;
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
