/**
 * author yuanwq, date: 2017年4月27日
 */
package protobuf4j.orm.dao;

/**
 * author yuanwq
 */
public interface ISqlConverter<T> {
  /**
   * 默认的table name
   */
  String tableName(Class<? extends T> beanClass);

  /**
   * 将{@code beanClass}实例的字段值{@code value}转换为对应sql类型的值
   *
   * @param beanClass 处理转换的bean实例的类型
   * @param field     {@code beanClass}的字段名
   * @param value     {@code beanClass}实例的{@code field}字段的值
   * @return 转换后sql类型的值
   */
  <B extends T> Object toSqlValue(Class<B> beanClass, String field, Object value);

  /**
   * 将sql类型的值{@code sqlValue}转换为{@code beanClass}中{@code field}字段的类型的值
   *
   * @param beanClass 处理转换的bean实例的类型
   * @param field     {@code beanClass}的字段名
   * @param sqlValue  sql类型的值
   * @return 转换后{@code beanClass}中{@code field}字段的值
   */
  <B extends T> Object fromSqlValue(Class<B> beanClass, String field, Object sqlValue);

}
