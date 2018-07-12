package org.protoframework.sql;

import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;

/**
 * 值集合
 * author: yuanwq
 * date: 2018/7/11
 */
public class ValueSet implements IExpression {
  private final List<Object> values;

  public ValueSet(@Nonnull Collection<?> values) {
    this.values = ImmutableList.copyOf(values);
  }

  @Override
  public int comparePrecedence(@Nonnull ISqlOperation outerOp) {
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
  public List<Object> collectSqlValue(@Nonnull List<Object> collectedValues) {
    // TODO: value conversion
    collectedValues.addAll(values);
    return collectedValues;
  }

  @Override
  public String toString() {
    return String.valueOf(values);
  }
}
