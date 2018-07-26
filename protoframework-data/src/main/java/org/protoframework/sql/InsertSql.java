package org.protoframework.sql;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.protoframework.sql.expr.Value;

import javax.annotation.Nonnull;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author: yuanwq
 * @date: 2018/7/26
 */
public class InsertSql extends AbstractSqlObject {
  private final ITableRef table;
  private final LinkedHashMap<String, IExpression> insertFields = Maps.newLinkedHashMap();

  public InsertSql(@Nonnull ITableRef table) {
    this.table = table;
  }

  public ITableRef getTable() {
    return table;
  }

  public InsertSql addField(String field, Object value) {
    return addField(field, Value.of(value, field));
  }

  public InsertSql addField(String field, IExpression value) {
    insertFields.put(field, value);
    return this;
  }

  @Override
  public StringBuilder toSqlTemplate(@Nonnull StringBuilder sb) {
    sb.append("INSERT INTO ");
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
    sb.append("INSERT INTO ");
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
