package protobuf4j.orm.sql.expr;

import protobuf4j.orm.sql.IExpression;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Iterator;

/**
 * 逻辑表达式：AND OR XOR
 * <p>
 * <p>
 * author: yuanwq
 * date: 2018/7/11
 */
public class LogicalExpr extends AbstractBinaryExpr<LogicalOp> {

  protected LogicalExpr(IExpression left, @Nonnull LogicalOp op, IExpression right) {
    super(left, op, right);
  }

  public static LogicalExpr and(IExpression left, IExpression right) {
    return new LogicalExpr(left, LogicalOp.AND, right);
  }

  public static LogicalExpr or(IExpression left, IExpression right) {
    return new LogicalExpr(left, LogicalOp.OR, right);
  }

  public static LogicalExpr xor(IExpression left, IExpression right) {
    return new LogicalExpr(left, LogicalOp.XOR, right);
  }

  public static LogicalExpr not(IExpression expr) {
    return new LogicalExpr(null, LogicalOp.NOT, expr);
  }

  public static IExpression and(Collection<IExpression> conds) {
    if (conds.isEmpty()) {
      return null;
    }
    Iterator<IExpression> iterator = conds.iterator();
    IExpression cond = iterator.next();
    while (iterator.hasNext()) {
      cond = cond.and(iterator.next());
    }
    return cond;
  }

  public static IExpression or(Collection<IExpression> conds) {
    if (conds.isEmpty()) {
      return null;
    }
    Iterator<IExpression> iterator = conds.iterator();
    IExpression cond = iterator.next();
    while (iterator.hasNext()) {
      cond = cond.or(iterator.next());
    }
    return cond;
  }

  public static IExpression xor(Collection<IExpression> conds) {
    if (conds.isEmpty()) {
      return null;
    }
    Iterator<IExpression> iterator = conds.iterator();
    IExpression cond = iterator.next();
    while (iterator.hasNext()) {
      cond = cond.xor(iterator.next());
    }
    return cond;
  }
}
