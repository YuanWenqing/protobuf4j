package protobufframework.orm.sql;

import com.google.common.collect.Maps;
import lombok.Data;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import protobufframework.orm.sql.expr.Value;

import javax.annotation.Nonnull;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * author: yuanwq
 * date: 2018/7/26
 */
@Data
public class InsertSql extends AbstractSqlObject implements ISqlStatement {
  @NonNull
  private final ITableRef table;
  private final LinkedHashMap<String, IExpression> insertFields = Maps.newLinkedHashMap();
  private boolean ignore = false;

  public InsertSql addValue(String field, Object value) {
    return addExpression(field, Value.of(value, field));
  }

  public InsertSql addExpression(String field, IExpression expression) {
    insertFields.put(field, expression);
    return this;
  }

  @Override
  public StringBuilder toSqlTemplate(@Nonnull StringBuilder sb) {
    if (isIgnore()) {
      sb.append("INSERT IGNORE INTO ");
    } else {
      sb.append("INSERT INTO ");
    }
    table.toSqlTemplate(sb);
    sb.append(" (").append(StringUtils.join(insertFields.keySet(), ",")).append(") VALUES (");
    boolean first = true;
    for (IExpression value : insertFields.values()) {
      if (first) {
        first = false;
      } else {
        sb.append(",");
      }
      value.toSqlTemplate(sb);
    }
    sb.append(")");
    return sb;
  }

  @Override
  public StringBuilder toSolidSql(@Nonnull StringBuilder sb) {
    if (isIgnore()) {
      sb.append("INSERT IGNORE INTO ");
    } else {
      sb.append("INSERT INTO ");
    }
    table.toSolidSql(sb);
    sb.append(" (").append(StringUtils.join(insertFields.keySet(), ",")).append(") VALUES (");
    boolean first = true;
    for (IExpression value : insertFields.values()) {
      if (first) {
        first = false;
      } else {
        sb.append(",");
      }
      value.toSolidSql(sb);
    }
    sb.append(")");
    return sb;
  }

  @Override
  public List<ISqlValue> collectSqlValue(@Nonnull List<ISqlValue> sqlValues) {
    table.collectSqlValue(sqlValues);
    insertFields.values().forEach(v -> v.collectSqlValue(sqlValues));
    return sqlValues;
  }
}
