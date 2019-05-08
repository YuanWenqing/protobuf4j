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
    return Expressions.eq(Column.of(field), Value.of(value, field));
  }

  static RelationalExpr ne(String field, Object value) {
    return Expressions.ne(Column.of(field), Value.of(value, field));
  }

  static RelationalExpr lt(String field, Object value) {
    return Expressions.lt(Column.of(field), Value.of(value, field));
  }

  static RelationalExpr lte(String field, Object value) {
    return Expressions.lte(Column.of(field), Value.of(value, field));
  }

  static RelationalExpr gt(String field, Object value) {
    return Expressions.gt(Column.of(field), Value.of(value, field));
  }

  static RelationalExpr gte(String field, Object value) {
    return Expressions.gte(Column.of(field), Value.of(value, field));
  }

  static RelationalExpr isNull(String field) {
    return Expressions.isNull(Column.of(field));
  }

  static RelationalExpr isNotNull(String field) {
    return Expressions.isNotNull(Column.of(field));
  }

  static RelationalExpr like(String field, String value) {
    return Expressions.like(Column.of(field), Value.of(value, field));
  }

  static RelationalExpr between(String field, Object min, Object max) {
    BetweenExpr betweenExpr = new BetweenExpr(Value.of(min, field), Value.of(max, field));
    return Expressions.between(Column.of(field), betweenExpr);
  }

  static RelationalExpr in(String field, Collection<?> values) {
    return Expressions.in(Column.of(field), ValueCollection.of(values, field));
  }

  static RelationalExpr nin(String field, Collection<?> values) {
    return Expressions.nin(Column.of(field), ValueCollection.of(values, field));
  }

  static ArithmeticExpr add(String field, Number value) {
    return Expressions.add(Column.of(field), Value.of(value, field));
  }

  static ArithmeticExpr subtract(String field, Number value) {
    return Expressions.subtract(Column.of(field), Value.of(value, field));
  }

  static ArithmeticExpr multiply(String field, Number value) {
    return Expressions.multiply(Column.of(field), Value.of(value, field));
  }

  static ArithmeticExpr divide(String field, Number value) {
    return Expressions.divide(Column.of(field), Value.of(value, field));
  }

  static ArithmeticExpr divRound(String field, Number value) {
    return Expressions.divRound(Column.of(field), Value.of(value, field));
  }

  static ArithmeticExpr mod(String field, Number value) {
    return Expressions.mod(Column.of(field), Value.of(value, field));
  }

  static ArithmeticExpr subtract(Number value, String field) {
    return Expressions.subtract(Value.of(value, field), Column.of(field));
  }

  static ArithmeticExpr divide(Number value, String field) {
    return Expressions.divide(Value.of(value, field), Column.of(field));
  }

  static ArithmeticExpr divRound(Number value, String field) {
    return Expressions.divRound(Value.of(value, field), Column.of(field));
  }

  static ArithmeticExpr mod(Number value, String field) {
    return Expressions.mod(Value.of(value, field), Column.of(field));
  }
}
