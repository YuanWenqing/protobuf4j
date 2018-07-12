package org.protoframework.sql.clause;

import com.google.common.collect.Lists;
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
public class GroupByClause implements ISqlStatement {
  private final List<IExpression> groupExprs = Lists.newArrayList();

  public List<IExpression> getGroupExprs() {
    return Collections.unmodifiableList(groupExprs);
  }

  public GroupByClause by(IExpression expr) {
    this.groupExprs.add(expr);
    return this;
  }

  public GroupByClause by(String column) {
    return by(new TableColumn(column));
  }

  @Override
  public StringBuilder toSqlTemplate(@Nonnull StringBuilder sb) {
    if (groupExprs.isEmpty()) {
      return sb;
    }
    sb.append("GROUP BY ");
    boolean first = true;
    for (IExpression expr : groupExprs) {
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
    if (groupExprs.isEmpty()) {
      return sb;
    }
    sb.append("GROUP BY ");
    boolean first = true;
    for (IExpression expr : groupExprs) {
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
    for (IExpression expr : groupExprs) {
      expr.collectSqlValue(collectedValues);
    }
    return collectedValues;
  }

  @Override
  public String toString() {
    return toSolidSql(new StringBuilder()).toString();
  }
}
