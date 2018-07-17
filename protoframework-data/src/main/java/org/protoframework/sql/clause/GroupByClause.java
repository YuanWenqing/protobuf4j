package org.protoframework.sql.clause;

import com.google.common.collect.Lists;
import org.protoframework.sql.AbstractSqlStatement;
import org.protoframework.sql.Direction;
import org.protoframework.sql.IExpression;
import org.protoframework.sql.ISqlValue;
import org.protoframework.sql.expr.TableColumn;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

/**
 * @author: yuanwq
 * @date: 2018/7/12
 */
public class GroupByClause extends AbstractSqlStatement {
  private final List<GroupByExpr> groupByExprs = Lists.newArrayList();

  public List<GroupByExpr> getGroupByExprs() {
    return Collections.unmodifiableList(groupByExprs);
  }

  public GroupByClause by(GroupByExpr groupByExpr) {
    this.groupByExprs.add(groupByExpr);
    return this;
  }

  public GroupByClause by(IExpression expr) {
    return by(new GroupByExpr(expr));
  }

  public GroupByClause by(String column) {
    return by(TableColumn.of(column));
  }

  public GroupByClause asc(IExpression expr) {
    return by(new GroupByExpr(expr, Direction.ASC));
  }

  public GroupByClause asc(String column) {
    return asc(TableColumn.of(column));
  }

  public GroupByClause desc(IExpression expr) {
    return by(new GroupByExpr(expr, Direction.DESC));
  }

  public GroupByClause desc(String column) {
    return desc(TableColumn.of(column));
  }

  public boolean isEmpty() {
    return groupByExprs.isEmpty();
  }

  @Override
  public StringBuilder toSqlTemplate(@Nonnull StringBuilder sb) {
    if (groupByExprs.isEmpty()) {
      return sb;
    }
    sb.append("GROUP BY ");
    boolean first = true;
    for (GroupByExpr expr : groupByExprs) {
      if (first) {
        first = false;
      } else {
        sb.append(",");
      }
      expr.toSqlTemplate(sb);
    }
    return sb;
  }

  @Override
  public StringBuilder toSolidSql(@Nonnull StringBuilder sb) {
    if (groupByExprs.isEmpty()) {
      return sb;
    }
    sb.append("GROUP BY ");
    boolean first = true;
    for (GroupByExpr expr : groupByExprs) {
      if (first) {
        first = false;
      } else {
        sb.append(",");
      }
      expr.toSolidSql(sb);
    }
    return sb;
  }

  @Override
  public List<ISqlValue> collectSqlValue(@Nonnull List<ISqlValue> sqlValues) {
    for (GroupByExpr expr : groupByExprs) {
      expr.collectSqlValue(sqlValues);
    }
    return sqlValues;
  }

}
