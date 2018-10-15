package protobufframework.orm.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

/**
 * 需要使用jdbc的测试用例的基类
 *
 * @author: yuanwq
 * @date: 2018/7/24
 */
@Configuration
public class JdbcConfiguration {

  @Bean
  public DataSource dataSource() {
    // https://blog.philipphauer.de/dont-use-in-memory-databases-tests-h2/ 不建议使用内嵌数据库做测试
    // 不过我们这里做基本的dao逻辑验证，h2的mysql模式基本足够了
    // http://coderec.cn/2016/08/09/%E5%8D%95%E5%85%83%E6%B5%8B%E8%AF%95%E4%B9%8B%E4%BD%BF%E7%94%A8H2-Database%E6%A8%A1%E6%8B%9F%E6%95%B0%E6%8D%AE%E5%BA%93%E7%8E%AF%E5%A2%83/
    return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).addScript("config.sql")
        .addDefaultScripts().build();
  }

  @Bean
  public JdbcTemplate jdbcTemplate(DataSource dataSource) {
    return new JdbcTemplate(dataSource);
  }
}
