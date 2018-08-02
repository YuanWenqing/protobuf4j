package org.protoframework.sql;

/**
 * 所有sql语句元素的基类，实现了统一的 {@link #toString()}
 * <p>
 *
 * @author: yuanwq
 * @date: 2018/7/15
 */
public abstract class AbstractSqlObject implements ISqlObject {
  @Override
  public String toString() {
    StringBuilder sb = this.toSolidSql(new StringBuilder());
    return String.format("%s{%s}", getClass().getSimpleName(), sb);
  }
}
