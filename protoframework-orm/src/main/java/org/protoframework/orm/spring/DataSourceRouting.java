package org.protoframework.orm.spring;

import java.lang.annotation.*;

/**
 * 将根据{@code value}值来寻找对应的{@code JdbcTemplate}，具体流程如下：
 * <ol>
 * <li>寻找名为`{@code <value>JdbcTemplate}`的{@code JdbcTemplate}</li>
 * <li>若没有找到，则寻找名为`{@code <value>DataSource}`的{@code DataSource}，
 * 使用该{@code DataSource}构造名为`{@code <value>JdbcTemplate}`的{@code JdbcTemplate}</li>
 * <li>将{@code JdbcTemplate}注入到对应的Dao实例中</li>
 * </ol>
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
