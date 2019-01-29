package protobuf4j.orm.sql;

import protobuf4j.orm.sql.expr.*;

import java.util.Collection;

/**
 * a util to build relational expression for a specified field and some specified value(s)
 *
 * author: yuanwq
 * date: 2018/7/16
 */
public interface FieldAndValue {

  static RelationalExpr eq(String field, Object value) {
    return RelationalExpr.eq(Column.of(field), Value.of(value, field));
  }

  static RelationalExpr ne(String field, Object value) {
    return RelationalExpr.ne(Column.of(field), Value.of(value, field));
  }

  static RelationalExpr lt(String field, Object value) {
    return RelationalExpr.lt(Column.of(field), Value.of(value, field));
  }

  static RelationalExpr lte(String field, Object value) {
    return RelationalExpr.lte(Column.of(field), Value.of(value, field));
  }

  static RelationalExpr gt(String field, Object value) {
    return RelationalExpr.gt(Column.of(field), Value.of(value, field));
  }

  static RelationalExpr gte(String field, Object value) {
    return RelationalExpr.gte(Column.of(field), Value.of(value, field));
  }

  static RelationalExpr isNull(String field) {
    return RelationalExpr.isNull(Column.of(field));
  }

  static RelationalExpr isNotNull(String field) {
    return RelationalExpr.isNotNull(Column.of(field));
  }

  static RelationalExpr like(String field, String value) {
    return RelationalExpr.like(Column.of(field), Value.of(value, field));
  }

  static RelationalExpr between(String field, Object min, Object max) {
    BetweenExpr betweenExpr = new BetweenExpr(Value.of(min, field), Value.of(max, field));
    return RelationalExpr.between(Column.of(field), betweenExpr);
  }

  static RelationalExpr in(String field, Collection<?> values) {
    return RelationalExpr.in(Column.of(field), ValueCollection.of(values, field));
  }

  static RelationalExpr nin(String field, Collection<?> values) {
    return RelationalExpr.nin(Column.of(field), ValueCollection.of(values, field));
  }

  static ArithmeticExpr add(String field, Number value) {
    return ArithmeticExpr.add(Column.of(field), Value.of(value, field));
  }

  static ArithmeticExpr subtract(String field, Number value) {
    return ArithmeticExpr.subtract(Column.of(field), Value.of(value, field));
  }

  static ArithmeticExpr multiply(String field, Number value) {
    return ArithmeticExpr.multiply(Column.of(field), Value.of(value, field));
  }

  static ArithmeticExpr divide(String field, Number value) {
    return ArithmeticExpr.divide(Column.of(field), Value.of(value, field));
  }

  static ArithmeticExpr divRound(String field, Number value) {
    return ArithmeticExpr.divRound(Column.of(field), Value.of(value, field));
  }

  static ArithmeticExpr mod(String field, Number value) {
    return ArithmeticExpr.mod(Column.of(field), Value.of(value, field));
  }

  static ArithmeticExpr subtract(Number value, String field) {
    return ArithmeticExpr.subtract(Value.of(value, field), Column.of(field));
  }

  static ArithmeticExpr divide(Number value, String field) {
    return ArithmeticExpr.divide(Value.of(value, field), Column.of(field));
  }

  static ArithmeticExpr divRound(Number value, String field) {
    return ArithmeticExpr.divRound(Value.of(value, field), Column.of(field));
  }

  static ArithmeticExpr mod(Number value, String field) {
    return ArithmeticExpr.mod(Value.of(value, field), Column.of(field));
  }
}
