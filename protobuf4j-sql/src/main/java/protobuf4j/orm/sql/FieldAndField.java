package protobuf4j.orm.sql;

import protobuf4j.orm.sql.expr.*;

/**
 * a util to build relational expression for two specified fields
 * <p>
 * author: yuanwq
 * date: 2018/7/16
 */
public interface FieldAndField {

  static RelationalExpr eq(String left, String right) {
    return Expressions.eq(Column.of(left), Column.of(right));
  }

  static RelationalExpr ne(String left, String right) {
    return Expressions.ne(Column.of(left), Column.of(right));
  }

  static RelationalExpr lt(String left, String right) {
    return Expressions.lt(Column.of(left), Column.of(right));
  }

  static RelationalExpr lte(String left, String right) {
    return Expressions.lte(Column.of(left), Column.of(right));
  }

  static RelationalExpr gt(String left, String right) {
    return Expressions.gt(Column.of(left), Column.of(right));
  }

  static RelationalExpr gte(String left, String right) {
    return Expressions.gte(Column.of(left), Column.of(right));
  }

  static ArithmeticExpr add(String left, String right) {
    return Expressions.add(Column.of(left), Column.of(right));
  }

  static ArithmeticExpr subtract(String left, String right) {
    return Expressions.subtract(Column.of(left), Column.of(right));
  }

  static ArithmeticExpr multiply(String left, String right) {
    return Expressions.multiply(Column.of(left), Column.of(right));
  }

  static ArithmeticExpr divide(String left, String right) {
    return Expressions.divide(Column.of(left), Column.of(right));
  }

  static ArithmeticExpr divRound(String left, String right) {
    return Expressions.divRound(Column.of(left), Column.of(right));
  }

  static ArithmeticExpr mod(String left, String right) {
    return Expressions.mod(Column.of(left), Column.of(right));
  }

  static LogicalExpr and(String left, String right) {
    return Expressions.and(Column.of(left), Column.of(right));
  }

  static LogicalExpr or(String left, String right) {
    return Expressions.or(Column.of(left), Column.of(right));
  }

  static LogicalExpr xor(String left, String right) {
    return Expressions.xor(Column.of(left), Column.of(right));
  }

  static LogicalExpr not(String field) {
    return Expressions.not(Column.of(field));
  }
}
