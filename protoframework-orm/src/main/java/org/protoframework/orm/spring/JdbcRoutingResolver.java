package org.protoframework.orm.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * @author: yuanwq
 * @date: 2018/8/2
 */
public class JdbcRoutingResolver implements ApplicationContextAware {
  private static final String SUFFIX_DATA_SOURCE = "DataSource";
  private static final String SUFFIX_JDBC_TEMPLATE = "JdbcTemplate";
  private ApplicationContext applicationContext;

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }

  public DataSource findDataSource(String name) {
    String beanId = name + SUFFIX_DATA_SOURCE;
    return applicationContext.getBean(beanId, DataSource.class);
  }

  public JdbcTemplate findJdbcTemplate(String name) {
    String beanId = name + SUFFIX_JDBC_TEMPLATE;
    try {
      return applicationContext.getBean(beanId, JdbcTemplate.class);
    } catch (NoSuchBeanDefinitionException e) {
      DataSource dataSource = findDataSource(name);
      BeanDefinition jdbcBean = BeanDefinitionBuilder.genericBeanDefinition(JdbcTemplate.class)
          .addConstructorArgValue(dataSource).getBeanDefinition();
      DefaultListableBeanFactory factory =
          (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
      factory.registerBeanDefinition(beanId, jdbcBean);
      return applicationContext.getBean(beanId, JdbcTemplate.class);
    }
  }
}
