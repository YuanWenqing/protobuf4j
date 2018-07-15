package org.protoframework.sql;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * @author: yuanwq
 * @date: 2018/7/11
 *
 * @param <T></T> 运算符类型
 */
public interface IBinaryExpr<T extends ISqlOperation> extends IExpression {
  /**
   * 左表达式
   */
  IExpression getLeft();

  /**
   * 右表达式
   */
  IExpression getRight();

  /**
   * 运算符
   */
  T getOp();

  @Override
  default int comparePrecedence(@Nonnull ISqlOperation outerOp) {
    // 默认：运算符不同，就需要包裹括号
    return Objects.equals(getOp(), outerOp) ? 0 : -1;
  }
}
