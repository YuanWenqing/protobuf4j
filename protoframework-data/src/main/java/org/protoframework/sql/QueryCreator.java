package org.protoframework.sql;

import org.protoframework.sql.clause.*;
import org.protoframework.sql.expr.TableColumn;
import org.protoframework.sql.expr.Value;

/**
 * 构建sql语句元素的util
 *
 * @author: yuanwq
 * @date: 2018/7/16
 */
public abstract class QueryCreator {

  public static ISqlValue sqlValue(Object value) {
    if (value instanceof ISqlValue) {
      return (ISqlValue) value;
    }
    return Value.of(value);
  }

  public static TableColumn column(String field) {
    return TableColumn.of(field);
  }

  public static SelectClause select() {
    return new SelectClause();
  }

  public static FromClause from(String tableName) {
    return new FromClause(table(tableName));
  }

  public static ITableRef table(String tableName) {
    return new SimpleTable(tableName);
  }

  public static WhereClause where() {
    return new WhereClause();
  }

  public static GroupByClause groupBy() {
    return new GroupByClause();
  }

  public static OrderByClause orderBy() {
    return new OrderByClause();
  }

  public static PaginationClause.Builder pagination(int limit) {
    return PaginationClause.newBuilder(limit);
  }

  public static SetClause set() {
    return new SetClause();
  }
}
