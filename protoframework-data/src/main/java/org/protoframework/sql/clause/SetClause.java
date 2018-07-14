package org.protoframework.sql.clause;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.protoframework.sql.ISqlStatement;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * author: yuanwq
 * date: 2018/7/12
 */
public class SetClause implements ISqlStatement {
  private final List<SetExpr> setExprs = Lists.newArrayList();

  public List<SetExpr> getSetExprs() {
    return setExprs;
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
