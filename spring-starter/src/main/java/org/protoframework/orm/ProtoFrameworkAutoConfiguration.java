package org.protoframework.orm;

import org.protoframework.orm.spring.DataSourceRoutingPostProcessor;
import org.protoframework.orm.spring.JdbcRoutingResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: yuanwq
 * @date: 2018/8/8
 */
@Configuration
public class ProtoFrameworkAutoConfiguration {

  @Bean
  public JdbcRoutingResolver jdbcRoutingResolver() {
    return new JdbcRoutingResolver();
  }

  @Bean
  public DataSourceRoutingPostProcessor dataSourceRoutingPostProcessor() {
    return new DataSourceRoutingPostProcessor(jdbcRoutingResolver());
  }
}
