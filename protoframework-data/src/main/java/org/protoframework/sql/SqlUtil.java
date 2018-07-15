package org.protoframework.sql;

import org.protoframework.sql.clause.SelectExpr;
import org.protoframework.sql.expr.RawExpr;

/**
 * author: yuanwq
 * date: 2018/7/15
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

}
