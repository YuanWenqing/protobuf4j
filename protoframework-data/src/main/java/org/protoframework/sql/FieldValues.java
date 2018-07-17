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

  public static RelationalExpr eq(String field, Object value) {
    return RelationalExpr.eq(new TableColumn(field), Value.of(value, field));
  }

  public static RelationalExpr ne(String field, Object value) {
    return RelationalExpr.ne(new TableColumn(field), Value.of(value, field));
  }

  public static RelationalExpr lt(String field, Object value) {
    return RelationalExpr.lt(new TableColumn(field), Value.of(value, field));
  }

  public static RelationalExpr lte(String field, Object value) {
    return RelationalExpr.lte(new TableColumn(field), Value.of(value, field));
  }

  public static RelationalExpr gt(String field, Object value) {
    return RelationalExpr.gt(new TableColumn(field), Value.of(value, field));
  }

  public static RelationalExpr gte(String field, Object value) {
    return RelationalExpr.gte(new TableColumn(field), Value.of(value, field));
  }

  public static RelationalExpr isNull(String field) {
    return RelationalExpr.isNull(new TableColumn(field));
  }

  public static RelationalExpr isNotNull(String field) {
    return RelationalExpr.isNotNull(new TableColumn(field));
  }

  public static RelationalExpr like(String field, String value) {
    return RelationalExpr.like(new TableColumn(field), Value.of(value, field));
  }

  public static RelationalExpr between(String field, Object min, Object max) {
    BetweenExpr betweenExpr = new BetweenExpr(Value.of(min, field), Value.of(max, field));
    return RelationalExpr.between(new TableColumn(field), betweenExpr);
  }

  public static RelationalExpr in(String field, Collection<?> values) {
    return RelationalExpr.in(new TableColumn(field), ValueCollection.of(values, field));
  }

  public static RelationalExpr nin(String field, Collection<?> values) {
    return RelationalExpr.nin(new TableColumn(field), ValueCollection.of(values, field));
  }

  public static ArithmeticExpr add(String field, Number value) {
    return ArithmeticExpr.add(new TableColumn(field), Value.of(value, field));
  }

  public static ArithmeticExpr subtract(String field, Number value) {
    return ArithmeticExpr.subtract(new TableColumn(field), Value.of(value, field));
  }

  public static ArithmeticExpr multiply(String field, Number value) {
    return ArithmeticExpr.multiply(new TableColumn(field), Value.of(value, field));
  }

  public static ArithmeticExpr divide(String field, Number value) {
    return ArithmeticExpr.divide(new TableColumn(field), Value.of(value, field));
  }

  public static ArithmeticExpr divRound(String field, Number value) {
    return ArithmeticExpr.divRound(new TableColumn(field), Value.of(value, field));
  }

  public static ArithmeticExpr mod(String field, Number value) {
    return ArithmeticExpr.mod(new TableColumn(field), Value.of(value, field));
  }

  public static ArithmeticExpr subtract(Number value, String field) {
    return ArithmeticExpr.subtract(Value.of(value, field), new TableColumn(field));
  }

  public static ArithmeticExpr divide(Number value, String field) {
    return ArithmeticExpr.divide(Value.of(value, field), new TableColumn(field));
  }

  public static ArithmeticExpr divRound(Number value, String field) {
    return ArithmeticExpr.divRound(Value.of(value, field), new TableColumn(field));
  }

  public static ArithmeticExpr mod(Number value, String field) {
    return ArithmeticExpr.mod(Value.of(value, field), new TableColumn(field));
  }
}
