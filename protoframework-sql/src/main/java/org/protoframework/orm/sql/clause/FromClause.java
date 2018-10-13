package org.protoframework.orm.sql.clause;

import lombok.Data;
import lombok.NonNull;
import org.protoframework.orm.sql.AbstractSqlObject;
import org.protoframework.orm.sql.ISqlValue;
import org.protoframework.orm.sql.ITableRef;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author: yuanwq
 * @date: 2018/7/12
 */
@Data
public class FromClause extends AbstractSqlObject {
  @NonNull
  private final ITableRef tableRef;

  @Override
  public StringBuilder toSqlTemplate(@Nonnull StringBuilder sb) {
    sb.append("FROM ");
    tableRef.toSqlTemplate(sb);
    return sb;
  }

  @Override
  public StringBuilder toSolidSql(@Nonnull StringBuilder sb) {
    sb.append("FROM ");
    tableRef.toSolidSql(sb);
    return sb;
  }

  @Override
  public List<ISqlValue> collectSqlValue(@Nonnull List<ISqlValue> sqlValues) {
    tableRef.collectSqlValue(sqlValues);
    return sqlValues;
  }

}
