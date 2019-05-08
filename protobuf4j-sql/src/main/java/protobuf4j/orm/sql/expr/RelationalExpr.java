package protobuf4j.orm.sql.expr;

import protobuf4j.orm.sql.IExpression;
import protobuf4j.orm.sql.ISqlOperator;

import javax.annotation.Nonnull;

/**
 * 关系表达式：{@code = != < <= > >=}
 * <p>
 *
 * author: yuanwq
 * date: 2018/7/11
 */
public class RelationalExpr extends AbstractBinaryExpr<RelationalOp> {

  protected RelationalExpr(IExpression left, @Nonnull RelationalOp op, IExpression right) {
    super(left, op, right);
  }


  @Override
  public int comparePrecedence(@Nonnull ISqlOperator outerOp) {
    // 关系表达式嵌套在逻辑表达式中不需要括号，其他都需要
    if (outerOp instanceof LogicalOp) {
      return 1;
    }
    return -1;
  }

}
