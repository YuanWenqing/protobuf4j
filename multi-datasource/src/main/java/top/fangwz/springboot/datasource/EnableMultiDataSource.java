package top.fangwz.springboot.datasource;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 开启多数据源支持：
 * 从{@link #location()}指定的数据源配置文件中读取所有配置项，
 * 根据{@link #loader()}指定的类型创建配置文件加载对象加载配置，
 * 根据{@link #prefix()}进行解析，将prefix之后的第一部分作为数据源名称，第二部分是数据源配置项
 * 比如：
 * <pre>
 *   [prefix].[name].url = jdbc:mysql://localhost:3306/test
 *   [prefix].[name].username = test
 *   [prefix].[name].password = [password]
 *   [prefix].[name].driver-class-name = com.mysql.cj.jdbc.Driver
 * </pre>
 *
 * @author: yuanwq
 * @date: 2018/8/28
 * @see MultiDataSourceProperties
 * @see PropertiesLoader
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({MultiDataSourceConfiguration.class, MultiDataSourceImportRegistrar.class})
public @interface EnableMultiDataSource {
  String DEFAULT_LOCATION = "classpath:application.properties";

  /**
   * 配置文件地址
   */
  String location() default DEFAULT_LOCATION;

  /**
   * 配置项前缀
   */
  String prefix() default "multi-datasource.multi";

  /**
   * 处理配置文件的类
   */
  Class<? extends PropertiesLoader> loader() default DefaultPropertiesLoader.class;
}
