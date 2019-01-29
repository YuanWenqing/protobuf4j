package protobuf4j.orm.sql.clause;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import protobuf4j.orm.sql.AbstractSqlObject;
import protobuf4j.orm.sql.ISqlValue;
import protobuf4j.orm.sql.ITableRef;

import javax.annotation.Nonnull;
import java.util.List;

import static com.google.common.base.Preconditions.*;

/**
 * author: yuanwq
 * date: 2018/7/16
 */
@Data
public class SimpleTable extends AbstractSqlObject implements ITableRef {
  private final String tableName;

  public SimpleTable(String tableName) {
    checkArgument(StringUtils.isNotBlank(tableName));
    this.tableName = tableName.trim();
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
  public List<ISqlValue> collectSqlValue(@Nonnull List<ISqlValue> sqlValues) {
    return sqlValues;
  }

}
