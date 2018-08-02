package org.protoframework.orm.sql.clause;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.protoframework.orm.sql.AbstractSqlObject;
import org.protoframework.orm.sql.IExpression;
import org.protoframework.orm.sql.ISqlValue;
import org.protoframework.orm.sql.expr.Column;
import org.protoframework.orm.sql.expr.Value;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

/**
 * @author: yuanwq
 * @date: 2018/7/12
 */
public class SetClause extends AbstractSqlObject {
  private final List<SetExpr> setExprs = Lists.newArrayList();

  public List<SetExpr> getSetExprs() {
    return Collections.unmodifiableList(setExprs);
  }

  public boolean isEmpty() {
    return setExprs.isEmpty();
  }

  public SetClause addSetExpr(SetExpr setExpr) {
    this.setExprs.add(setExpr);
    return this;
  }

  public SetClause setExpr(String column, IExpression valueExpr) {
    return addSetExpr(new SetExpr(Column.of(column), valueExpr));
  }

  public SetClause setValue(String column, Object value) {
    return setExpr(column, Value.of(value, column));
  }

  public SetClause setColumn(String column, String other) {
    return setExpr(column, Column.of(other));
  }

  @Override
  public StringBuilder toSqlTemplate(@Nonnull StringBuilder sb) {
    Preconditions.checkArgument(!setExprs.isEmpty(), "nothing to set");
    sb.append("SET ");
    boolean first = true;
    for (SetExpr setExpr : setExprs) {
      if (first) {
        first = false;
      } else {
        sb.append(",");
      }
      setExpr.toSqlTemplate(sb);
    }
    return sb;
  }

  @Override
  public StringBuilder toSolidSql(@Nonnull StringBuilder sb) {
    sb.append("SET ");
    boolean first = true;
    for (SetExpr setExpr : setExprs) {
      if (first) {
        first = false;
      } else {
        sb.append(",");
      }
      setExpr.toSolidSql(sb);
    }
    return sb;
  }

  @Override
  public List<ISqlValue> collectSqlValue(@Nonnull List<ISqlValue> sqlValues) {
    for (SetExpr setExpr : setExprs) {
      setExpr.collectSqlValue(sqlValues);
    }
    return sqlValues;
  }
}
