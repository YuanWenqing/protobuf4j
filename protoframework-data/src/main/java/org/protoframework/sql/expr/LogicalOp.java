package org.protoframework.sql.expr;

import org.protoframework.sql.ISqlOperation;

import javax.annotation.Nonnull;

/**
 * author: yuanwq
 * date: 2018/7/11
 */

public enum LogicalOp implements ISqlOperation<LogicalExpr> {
  AND(" AND "),
  OR(" OR "),
  XOR(" XOR "),
  NOT("NOT ") {
    @Override
    public void toSqlTemplate(@Nonnull LogicalExpr expr, @Nonnull StringBuilder sb) {
      // no left
      sb.append(this.getOp());
      boolean needWrap = expr.getRight().comparePrecedence(this) < 0;
      if (needWrap) {
        sb.append(WRAP_LEFT);
      }
      expr.getRight().toSqlTemplate(sb);
      if (needWrap) {
        sb.append(WRAP_RIGHT);
      }
    }

    @Override
    public void toSolidSql(@Nonnull LogicalExpr expr, @Nonnull StringBuilder sb) {
      // no left
      sb.append(this.getOp());
      boolean needWrap = expr.getRight().comparePrecedence(this) < 0;
      if (needWrap) {
        sb.append(WRAP_LEFT);
      }
      expr.getRight().toSolidSql(sb);
      if (needWrap) {
        sb.append(WRAP_RIGHT);
      }
    }
  };

  private final String op;

  LogicalOp(String op) {
    this.op = op;
  }

  @Override
  public String getOp() {
    return op;
  }

}
