package org.protoframework.sql;

import org.protoframework.sql.expr.ArithmeticExpr;
import org.protoframework.sql.expr.LogicalExpr;
import org.protoframework.sql.expr.RelationalExpr;
import org.protoframework.sql.expr.TableColumn;

/**
 * a util to build relational expression for two specified fields
 *
 * @author: yuanwq
 * @date: 2018/7/16
 */
public abstract class FieldFields {

  public static RelationalExpr eq(String left, String right) {
    return RelationalExpr.eq(TableColumn.of(left), TableColumn.of(right));
  }

  public static RelationalExpr ne(String left, String right) {
    return RelationalExpr.ne(TableColumn.of(left), TableColumn.of(right));
  }

  public static RelationalExpr lt(String left, String right) {
    return RelationalExpr.lt(TableColumn.of(left), TableColumn.of(right));
  }

  public static RelationalExpr lte(String left, String right) {
    return RelationalExpr.lte(TableColumn.of(left), TableColumn.of(right));
  }

  public static RelationalExpr gt(String left, String right) {
    return RelationalExpr.gt(TableColumn.of(left), TableColumn.of(right));
  }

  public static RelationalExpr gte(String left, String right) {
    return RelationalExpr.gte(TableColumn.of(left), TableColumn.of(right));
  }

  public static ArithmeticExpr add(String left, String right) {
    return ArithmeticExpr.add(TableColumn.of(left), TableColumn.of(right));
  }

  public static ArithmeticExpr subtract(String left, String right) {
    return ArithmeticExpr.subtract(TableColumn.of(left), TableColumn.of(right));
  }

  public static ArithmeticExpr multiply(String left, String right) {
    return ArithmeticExpr.multiply(TableColumn.of(left), TableColumn.of(right));
  }

  public static ArithmeticExpr divide(String left, String right) {
    return ArithmeticExpr.divide(TableColumn.of(left), TableColumn.of(right));
  }

  public static ArithmeticExpr divRound(String left, String right) {
    return ArithmeticExpr.divRound(TableColumn.of(left), TableColumn.of(right));
  }

  public static ArithmeticExpr mod(String left, String right) {
    return ArithmeticExpr.mod(TableColumn.of(left), TableColumn.of(right));
  }

  public static LogicalExpr and(String left, String right) {
    return LogicalExpr.and(TableColumn.of(left), TableColumn.of(right));
  }

  public static LogicalExpr or(String left, String right) {
    return LogicalExpr.or(TableColumn.of(left), TableColumn.of(right));
  }

  public static LogicalExpr xor(String left, String right) {
    return LogicalExpr.xor(TableColumn.of(left), TableColumn.of(right));
  }

  public static LogicalExpr not(String field) {
    return LogicalExpr.not(TableColumn.of(field));
  }
}
