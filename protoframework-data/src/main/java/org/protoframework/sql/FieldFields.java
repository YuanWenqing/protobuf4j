package org.protoframework.sql;

import org.protoframework.sql.expr.RelationalExpr;
import org.protoframework.sql.expr.RelationalOp;
import org.protoframework.sql.expr.TableColumn;

/**
 * a util to build relational expression for two specified fields
 *
 * @author: yuanwq
 * @date: 2018/7/16
 */
public abstract class FieldFields {

  public static IExpression eq(String left, String right) {
    return new RelationalExpr(new TableColumn(left), RelationalOp.EQ, new TableColumn(right));
  }

  public static IExpression ne(String left, String right) {
    return new RelationalExpr(new TableColumn(left), RelationalOp.NE, new TableColumn(right));
  }

  public static IExpression lt(String left, String right) {
    return new RelationalExpr(new TableColumn(left), RelationalOp.LT, new TableColumn(right));
  }

  public static IExpression lte(String left, String right) {
    return new RelationalExpr(new TableColumn(left), RelationalOp.LTE, new TableColumn(right));
  }

  public static IExpression gt(String left, String right) {
    return new RelationalExpr(new TableColumn(left), RelationalOp.GT, new TableColumn(right));
  }

  public static IExpression gte(String left, String right) {
    return new RelationalExpr(new TableColumn(left), RelationalOp.GTE, new TableColumn(right));
  }
}
