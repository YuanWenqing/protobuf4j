package org.protoframework.sql;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * 常量：字符串 数值 等
 * <p>
 * author: yuanwq
 * date: 2018/7/11
 */
public class ConstValue implements IExpression, ISqlStatement {
  private final Object value;

  public ConstValue(Object value) {
    this.value = value;
  }

  public Object getValue() {
    return value;
  }

  @Override
  public int comparePrecedence(@Nonnull ISqlOperation outerOp) {
    return 1;
  }

  @Override
  public StringBuilder toSqlTemplate(@Nonnull StringBuilder sb) {
    return sb.append("?");
  }

  @Override
  public StringBuilder toSolidSql(@Nonnull StringBuilder sb) {
    if (value instanceof String) {
      sb.append("'").append(String.valueOf(value).replace("'", "\\'")).append("'");
    } else {
      sb.append(value);
    }
    return sb;
  }

  @Override
  public List<Object> collectSqlValue(@Nonnull List<Object> collectedValues) {
    collectedValues.add(value);
    return collectedValues;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }
}
