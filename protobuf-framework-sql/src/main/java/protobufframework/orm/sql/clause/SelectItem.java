package protobufframework.orm.sql.clause;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import protobufframework.orm.sql.AbstractSqlObject;
import protobufframework.orm.sql.IExpression;
import protobufframework.orm.sql.ISqlValue;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author: yuanwq
 * @date: 2018/7/11
 */
@Data
@RequiredArgsConstructor
public class SelectItem extends AbstractSqlObject {
  @NonNull
  private final IExpression expression;
  private String alias;

  public SelectItem(IExpression expression, String alias) {
    this(expression);
    this.setAlias(alias);
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
