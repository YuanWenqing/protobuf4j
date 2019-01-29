package protobufframework.orm.sql.expr;

import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import protobufframework.orm.sql.ISqlOperator;
import protobufframework.orm.sql.ISqlValue;
import protobufframework.orm.sql.SqlUtil;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.*;

/**
 * author: yuanwq
 * date: 2018/7/11
 */
@Data
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
  public int comparePrecedence(@Nonnull ISqlOperator outerOp) {
    if (!StringUtils.containsAny(sql, " +-*/><=")) {
      return 1;
    }
    if (sql.startsWith("(") && sql.endsWith(")")) {
      return 1;
    }
    return -1;
  }

}
