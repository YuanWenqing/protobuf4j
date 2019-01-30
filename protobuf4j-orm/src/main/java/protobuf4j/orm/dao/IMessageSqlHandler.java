/**
 * author yuanwq, date: 2017年4月27日
 */
package protobuf4j.orm.dao;

/**
 * author yuanwq
 */
public interface IMessageSqlHandler {

  /**
   * 将{@code field}的值{@code value}转换为对应sql类型的值
   *
   * @param field 字段名
   * @param value 字段值
   * @return 转换后sql类型的值
   */
  Object toSqlValue(String field, Object value);

  /**
   * 将sql类型的值{@code sqlValue}转换为{@code field}的值
   *
   * @param field    字段名
   * @param sqlValue sql类型的值
   * @return 转换后{@code field}的字段值
   */
  Object fromSqlValue(String field, Object sqlValue);

}
