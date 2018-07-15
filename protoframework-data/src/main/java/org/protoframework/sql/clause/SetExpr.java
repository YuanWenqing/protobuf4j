package org.protoframework.sql.clause;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.protoframework.sql.AbstractSqlStatement;
import org.protoframework.sql.IExpression;
import org.protoframework.sql.ISqlStatement;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author: yuanwq
 * @date: 2018/7/14
 */
public class SetExpr extends AbstractSqlStatement implements ISqlStatement {
  private final String column;
  private final IExpression valueExpr;

  public SetExpr(@Nonnull String column, @Nonnull IExpression valueExpr) {
    Preconditions.checkArgument(StringUtils.isNotBlank(column));
    Preconditions.checkNotNull(valueExpr);
    this.column = column;
    this.valueExpr = valueExpr;
  }

  public String getColumn() {
    return column;
  }

  public IExpression getValueExpr() {
    return valueExpr;
  }

  @Override
  public StringBuilder toSqlTemplate(@Nonnull StringBuilder sb) {
    sb.append(column).append("=");
    valueExpr.toSqlTemplate(sb);
    return sb;
  }

  @Override
  public StringBuilder toSolidSql(@Nonnull StringBuilder sb) {
    sb.append(column).append("=");
    valueExpr.toSolidSql(sb);
    return sb;
  }

  @Override
  public List<Object> collectSqlValue(@Nonnull List<Object> collectedValues) {
    valueExpr.collectSqlValue(collectedValues);
    return collectedValues;
  }
}
