package org.protoframework.orm.sql.clause;

import com.google.common.collect.Lists;
import lombok.Data;
import org.protoframework.orm.sql.AbstractSqlObject;
import org.protoframework.orm.sql.Direction;
import org.protoframework.orm.sql.IExpression;
import org.protoframework.orm.sql.ISqlValue;
import org.protoframework.orm.sql.expr.Column;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author: yuanwq
 * @date: 2018/7/12
 */
@Data
public class OrderByClause extends AbstractSqlObject {
  private final List<OrderByItem> orderByItems = Lists.newArrayList();

  public OrderByClause clear() {
    orderByItems.clear();
    return this;
  }

  public OrderByClause by(OrderByItem orderByItem) {
    this.orderByItems.add(orderByItem);
    return this;
  }

  public OrderByClause asc(IExpression expr) {
    return by(new OrderByItem(expr, Direction.ASC));
  }

  public OrderByClause asc(String column) {
    return asc(Column.of(column));
  }

  public OrderByClause desc(IExpression expr) {
    return by(new OrderByItem(expr, Direction.DESC));
  }

  public OrderByClause desc(String column) {
    return desc(Column.of(column));
  }

  public boolean isEmpty() {
    return orderByItems.isEmpty();
  }

  @Override
  public StringBuilder toSqlTemplate(@Nonnull StringBuilder sb) {
    if (orderByItems.isEmpty()) {
      return sb;
    }
    sb.append("ORDER BY ");
    boolean first = true;
    for (OrderByItem expr : orderByItems) {
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
    if (orderByItems.isEmpty()) {
      return sb;
    }
    sb.append("ORDER BY ");
    boolean first = true;
    for (OrderByItem expr : orderByItems) {
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
    for (OrderByItem expr : orderByItems) {
      expr.collectSqlValue(sqlValues);
    }
    return sqlValues;
  }

}
