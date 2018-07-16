package org.protoframework.sql;

import org.protoframework.sql.expr.*;

import java.util.Collection;

/**
 * a util to build relational expression for a specified field and some specified value(s)
 *
 * @author: yuanwq
 * @date: 2018/7/16
 */
public abstract class FieldValues {

  public static IExpression eq(String field, Object value) {
    return new RelationalExpr(new TableColumn(field), RelationalOp.EQ, new Value(value, field));
  }

  public static IExpression ne(String field, Object value) {
    return new RelationalExpr(new TableColumn(field), RelationalOp.NE, new Value(value, field));
  }

  public static IExpression lt(String field, Object value) {
    return new RelationalExpr(new TableColumn(field), RelationalOp.LT, new Value(value, field));
  }

  public static IExpression lte(String field, Object value) {
    return new RelationalExpr(new TableColumn(field), RelationalOp.LTE, new Value(value, field));
  }

  public static IExpression gt(String field, Object value) {
    return new RelationalExpr(new TableColumn(field), RelationalOp.GT, new Value(value, field));
  }

  public static IExpression gte(String field, Object value) {
    return new RelationalExpr(new TableColumn(field), RelationalOp.GTE, new Value(value, field));
  }

  public static IExpression isNull(String field) {
    return new RelationalExpr(new TableColumn(field), RelationalOp.IS_NULL, null);
  }

  public static IExpression isNotNull(String field) {
    return new RelationalExpr(new TableColumn(field), RelationalOp.IS_NULL, null);
  }

  public static IExpression like(String field, String value) {
    return new RelationalExpr(new TableColumn(field), RelationalOp.LIKE, new Value(value, field));
  }

  public static IExpression between(String field, Object min, Object max) {
    BetweenExpr betweenExpr = new BetweenExpr(new Value(min, field), new Value(max, field));
    return new RelationalExpr(new TableColumn(field), RelationalOp.BETWEEN, betweenExpr);
  }

  public static IExpression in(String field, Collection<?> values) {
    return new RelationalExpr(new TableColumn(field), RelationalOp.IN,
        new ValueCollection(values, field));
  }

  public static IExpression nin(String field, Collection<?> values) {
    return new RelationalExpr(new TableColumn(field), RelationalOp.NIN,
        new ValueCollection(values, field));
  }
}
