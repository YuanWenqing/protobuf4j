package org.protoframework.sql;

/**
 * author: yuanwq
 * date: 2018/7/11
 */

public enum LogicalOp implements ISqlOperation<LogicalExpr> {
  AND(" AND "),
  OR(" OR "),
  XOR(" XOR ");

  private final String op;

  LogicalOp(String op) {
    this.op = op;
  }

  @Override
  public String getOp() {
    return op;
  }

}
