package org.protoframework.sql;

/**
 * sqlValue接口，处理value与字段的关联，便于后续value的转换
 *
 * @author: yuanwq
 * @date: 2018/7/16
 */
public interface ISqlValue {
  /**
   * {@code value}对应的field
   */
  String getField();

  /**
   * 原始的value
   */
  Object getValue();
}
