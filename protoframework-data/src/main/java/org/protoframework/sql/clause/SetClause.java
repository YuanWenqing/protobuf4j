package org.protoframework.sql.clause;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.protoframework.sql.IExpression;
import org.protoframework.sql.ISqlStatement;
import org.protoframework.sql.expr.ConstValue;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

/**
 * author: yuanwq
 * date: 2018/7/12
 */
public class SetClause implements ISqlStatement {
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

  public SetClause setColumn(String column, IExpression valueExpr) {
    return addSetExpr(new SetExpr(column, valueExpr));
  }

  public SetClause setColumn(String column, Object value) {
    return setColumn(column, new ConstValue(value));
  }

  @Override
  public StringBuilder toSqlTemplate(@Nonnull StringBuilder sb) {
    Preconditions.checkArgument(setExprs.isEmpty(), "nothing to set");
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
  public List<Object> collectSqlValue(@Nonnull List<Object> collectedValues) {
    for (SetExpr setExpr : setExprs) {
      setExpr.collectSqlValue(collectedValues);
    }
    return collectedValues;
  }
}
