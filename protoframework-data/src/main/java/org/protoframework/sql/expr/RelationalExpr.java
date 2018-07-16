package org.protoframework.sql.expr;

import org.protoframework.sql.IExpression;

import javax.annotation.Nonnull;

/**
 * 关系表达式：{@code = != < <= > >=}
 * <p>
 *
 * @author: yuanwq
 * @date: 2018/7/11
 */
public class RelationalExpr extends AbstractBinaryExpr<RelationalOp> {

  public RelationalExpr(IExpression left, @Nonnull RelationalOp op, IExpression right) {
    super(left, op, right);
  }

}
