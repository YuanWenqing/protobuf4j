package org.protoframework.orm.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.StringUtils;

/**
 * 参考{@code org.mybatis.spring.mapper.MapperScannerConfigurer}
 *
 * @author: yuanwq
 * @date: 2018/8/5
 */
public class DaoScannerPostProcesser
    implements BeanDefinitionRegistryPostProcessor, ApplicationContextAware {
  private String basePackage;
  private ApplicationContext applicationContext;
  private JdbcRoutingResolver jdbcRoutingResolver;
  private BeanNameGenerator nameGenerator;

  public void setBasePackage(String basePackage) {
    this.basePackage = basePackage;
  }

  public void setJdbcRoutingResolver(JdbcRoutingResolver jdbcRoutingResolver) {
    this.jdbcRoutingResolver = jdbcRoutingResolver;
  }

  public void setNameGenerator(BeanNameGenerator nameGenerator) {
    this.nameGenerator = nameGenerator;
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  @Override
  public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry)
      throws BeansException {
    // TODO: impl 2018/8/5
    ClassPathDaoScanner scanner = new ClassPathDaoScanner(registry);
    scanner.setResourceLoader(applicationContext);
    scanner.setBeanNameGenerator(nameGenerator);
    scanner.scan(StringUtils.tokenizeToStringArray(this.basePackage,
        ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS));
  }

  @Override
  public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
      throws BeansException {
    // TODO: impl 2018/8/5

  }
}
