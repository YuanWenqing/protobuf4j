package protobuf4j.orm;

import java.lang.annotation.*;

/**
 * 根据{@code value}值来寻找对应的名为`{@code <value>JdbcTemplate}`的{@code JdbcTemplate}，注入到对应的Dao实例中
 * <p>
 * Author: yuanwq
 * Date: 2019/2/24
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSourceRouting {
  /**
   * {@code JdbcTemplate}的bean的名字前缀，完整的beanName={@code <value>JdbcTemplate}
   */
  String value();

}
