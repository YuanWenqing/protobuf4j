package protobuf4j.orm.sql.expr;

import protobuf4j.orm.sql.IExpression;

import java.util.Collection;
import java.util.Iterator;

/**
 * Author: yuanwq
 * Date: 2019/5/8
 */
public abstract class Expressions {
  private Expressions() {
  }

  public static ArithmeticExpr add(IExpression left, IExpression right) {
    return new ArithmeticExpr(left, ArithmeticOp.ADD, right);
  }

  public static ArithmeticExpr subtract(IExpression left, IExpression right) {
    return new ArithmeticExpr(left, ArithmeticOp.SUBTRACT, right);
  }

  public static ArithmeticExpr multiply(IExpression left, IExpression right) {
    return new ArithmeticExpr(left, ArithmeticOp.MULTIPLY, right);
  }

  public static ArithmeticExpr divide(IExpression left, IExpression right) {
    return new ArithmeticExpr(left, ArithmeticOp.DIVIDE, right);
  }

  public static ArithmeticExpr divRound(IExpression left, IExpression right) {
    return new ArithmeticExpr(left, ArithmeticOp.DIV_ROUND, right);
  }

  public static ArithmeticExpr mod(IExpression left, IExpression right) {
    return new ArithmeticExpr(left, ArithmeticOp.MOD, right);
  }

  public static RelationalExpr eq(IExpression left, IExpression right) {
    return new RelationalExpr(left, RelationalOp.EQ, right);
  }

  public static RelationalExpr ne(IExpression left, IExpression right) {
    return new RelationalExpr(left, RelationalOp.NE, right);
  }

  public static RelationalExpr lt(IExpression left, IExpression right) {
    return new RelationalExpr(left, RelationalOp.LT, right);
  }

  public static RelationalExpr lte(IExpression left, IExpression right) {
    return new RelationalExpr(left, RelationalOp.LTE, right);
  }

  public static RelationalExpr gt(IExpression left, IExpression right) {
    return new RelationalExpr(left, RelationalOp.GT, right);
  }

  public static RelationalExpr gte(IExpression left, IExpression right) {
    return new RelationalExpr(left, RelationalOp.GTE, right);
  }

  public static RelationalExpr isNull(IExpression left) {
    return new RelationalExpr(left, RelationalOp.IS_NULL, null);
  }

  public static RelationalExpr isNotNull(IExpression left) {
    return new RelationalExpr(left, RelationalOp.IS_NOT_NULL, null);
  }

  public static RelationalExpr like(IExpression left, Value value) {
    return new RelationalExpr(left, RelationalOp.LIKE, value);
  }

  public static RelationalExpr between(IExpression left, IExpression min, IExpression max) {
    BetweenExpr betweenExpr = new BetweenExpr(min, max);
    return new RelationalExpr(left, RelationalOp.BETWEEN, betweenExpr);
  }

  public static RelationalExpr between(IExpression left, BetweenExpr betweenExpr) {
    return new RelationalExpr(left, RelationalOp.BETWEEN, betweenExpr);
  }

  public static RelationalExpr in(IExpression left, ValueCollection valueCollection) {
    return new RelationalExpr(left, RelationalOp.IN, valueCollection);
  }

  public static RelationalExpr nin(IExpression left, ValueCollection valueCollection) {
    return new RelationalExpr(left, RelationalOp.NIN, valueCollection);
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
