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
    return RelationalExpr.eq(new TableColumn(left), new TableColumn(right));
  }

  public static RelationalExpr ne(String left, String right) {
    return RelationalExpr.ne(new TableColumn(left), new TableColumn(right));
  }

  public static RelationalExpr lt(String left, String right) {
    return RelationalExpr.lt(new TableColumn(left), new TableColumn(right));
  }

  public static RelationalExpr lte(String left, String right) {
    return RelationalExpr.lte(new TableColumn(left), new TableColumn(right));
  }

  public static RelationalExpr gt(String left, String right) {
    return RelationalExpr.gt(new TableColumn(left), new TableColumn(right));
  }

  public static RelationalExpr gte(String left, String right) {
    return RelationalExpr.gte(new TableColumn(left), new TableColumn(right));
  }

  public static ArithmeticExpr add(String left, String right) {
    return ArithmeticExpr.add(new TableColumn(left), new TableColumn(right));
  }

  public static ArithmeticExpr subtract(String left, String right) {
    return ArithmeticExpr.subtract(new TableColumn(left), new TableColumn(right));
  }

  public static ArithmeticExpr multiply(String left, String right) {
    return ArithmeticExpr.multiply(new TableColumn(left), new TableColumn(right));
  }

  public static ArithmeticExpr divide(String left, String right) {
    return ArithmeticExpr.divide(new TableColumn(left), new TableColumn(right));
  }

  public static ArithmeticExpr divRound(String left, String right) {
    return ArithmeticExpr.divRound(new TableColumn(left), new TableColumn(right));
  }

  public static ArithmeticExpr mod(String left, String right) {
    return ArithmeticExpr.mod(new TableColumn(left), new TableColumn(right));
  }

  public static LogicalExpr and(String left, String right) {
    return LogicalExpr.and(new TableColumn(left), new TableColumn(right));
  }

  public static LogicalExpr or(String left, String right) {
    return LogicalExpr.or(new TableColumn(left), new TableColumn(right));
  }

  public static LogicalExpr xor(String left, String right) {
    return LogicalExpr.xor(new TableColumn(left), new TableColumn(right));
  }

  public static LogicalExpr not(String field) {
    return LogicalExpr.not(new TableColumn(field));
  }
}
