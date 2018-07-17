package org.protoframework.sql;

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
}
