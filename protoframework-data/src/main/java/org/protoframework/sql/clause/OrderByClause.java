package org.protoframework.sql.clause;

import com.google.common.collect.Lists;
import org.protoframework.sql.AbstractSqlStatement;
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
public class OrderByClause extends AbstractSqlStatement {
  private final List<OrderByExpr> orderByExprs = Lists.newArrayList();

  public List<OrderByExpr> getOrderByExprs() {
    return Collections.unmodifiableList(orderByExprs);
  }

  public OrderByClause by(OrderByExpr orderByExpr) {
    this.orderByExprs.add(orderByExpr);
    return this;
  }

  public OrderByClause by(IExpression expr) {
    return by(new OrderByExpr(expr));
  }

  public OrderByClause by(String column) {
    return by(new TableColumn(column));
  }

  @Override
  public StringBuilder toSqlTemplate(@Nonnull StringBuilder sb) {
    if (orderByExprs.isEmpty()) {
      return sb;
    }
    sb.append("ORDER BY ");
    boolean first = true;
    for (OrderByExpr expr : orderByExprs) {
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
    if (orderByExprs.isEmpty()) {
      return sb;
    }
    sb.append("ORDER BY ");
    boolean first = true;
    for (OrderByExpr expr : orderByExprs) {
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
    for (OrderByExpr expr : orderByExprs) {
      expr.collectSqlValue(sqlValues);
    }
    return sqlValues;
  }

}
