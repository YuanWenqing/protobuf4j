package org.protoframework.sql;

/**
 * 所有sql语句元素的基类，实现了统一的 {@link #toString()}
 * <p>
 * author: yuanwq
 * date: 2018/7/15
 */
public abstract class AbstractSqlStatement implements ISqlStatement {
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder(getClass().getSimpleName()).append("{");
    this.toSolidSql(sb);
    sb.append("}");
    return sb.toString();
  }
}
