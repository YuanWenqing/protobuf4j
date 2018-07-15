package org.protoframework.sql.clause;

import com.google.common.collect.Lists;
import org.protoframework.sql.AbstractSqlStatement;
import org.protoframework.sql.IExpression;
import org.protoframework.sql.ISqlStatement;
import org.protoframework.sql.expr.TableColumn;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

/**
 * author: yuanwq
 * date: 2018/7/12
 */
public class GroupByClause extends AbstractSqlStatement implements ISqlStatement {
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
    return by(new TableColumn(column));
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
  public List<Object> collectSqlValue(@Nonnull List<Object> collectedValues) {
    for (GroupByExpr expr : groupByExprs) {
      expr.collectSqlValue(collectedValues);
    }
    return collectedValues;
  }

}
