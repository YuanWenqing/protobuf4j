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


}
