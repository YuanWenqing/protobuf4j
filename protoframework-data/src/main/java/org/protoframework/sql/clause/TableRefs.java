package org.protoframework.sql.clause;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.protoframework.sql.AbstractSqlStatement;
import org.protoframework.sql.ITableRef;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author: yuanwq
 * @date: 2018/7/12
 */
public abstract class TableRefs {
  public static ITableRef of(String tableName) {
    return new SimpleTable(tableName);
  }

  private static class SimpleTable extends AbstractSqlStatement implements ITableRef {
    private final String tableName;

    public SimpleTable(String tableName) {
      Preconditions.checkArgument(StringUtils.isNotBlank(tableName));
      this.tableName = tableName;
    }

    @Override
    public String getTableName() {
      return tableName;
    }

    @Override
    public StringBuilder toSqlTemplate(@Nonnull StringBuilder sb) {
      return sb.append(tableName);
    }

    @Override
    public StringBuilder toSolidSql(@Nonnull StringBuilder sb) {
      return sb.append(tableName);
    }

    @Override
    public List<Object> collectSqlValue(@Nonnull List<Object> collectedValues) {
      return collectedValues;
    }

  }
}
