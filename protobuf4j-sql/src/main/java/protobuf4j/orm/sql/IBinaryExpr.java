package protobuf4j.orm.sql;

/**
 * @param <T> 运算符类型
 * author: yuanwq
 * date: 2018/7/11
 */
public interface IBinaryExpr<T extends ISqlOperator> extends IExpression {
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

}
