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
 * @date: 2018/7/11
 */
public class SelectExpr extends AbstractSqlStatement implements ISqlStatement {

  private final IExpression expression;
  private String alias;

  public SelectExpr(@Nonnull IExpression expression, String alias) {
    this(expression);
    this.setAlias(alias);
  }

  public SelectExpr(@Nonnull IExpression expression) {
    Preconditions.checkNotNull(expression);
    this.expression = expression;
  }

  public IExpression getExpression() {
    return expression;
  }

  public String getAlias() {
    return alias;
  }

  public SelectExpr setAlias(String alias) {
    this.alias = alias;
    return this;
  }

  @Override
  public StringBuilder toSqlTemplate(@Nonnull StringBuilder sb) {
    expression.toSqlTemplate(sb);
    if (StringUtils.isNotBlank(alias)) {
      sb.append(" AS ").append(alias);
    }
    return sb;
  }

  @Override
  public StringBuilder toSolidSql(@Nonnull StringBuilder sb) {
    return toSqlTemplate(sb);
  }

  @Override
  public List<Object> collectSqlValue(@Nonnull List<Object> collectedValues) {
    expression.collectSqlValue(collectedValues);
    return collectedValues;
  }

}
