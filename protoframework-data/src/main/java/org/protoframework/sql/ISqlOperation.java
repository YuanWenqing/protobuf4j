package org.protoframework.sql;

import javax.annotation.Nonnull;

/**
 * 访问者模式解耦具体表达式的sql语句构造逻辑
 * <p>
 * author: yuanwq
 * date: 2018/7/11
 *
 * @param <T> 处理的表达式类型
 */
public interface ISqlOperation<T extends IBinaryExpr> {
  String WRAP_LEFT = "(";
  String WRAP_RIGHT = ")";

  /**
   * 构造sql语句中的操作符形式，两边包括必要的空格
   */
  String getOp();

  default void toSqlTemplate(@Nonnull T expr, @Nonnull StringBuilder sb) {
    boolean needWrap = expr.getLeft().comparePrecedence(this) < 0;
    if (needWrap) {
      sb.append(WRAP_LEFT);
    }
    expr.getLeft().toSqlTemplate(sb);
    if (needWrap) {
      sb.append(WRAP_RIGHT);
    }
    sb.append(this.getOp());
    needWrap = expr.getRight().comparePrecedence(this) < 0;
    if (needWrap) {
      sb.append(WRAP_LEFT);
    }
    expr.getRight().toSqlTemplate(sb);
    if (needWrap) {
      sb.append(WRAP_RIGHT);
    }
  }

  default void toSolidSql(@Nonnull T expr, @Nonnull StringBuilder sb) {
    boolean needWrap = expr.getLeft().comparePrecedence(this) < 0;
    if (needWrap) {
      sb.append(WRAP_LEFT);
    }
    expr.getLeft().toSolidSql(sb);
    if (needWrap) {
      sb.append(WRAP_RIGHT);
    }
    sb.append(this.getOp());
    needWrap = expr.getRight().comparePrecedence(this) < 0;
    if (needWrap) {
      sb.append(WRAP_LEFT);
    }
    expr.getRight().toSolidSql(sb);
    if (needWrap) {
      sb.append(WRAP_RIGHT);
    }
  }

}
