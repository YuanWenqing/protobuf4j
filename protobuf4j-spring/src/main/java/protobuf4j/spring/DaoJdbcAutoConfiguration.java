package protobuf4j.spring;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 自动配置dao的jdbc
 *
 * author: yuanwq
 * date: 2018/9/24
 */
@Configuration
@ConditionalOnProperty(value = "protobuf4j.datasource.auto-enable", matchIfMissing = true)
@ConditionalOnBean(JdbcTemplate.class)
@AutoConfigureAfter(JdbcTemplateAutoConfiguration.class)
public class DaoJdbcAutoConfiguration {
  @Bean
  public DaoJdbcTemplatePostProcessor daoJdbcTemplatePostProcessor() {
    return new DaoJdbcTemplatePostProcessor();
  }

}
