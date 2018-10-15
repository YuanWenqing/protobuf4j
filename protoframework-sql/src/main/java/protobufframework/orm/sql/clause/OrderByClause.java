package protobufframework.orm.sql.clause;

import com.google.common.collect.Lists;
import lombok.Data;
import protobufframework.orm.sql.AbstractSqlObject;
import protobufframework.orm.sql.Direction;
import protobufframework.orm.sql.IExpression;
import protobufframework.orm.sql.ISqlValue;
import protobufframework.orm.sql.expr.Column;

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

  private OrderByClause addOrderByItem(OrderByItem orderByItem) {
    this.orderByItems.add(orderByItem);
    return this;
  }

  public OrderByClause by(IExpression expr) {
    return addOrderByItem(new OrderByItem(expr));
  }

  public OrderByClause by(String column) {
    return by(Column.of(column));
  }

  public OrderByClause asc(IExpression expr) {
    return addOrderByItem(new OrderByItem(expr, Direction.ASC));
  }

  public OrderByClause asc(String column) {
    return asc(Column.of(column));
  }

  public OrderByClause desc(IExpression expr) {
    return addOrderByItem(new OrderByItem(expr, Direction.DESC));
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
