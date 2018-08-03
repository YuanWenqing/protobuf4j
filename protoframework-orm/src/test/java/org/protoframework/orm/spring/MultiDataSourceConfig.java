package org.protoframework.orm.spring;

import org.apache.logging.log4j.status.StatusLogger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

/**
 * @author: yuanwq
 * @date: 2018/8/3
 */
@Configuration
@ComponentScan
public class MultiDataSourceConfig {
  static {
    StatusLogger.getLogger();
  }

  @Bean("aDataSource")
  public DataSource aDataSource() {
    return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).setName("a").build();
  }

  @Bean("bDataSource")
  public DataSource bDataSource() {
    return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).setName("b").build();
  }

  @Bean
  public JdbcRoutingResolver jdbcRoutingResolver() {
    return new JdbcRoutingResolver();
  }

  @Bean
  public DataSourceRoutingPostProcessor dataSourceRoutingPostProcessor(
      JdbcRoutingResolver resolver) {
    return new DataSourceRoutingPostProcessor(resolver);
  }
}
