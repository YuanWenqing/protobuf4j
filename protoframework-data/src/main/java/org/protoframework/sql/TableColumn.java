package org.protoframework.sql;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * 表列
 * <p>
 * author: yuanwq
 * date: 2018/7/11
 */
public class TableColumn implements IExpression, ISqlStatement {
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

  @Override
  public String toString() {
    return column;
  }
}
