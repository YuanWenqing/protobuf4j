package protobuf4j.orm.sql;

import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import protobuf4j.orm.sql.expr.Value;

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
public class RawSql extends AbstractSqlObject implements ISqlStatement {

  private final String sql;
  private final List<Object> values;

  public RawSql(String sql) {
    this(sql, Collections.emptyList());
  }

  public RawSql(String sql, Collection<?> values) {
    checkArgument(StringUtils.isNotBlank(sql));
    this.sql = sql;
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

}
