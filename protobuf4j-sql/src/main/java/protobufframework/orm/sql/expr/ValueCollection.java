package protobufframework.orm.sql.expr;

import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import protobufframework.orm.sql.ISqlOperator;
import protobufframework.orm.sql.ISqlValue;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;

/**
 * 值集合
 *
 * author: yuanwq
 * date: 2018/7/11
 */
@Data
public class ValueCollection extends AbstractExpression {
  /**
   * @return 与{@code value}关联的字段，便于确定{@code value}转换SqlValue时的类型
   */
  private final String field;
  private final List<Object> values;

  /**
   * @param field 与{@code value}关联的字段，便于确定{@code value}转换SqlValue时的类型
   */
  private ValueCollection(Collection<?> values, String field) {
    this.values = ImmutableList.copyOf(values);
    this.field = field;
  }

  /**
   * @param field 与{@code value}关联的字段，便于确定{@code value}转换SqlValue时的类型
   */
  public static ValueCollection of(Collection<?> values, String field) {
    values = Collections2
        .transform(values, v -> ((v instanceof ISqlValue) ? ((ISqlValue) v).getValue() : v));
    return new ValueCollection(values, field);
  }

  public static ValueCollection of(Collection<?> values) {
    return of(values, null);
  }

  public boolean isEmpty() {
    return values.isEmpty();
  }

  @Override
  public int comparePrecedence(@Nonnull ISqlOperator outerOp) {
    return 1;
  }

  @Override
  public StringBuilder toSqlTemplate(@Nonnull StringBuilder sb) {
    sb.append(String.format("(%s)", StringUtils.repeat("?", ",", values.size())));
    return sb;
  }

  @Override
  public StringBuilder toSolidSql(@Nonnull StringBuilder sb) {
    sb.append(String.format("(%s)", StringUtils.join(values, ",")));
    return sb;
  }

  @Override
  public List<ISqlValue> collectSqlValue(@Nonnull List<ISqlValue> sqlValues) {
    for (Object value : values) {
      sqlValues.add(Value.of(value, field));
    }
    return sqlValues;
  }
}
