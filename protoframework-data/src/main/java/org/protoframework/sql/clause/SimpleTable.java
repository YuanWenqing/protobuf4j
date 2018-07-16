package org.protoframework.sql.clause;

import org.apache.commons.lang3.StringUtils;
import org.protoframework.sql.AbstractSqlStatement;
import org.protoframework.sql.ITableRef;

import javax.annotation.Nonnull;
import java.util.List;

import static com.google.common.base.Preconditions.*;

/**
 * @author: yuanwq
 * @date: 2018/7/16
 */
public class SimpleTable extends AbstractSqlStatement implements ITableRef {
  private final String tableName;

  public SimpleTable(String tableName) {
    checkArgument(StringUtils.isNotBlank(tableName));
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
