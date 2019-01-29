package protobuf4j.orm.sql;

import javax.annotation.Nonnull;

/**
 * 表达式
 *
 * author: yuanwq
 * date: 2018/7/11
 */
public interface IExpression extends ISqlObject {

  /**
   * 比较该表达式与外部运算符{@code outerOp}的优先级
   * <p>
   * 若表达式优先级较低，则需要在外部使用括号包裹
   *
   * @param outerOp 外部运算符
   * @return 1: 表达式优先级高于外部运算符；0：相同；-1：表达式优先级低于外部运算符
   */
  int comparePrecedence(@Nonnull ISqlOperator outerOp);

  IExpression and(IExpression right);

  IExpression or(IExpression right);

  IExpression xor(IExpression right);

  IExpression not();
}
