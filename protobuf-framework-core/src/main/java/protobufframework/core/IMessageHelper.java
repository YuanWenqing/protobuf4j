package protobufframework.core;

import java.util.Map;
import java.util.Set;

/**
 * 通过反射处理bean的辅助类
 *
 * author: yuanwq
 * date: 2018/7/2
 */
public interface IMessageHelper<T> {

  /**
   * @return bean的类型
   */
  Class<? extends T> getType();

  /**
   * @return bean的默认值
   */
  T defaultValue();

  /**
   * message是否为空
   * <p>
   * message为空=所有字段未set
   *
   * @return true=null or equals to default instance
   * @see #isFieldSet(T, String)
   */
  boolean isEmpty(T message);

  /**
   * @return 是否存在对应的字段
   */
  boolean hasField(String fieldName);

  /**
   * 获取message的字段名集合
   */
  Set<String> getFieldNames();

  /**
   * 获取对应字段的类型
   *
   * @return null if no field named as {@code fieldName}
   */
  Class<?> getFieldType(String fieldName);

  /**
   * @return 字段名和字段类型的映射
   */
  Map<String, Class<?>> getFieldTypeMap();

  /**
   * message中对应字段是否设置
   * <ul>
   * <li>基本类型: true=不等于默认值</li>
   * <li>嵌套类型: true=不为null</li>
   * <li>集合类型: true=不为空</li>
   * </ul>
   *
   * @throws RuntimeException no field found
   */
  boolean isFieldSet(T message, String fieldName);

  /**
   * 获取message中对应字段的值
   * <ul>
   * <li>已设置：设置的值</li>
   * <li>未设置：默认值，其中对于集合类型返回空集合</li>
   * </ul>
   *
   * @throws RuntimeException no field found
   * @see #isFieldSet(T, String)
   */
  Object getFieldValue(T message, String fieldName);

  /**
   * 设置message的{@code fieldName}字段值为{@code fieldValue}
   *
   * @return new message with the field set
   * @throws RuntimeException no field found
   */
  T setFieldValue(T message, String fieldName, Object fieldValue);

  /**
   * 统一处理message的toString
   */
  String toString(T message);
}
