package protobuf4j.core;

import java.util.Collection;
import java.util.Set;

/**
 * 通过反射处理enum的辅助类
 *
 * author: yuanwq
 * date: 2018/7/2
 */
public interface IEnumHelper<T> {

  /**
   * @return enum的类型
   */
  Class<? extends T> getType();

  /**
   * @return enum的默认值
   */
  T defaultValue();

  /**
   * 获取enum的值集合
   */
  Collection<T> getEnumValues();

  /**
   * 获取enum的值的name集合
   */
  Set<String> getEnumValueNames();


  /**
   * 获取enum的值的name集合
   */
  Set<Integer> getEnumValueNumbers();

  /**
   * 根据name查找枚举值，若没有则返回null
   */
  T of(String enumValueName);

  /**
   * 根据number查找枚举值，若没有则返回null
   */
  T forNumber(int number);

  /**
   * 统一处理enum的toString
   */
  String toString(T enumValue);
}
