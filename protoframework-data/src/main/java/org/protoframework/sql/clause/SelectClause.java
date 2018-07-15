package org.protoframework.sql.clause;

import com.google.common.base.Preconditions;
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
public class SelectClause implements ISqlStatement {
  private final List<SelectExpr> selectExprs = Lists.newArrayList();

  public List<SelectExpr> getSelectExprs() {
    return Collections.unmodifiableList(selectExprs);
  }

  public boolean isEmpty() {
    return selectExprs.isEmpty();
  }

  public SelectClause select(SelectExpr selectExpr) {
    this.selectExprs.add(selectExpr);
    return this;
  }

  public SelectClause select(IExpression expr) {
    return select(new SelectExpr(expr));
  }

  public SelectClause select(String column) {
    return select(new TableColumn(column));
  }

  @Override
  public StringBuilder toSqlTemplate(@Nonnull StringBuilder sb) {
    Preconditions.checkArgument(selectExprs.isEmpty(), "nothing to select");
    sb.append("SELECT ");
    boolean first = true;
    for (SelectExpr selectExpr : selectExprs) {
      if (first) {
        first = false;
      } else {
        sb.append(",");
      }
      selectExpr.toSqlTemplate(sb);
    }
    return sb;
  }

  @Override
  public StringBuilder toSolidSql(@Nonnull StringBuilder sb) {
    sb.append("SELECT ");
    boolean first = true;
    for (SelectExpr selectExpr : selectExprs) {
      if (first) {
        first = false;
      } else {
        sb.append(",");
      }
      selectExpr.toSolidSql(sb);
    }
    return sb;
  }

  @Override
  public List<Object> collectSqlValue(@Nonnull List<Object> collectedValues) {
    for (SelectExpr selectExpr : selectExprs) {
      selectExpr.collectSqlValue(collectedValues);
    }
    return collectedValues;
  }

  @Override
  public String toString() {
    return toSolidSql(new StringBuilder()).toString();
  }
}
