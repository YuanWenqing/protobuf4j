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
    return new RelationalExpr(new TableColumn(field), RelationalOp.EQ, Value.of(value, field));
  }

  public static IExpression ne(String field, Object value) {
    return new RelationalExpr(new TableColumn(field), RelationalOp.NE, Value.of(value, field));
  }

  public static IExpression lt(String field, Object value) {
    return new RelationalExpr(new TableColumn(field), RelationalOp.LT, Value.of(value, field));
  }

  public static IExpression lte(String field, Object value) {
    return new RelationalExpr(new TableColumn(field), RelationalOp.LTE, Value.of(value, field));
  }

  public static IExpression gt(String field, Object value) {
    return new RelationalExpr(new TableColumn(field), RelationalOp.GT, Value.of(value, field));
  }

  public static IExpression gte(String field, Object value) {
    return new RelationalExpr(new TableColumn(field), RelationalOp.GTE, Value.of(value, field));
  }

  public static IExpression isNull(String field) {
    return new RelationalExpr(new TableColumn(field), RelationalOp.IS_NULL, null);
  }

  public static IExpression isNotNull(String field) {
    return new RelationalExpr(new TableColumn(field), RelationalOp.IS_NOT_NULL, null);
  }

  public static IExpression like(String field, String value) {
    return new RelationalExpr(new TableColumn(field), RelationalOp.LIKE, Value.of(value, field));
  }

  public static IExpression between(String field, Object min, Object max) {
    BetweenExpr betweenExpr = new BetweenExpr(Value.of(min, field), Value.of(max, field));
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
