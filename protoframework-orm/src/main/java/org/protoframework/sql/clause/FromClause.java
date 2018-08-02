package org.protoframework.sql.clause;

import com.google.common.base.Preconditions;
import org.protoframework.sql.AbstractSqlObject;
import org.protoframework.sql.ISqlValue;
import org.protoframework.sql.ITableRef;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author: yuanwq
 * @date: 2018/7/12
 */
public class FromClause extends AbstractSqlObject {
  private final ITableRef tableRef;

  public FromClause(@Nonnull ITableRef tableRef) {
    Preconditions.checkNotNull(tableRef);
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
  public List<ISqlValue> collectSqlValue(@Nonnull List<ISqlValue> sqlValues) {
    tableRef.collectSqlValue(sqlValues);
    return sqlValues;
  }

}
