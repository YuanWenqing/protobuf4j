package org.protoframework.sql;

import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.*;

/**
 * @author: yuanwq
 * @date: 2018/7/11
 */
public class RawSql extends AbstractSqlStatement {

  private final String sql;
  private final List<Object> values;

  public RawSql(@Nonnull String sql) {
    this(sql, Collections.emptyList());
  }

  public RawSql(@Nonnull String sql, @Nonnull Collection<?> values) {
    checkArgument(StringUtils.isNotBlank(sql));
    this.sql = sql;
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
    sqlValues.addAll(Collections2.transform(values, QueryCreator::sqlValue));
    return sqlValues;
  }

}
