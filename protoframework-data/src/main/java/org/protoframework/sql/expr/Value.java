package org.protoframework.sql.expr;

import org.protoframework.sql.ISqlOperation;
import org.protoframework.sql.ISqlValue;

import javax.annotation.Nonnull;
import java.util.List;

import static com.google.common.base.Preconditions.*;

/**
 * 常量：字符串 数值 等
 * <p>
 *
 * @author: yuanwq
 * @date: 2018/7/11
 */
public class Value extends AbstractExpression implements ISqlValue {
  /**
   * @param field 与{@code value}关联的字段，便于确定{@code value}转换SqlValue时的类型
   */
  public static Value of(Object value, String field) {
    return of(value).setField(field);
  }

  public static Value of(Object value) {
    if (value instanceof Value) {
      return ((Value) value);
    }
    return new Value(value);
  }

  private final Object value;
  private String field;

  /**
   * 将构造方法私有化，避免嵌套构造
   */
  private Value(@Nonnull Object value) {
    checkNotNull(value);
    this.value = value;
  }

  @Override
  public String getField() {
    return field;
  }

  /**
   * @param field 与{@code value}关联的字段，便于确定{@code value}转换SqlValue时的类型
   */
  public Value setField(String field) {
    this.field = field;
    return this;
  }

  @Override
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
  public List<ISqlValue> collectSqlValue(@Nonnull List<ISqlValue> sqlValues) {
    sqlValues.add(this);
    return sqlValues;
  }

}
