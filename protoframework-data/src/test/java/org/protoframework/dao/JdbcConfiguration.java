package org.protoframework.dao;

import org.apache.logging.log4j.status.StatusLogger;
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
  static {
    // 主动初始化log4j，避免最后shutdown时才初始化，导致出错
    StatusLogger.getLogger();
  }

  @Bean
  public DataSource dataSource() {
    return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.HSQL).addDefaultScripts()
        .build();
  }

  @Bean
  public JdbcTemplate jdbcTemplate(DataSource dataSource) {
    return new JdbcTemplate(dataSource);
  }
}
