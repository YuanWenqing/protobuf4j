package top.fangwz.springboot.datasource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: yuanwq
 * @date: 2018/8/28
 */
@Configuration
public class MultiDataSourceConfiguration {

  @Bean
  public DataSourceRoutingPostProcessor dataSourceRoutingPostProcessor() {
    return new DataSourceRoutingPostProcessor();
  }
}
