package org.protoframework.orm.sql.clause;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.protoframework.orm.sql.AbstractSqlObject;
import org.protoframework.orm.sql.IExpression;
import org.protoframework.orm.sql.ISqlValue;
import org.protoframework.orm.sql.SqlUtil;
import org.protoframework.orm.sql.expr.Column;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

/**
 * @author: yuanwq
 * @date: 2018/7/12
 */
public class SelectClause extends AbstractSqlObject {
  private final List<SelectExpr> selectExprs = Lists.newArrayList();

  public List<SelectExpr> getSelectExprs() {
    return Collections.unmodifiableList(selectExprs);
  }

  public boolean isEmpty() {
    return selectExprs.isEmpty();
  }

  public SelectClause clear() {
    selectExprs.clear();
    return this;
  }

  public SelectClause select(SelectExpr selectExpr) {
    this.selectExprs.add(selectExpr);
    return this;
  }

  public SelectClause select(IExpression expr) {
    return select(new SelectExpr(expr));
  }

  public SelectClause select(String column) {
    return select(Column.of(column));
  }

  public SelectClause star() {
    return select(SqlUtil.SELECT_STAR);
  }

  @Override
  public StringBuilder toSqlTemplate(@Nonnull StringBuilder sb) {
    Preconditions.checkArgument(!selectExprs.isEmpty(), "nothing to select");
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
  public List<ISqlValue> collectSqlValue(@Nonnull List<ISqlValue> sqlValues) {
    for (SelectExpr selectExpr : selectExprs) {
      selectExpr.collectSqlValue(sqlValues);
    }
    return sqlValues;
  }

}
