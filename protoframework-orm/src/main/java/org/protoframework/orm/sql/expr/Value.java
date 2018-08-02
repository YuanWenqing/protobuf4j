package org.protoframework.orm.sql.expr;

import org.protoframework.orm.sql.ISqlOperator;
import org.protoframework.orm.sql.ISqlValue;

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
  private final Object value;
  private final String field;

  /**
   * 将构造方法私有化，避免嵌套构造
   */
  private Value(@Nonnull Object value, String field) {
    checkNotNull(value);
    this.value = value;
    this.field = field;
  }

  /**
   * @param field 与{@code value}关联的字段，便于确定{@code value}转换SqlValue时的类型
   */
  public static Value of(Object value, String field) {
    if (value instanceof ISqlValue) {
      value = ((ISqlValue) value).getValue();
    }
    return new Value(value, field);
  }

  public static Value of(Object value) {
    return of(value, null);
  }

  @Override
  public String getField() {
    return field;
  }

  @Override
  public Object getValue() {
    return value;
  }

  @Override
  public int comparePrecedence(@Nonnull ISqlOperator outerOp) {
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

  @Override
  public String toString() {
    return "Value{" + "value=" + value + ", field='" + field + '\'' + '}';
  }
}
