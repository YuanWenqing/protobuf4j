package org.protoframework.sql.clause;

import org.protoframework.sql.ISqlStatement;
import org.protoframework.sql.ITableRef;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * author: yuanwq
 * date: 2018/7/12
 */
public class FromClause implements ISqlStatement {
  private final ITableRef tableRef;

  public FromClause(ITableRef tableRef) {
    this.tableRef = tableRef;
  }

  public ITableRef getTableRef() {
    return tableRef;
  }

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
  public List<Object> collectSqlValue(@Nonnull List<Object> collectedValues) {
    tableRef.collectSqlValue(collectedValues);
    return collectedValues;
  }
}
