package org.protoframework.sql.expr;

import org.protoframework.sql.AbstractSqlStatement;
import org.protoframework.sql.IExpression;

/**
 * 表达式基类，实现一些同一的方法
 *
 * @author: yuanwq
 * @date: 2018/7/16
 */
public abstract class AbstractExpression extends AbstractSqlStatement implements IExpression {
  @Override
  public IExpression and(IExpression right) {
    return LogicalExpr.and(this, right);
  }

  @Override
  public IExpression or(IExpression right) {
    return LogicalExpr.or(this, right);
  }

  @Override
  public IExpression xor(IExpression right) {
    return LogicalExpr.xor(this, right);
  }

  @Override
  public IExpression not() {
    return LogicalExpr.not(this);
  }
}
