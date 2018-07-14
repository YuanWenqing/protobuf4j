package org.protoframework.sql.expr;

import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.StringUtils;
import org.protoframework.sql.IExpression;
import org.protoframework.sql.ISqlOperation;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * author: yuanwq
 * date: 2018/7/11
 */
public class RawExpr implements IExpression {

  private final String rawSqlExpr;
  private final List<Object> values;

  public RawExpr(@Nonnull String rawSqlExpr) {
    this(rawSqlExpr, Collections.emptyList());
  }

  public RawExpr(@Nonnull String rawSqlExpr, @Nonnull Collection<?> values) {
    this.rawSqlExpr = rawSqlExpr;
    this.values = ImmutableList.copyOf(values);
  }

  public String getRawSqlExpr() {
    return rawSqlExpr;
  }

  public List<Object> getValues() {
    return values;
  }

  @Override
  public int comparePrecedence(@Nonnull ISqlOperation outerOp) {
    return 1;
  }

  @Override
  public StringBuilder toSqlTemplate(@Nonnull StringBuilder sb) {
    return sb.append(rawSqlExpr);
  }

  @Override
  public StringBuilder toSolidSql(@Nonnull StringBuilder sb) {
    Iterator<Object> iter = values.iterator();
    for (String part : StringUtils.splitPreserveAllTokens(rawSqlExpr, "?")) {
      sb.append(part);
      if (iter.hasNext()) {
        sb.append(iter.next());
      } else {
        sb.append("?");
      }
    }
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
    return toSolidSql(new StringBuilder()).toString();
  }
}
