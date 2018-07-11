package org.protoframework.sql;

import javax.annotation.Nonnull;

/**
 * author: yuanwq
 * date: 2018/7/11
 */

public enum RelationalOp implements ISqlOperation<RelationalExpr> {
  EQ("="),
  NE("!="),
  GT(">"),
  GTE(">="),
  LT("<"),
  LTE("<="),
  IS_NULL(" IS NULL", true) {
    @Override
    public void toSqlTemplate(@Nonnull RelationalExpr expr, @Nonnull StringBuilder sb) {
      boolean needWrap = expr.getLeft().comparePrecedence(this) < 0;
      if (needWrap) {
        sb.append(WRAP_LEFT);
      }
      expr.getLeft().toSqlTemplate(sb);
      if (needWrap) {
        sb.append(WRAP_RIGHT);
      }
      sb.append(this.getOp());
      // no right
    }

    @Override
    public void toSolidSql(@Nonnull RelationalExpr expr, @Nonnull StringBuilder sb) {
      boolean needWrap = expr.getLeft().comparePrecedence(this) < 0;
      if (needWrap) {
        sb.append(WRAP_LEFT);
      }
      expr.getLeft().toSolidSql(sb);
      if (needWrap) {
        sb.append(WRAP_RIGHT);
      }
      sb.append(this.getOp());
      // no right
    }
  },
  LIKE(" LIKE ") {
  },
  BETWEEN(" BETWEEN ") {
    @Override
    public void toSqlTemplate(@Nonnull RelationalExpr expr, @Nonnull StringBuilder sb) {
      boolean needWrap = expr.getLeft().comparePrecedence(this) < 0;
      if (needWrap) {
        sb.append(WRAP_LEFT);
      }
      expr.getLeft().toSqlTemplate(sb);
      if (needWrap) {
        sb.append(WRAP_RIGHT);
      }
      sb.append(this.getOp());
      expr.getRight().toSqlTemplate(sb);
    }

    @Override
    public void toSolidSql(@Nonnull RelationalExpr expr, @Nonnull StringBuilder sb) {
      boolean needWrap = expr.getLeft().comparePrecedence(this) < 0;
      if (needWrap) {
        sb.append(WRAP_LEFT);
      }
      expr.getLeft().toSolidSql(sb);
      if (needWrap) {
        sb.append(WRAP_RIGHT);
      }
      sb.append(this.getOp());
      expr.getRight().toSolidSql(sb);
    }
  },
  IN(" IN "),
  NIN(" NOT IN ");

  private final String op;
  private boolean unary = false;

  RelationalOp(String op) {
    this.op = op;
  }

  RelationalOp(String op, boolean unary) {
    this.op = op;
    this.unary = unary;
  }

  @Override
  public String getOp() {
    return op;
  }

  public boolean isUnary() {
    return unary;
  }
}