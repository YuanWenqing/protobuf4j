package top.fangwz.springboot.datasource;

import java.lang.annotation.*;

/**
 * 根据{@code value}值来寻找对应的名为`{@code <value>JdbcTemplate}`的{@code JdbcTemplate}，注入到对应的Dao实例中
 *
 * @author: yuanwq
 * @date: 2018/8/2
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSourceRouting {
  /**
   * {@code DataSource}的名字，建议与数据库名称相同
   */
  String value();

}
