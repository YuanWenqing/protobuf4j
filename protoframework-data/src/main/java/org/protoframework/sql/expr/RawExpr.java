package org.protoframework.sql.expr;

import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.StringUtils;
import org.protoframework.sql.ISqlOperation;
import org.protoframework.sql.ISqlValue;
import org.protoframework.sql.SqlUtil;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.*;

/**
 * @author: yuanwq
 * @date: 2018/7/11
 */
public class RawExpr extends AbstractExpression {

  private final String sql;
  private final List<Object> values;

  public RawExpr(@Nonnull String sql) {
    this(sql, Collections.emptyList());
  }

  public RawExpr(@Nonnull String sql, @Nonnull Collection<?> values) {
    checkArgument(StringUtils.isNotBlank(sql));
    this.sql = StringUtils.trim(sql);
    this.values = ImmutableList.copyOf(values);
  }

  public String getSql() {
    return sql;
  }

  public List<Object> getValues() {
    return values;
  }

  @Override
  public StringBuilder toSqlTemplate(@Nonnull StringBuilder sb) {
    return sb.append(sql);
  }

  @Override
  public StringBuilder toSolidSql(@Nonnull StringBuilder sb) {
    sb.append(SqlUtil.replaceParamHolder(sql, values));
    return sb;
  }

  @Override
  public List<ISqlValue> collectSqlValue(@Nonnull List<ISqlValue> sqlValues) {
    sqlValues.addAll(Collections2.transform(values, Value::of));
    return sqlValues;
  }

  @Override
  public int comparePrecedence(@Nonnull ISqlOperation outerOp) {
    if (!StringUtils.containsAny(sql, " +-*/><=")) {
      return 1;
    }
    if (sql.startsWith("(") && sql.endsWith(")")) {
      return 1;
    }
    return -1;
  }

}
