package org.protoframework.sql;

import static com.google.common.base.Preconditions.*;

/**
 * 访问者模式解耦具体表达式的sql语句构造逻辑
 * <p>
 *
 * @param <T> 处理的表达式类型
 * @author: yuanwq
 * @date: 2018/7/11
 */
public interface ISqlOperation<T extends IBinaryExpr> {
  String WRAP_LEFT = "(";
  String WRAP_RIGHT = ")";

  /**
   * 构造sql语句中的运算符形式，两边包括必要的空格
   */
  String getOp();

  /**
   * 校验操作符两侧的表达式参数
   */
  default void checkExpression(IExpression left, IExpression right) {
    checkNotNull(left, "left expr is null");
    checkNotNull(right, "right expr is null");
  }
}
