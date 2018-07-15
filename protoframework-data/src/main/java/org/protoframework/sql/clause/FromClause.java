package org.protoframework.sql.clause;

import com.google.common.base.Preconditions;
import org.protoframework.sql.AbstractSqlStatement;
import org.protoframework.sql.ISqlStatement;
import org.protoframework.sql.ITableRef;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * author: yuanwq
 * date: 2018/7/12
 */
public class FromClause extends AbstractSqlStatement implements ISqlStatement {
  private final ITableRef tableRef;

  public FromClause(@Nonnull ITableRef tableRef) {
    Preconditions.checkNotNull(tableRef);
    this.tableRef = tableRef;
  }

  public static FromClause from(String tableName) {
    return new FromClause(TableRefs.of(tableName));
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
