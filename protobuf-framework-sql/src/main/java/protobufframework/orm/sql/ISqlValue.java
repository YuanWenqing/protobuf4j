package protobufframework.orm.sql;

/**
 * sqlValue接口，处理value与字段的关联，便于后续value的转换
 *
 * @author: yuanwq
 * @date: 2018/7/16
 */
public interface ISqlValue {
  /**
   * {@code value}对应的field，便于确定{@code value}转换SqlValue时的类型
   * <p>
   * Warn：若为空，则直接取{@code value}
   */
  String getField();

  /**
   * 原始的value
   */
  Object getValue();
}
