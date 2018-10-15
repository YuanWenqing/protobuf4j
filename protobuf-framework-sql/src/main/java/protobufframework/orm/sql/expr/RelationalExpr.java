package protobufframework.orm.sql.expr;

import protobufframework.orm.sql.IExpression;
import protobufframework.orm.sql.ISqlOperator;

import javax.annotation.Nonnull;

/**
 * 关系表达式：{@code = != < <= > >=}
 * <p>
 *
 * @author: yuanwq
 * @date: 2018/7/11
 */
public class RelationalExpr extends AbstractBinaryExpr<RelationalOp> {

  protected RelationalExpr(IExpression left, @Nonnull RelationalOp op, IExpression right) {
    super(left, op, right);
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

  @Override
  public int comparePrecedence(@Nonnull ISqlOperator outerOp) {
    // 关系表达式嵌套在逻辑表达式中不需要括号，其他都需要
    if (outerOp instanceof LogicalOp) {
      return 1;
    }
    return -1;
  }

}
