package org.protoframework.sql;

import org.apache.commons.lang3.StringUtils;
import org.protoframework.sql.clause.SelectExpr;
import org.protoframework.sql.expr.AbstractExpression;
import org.protoframework.sql.expr.RawExpr;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * 构建sql语句元素的util
 *
 * @author: yuanwq
 * @date: 2018/7/15
 */
public class SqlUtil {
  public static final SelectExpr SELECT_STAR = new SelectExpr(new RawExpr("*")) {
    @Override
    public SelectExpr setAlias(String alias) {
      throw new UnsupportedOperationException("cannot set alias for STAR `*`");
    }
  };

  public static final SelectExpr SELECT_COUNT = new SelectExpr(new RawExpr("COUNT(1)")) {
    @Override
    public SelectExpr setAlias(String alias) {
      throw new UnsupportedOperationException("cannot set alias for DEFAULT `COUNT(1)`");
    }
  };

  public static final IExpression aggregateWrap(String aggregateFunc, IExpression expr) {
    return new AbstractExpression() {
      @Override
      public int comparePrecedence(@Nonnull ISqlOperation outerOp) {
        return 1;
      }

      @Override
      public StringBuilder toSqlTemplate(@Nonnull StringBuilder sb) {
        sb.append(aggregateFunc).append("(");
        expr.toSqlTemplate(sb);
        sb.append(")");
        return sb;
      }

      @Override
      public StringBuilder toSolidSql(@Nonnull StringBuilder sb) {
        sb.append(aggregateFunc).append("(");
        expr.toSolidSql(sb);
        sb.append(")");
        return sb;
      }

      @Override
      public List<Object> collectSqlValue(@Nonnull List<Object> collectedValues) {
        expr.collectSqlValue(collectedValues);
        return collectedValues;
      }
    };
  }

  /**
   * 将{@code sqlTemplate}中的{@code "?"}按顺序使用{@code values}中的值进行替换，若values不足，则保留{@code "?"}
   */
  public static String replaceParamHolder(String sqlTemplate, Collection<?> values) {
    StringBuilder sb = new StringBuilder();
    Iterator<?> iter = values.iterator();
    for (String part : StringUtils.splitPreserveAllTokens(sqlTemplate, "?")) {
      sb.append(part);
      if (iter.hasNext()) {
        sb.append(iter.next());
      } else {
        sb.append("?");
      }
    }
    return sb.toString();
  }
}
