package org.protoframework.sql.expr;

import org.protoframework.sql.AbstractSqlStatement;
import org.protoframework.sql.IExpression;
import org.protoframework.sql.ISqlOperation;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * 表列
 * <p>
 * @author: yuanwq
 * @date: 2018/7/11
 */
public class TableColumn extends AbstractSqlStatement implements IExpression {
  private final String column;

  public TableColumn(String column) {
    this.column = column;
  }

  public String getColumn() {
    return column;
  }

  @Override
  public int comparePrecedence(@Nonnull ISqlOperation outerOp) {
    return 1;
  }

  @Override
  public StringBuilder toSqlTemplate(@Nonnull StringBuilder sb) {
    return sb.append(column);
  }

  @Override
  public StringBuilder toSolidSql(@Nonnull StringBuilder sb) {
    return sb.append(column);
  }

  @Override
  public List<Object> collectSqlValue(@Nonnull List<Object> collectedValues) {
    return collectedValues;
  }

}
