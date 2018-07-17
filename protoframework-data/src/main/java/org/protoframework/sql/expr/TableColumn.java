package org.protoframework.sql.expr;

import org.apache.commons.lang3.StringUtils;
import org.protoframework.sql.ISqlOperation;
import org.protoframework.sql.ISqlValue;

import javax.annotation.Nonnull;
import java.util.List;

import static com.google.common.base.Preconditions.*;

/**
 * 表列
 * <p>
 *
 * @author: yuanwq
 * @date: 2018/7/11
 */
public class TableColumn extends AbstractExpression {
  public static TableColumn of(String column) {
    return new TableColumn(column);
  }

  private final String column;

  private TableColumn(String column) {
    checkArgument(StringUtils.isNotBlank(column));
    this.column = column;
  }

  public String getColumn() {
    return column;
  }

  @Override
  public int comparePrecedence(@Nonnull ISqlOperation outerOp) {
    return 1;
  }

  @Override
  public StringBuilder toSqlTemplate(@Nonnull StringBuilder sb) {
    return sb.append(column);
  }

  @Override
  public StringBuilder toSolidSql(@Nonnull StringBuilder sb) {
    return sb.append(column);
  }

  @Override
  public List<ISqlValue> collectSqlValue(@Nonnull List<ISqlValue> sqlValues) {
    return sqlValues;
  }

}
