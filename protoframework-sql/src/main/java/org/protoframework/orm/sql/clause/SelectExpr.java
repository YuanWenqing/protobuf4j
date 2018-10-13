package org.protoframework.orm.sql.clause;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.protoframework.orm.sql.AbstractSqlObject;
import org.protoframework.orm.sql.IExpression;
import org.protoframework.orm.sql.ISqlValue;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author: yuanwq
 * @date: 2018/7/11
 */
public class SelectExpr extends AbstractSqlObject {

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
    expression.toSolidSql(sb);
    if (StringUtils.isNotBlank(alias)) {
      sb.append(" AS ").append(alias);
    }
    return sb;
  }

  @Override
  public List<ISqlValue> collectSqlValue(@Nonnull List<ISqlValue> sqlValues) {
    expression.collectSqlValue(sqlValues);
    return sqlValues;
  }

}
