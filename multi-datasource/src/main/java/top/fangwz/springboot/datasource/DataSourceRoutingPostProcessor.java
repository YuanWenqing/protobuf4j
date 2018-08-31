package top.fangwz.springboot.datasource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNotOfRequiredTypeException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

import static com.google.common.base.Preconditions.*;

/**
 * @author: yuanwq
 * @date: 2018/8/2
 */
class DataSourceRoutingPostProcessor implements BeanPostProcessor, ApplicationContextAware {
  private ApplicationContext applicationContext;

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  @Override
  public Object postProcessBeforeInitialization(Object bean, String beanName)
      throws BeansException {
    DataSourceRouting routing =
        AnnotationUtils.findAnnotation(bean.getClass(), DataSourceRouting.class);
    if (routing == null) {
      return bean;
    }
    checkArgument(
        StringUtils.isNotBlank(routing.value()) && !StringUtils.containsWhitespace(routing.value()),
        "Illegal datasource name: " + routing.value() + " on " + bean.getClass().getName());
    if (bean instanceof JdbcTemplateAware) {
      JdbcTemplate jdbcTemplate = findJdbcTemplate(routing.value());
      ((JdbcTemplateAware) bean).setJdbcTemplate(jdbcTemplate);
    } else if (bean instanceof DataSourceAware) {
      DataSource dataSource = findDataSource(routing.value());
      ((DataSourceAware) bean).setDataSource(dataSource);
    } else {
      throw new BeanNotOfRequiredTypeException(beanName, RoutingAware.class, bean.getClass());
    }
    return bean;
  }

  private JdbcTemplate findJdbcTemplate(String baseName) {
    String beanName = DataSourceUtils.generateJdbcTemplateBeanName(baseName);
    return applicationContext.getBean(beanName, JdbcTemplate.class);
  }

  private DataSource findDataSource(String baseName) {
    String beanName = DataSourceUtils.generateDataSourceBeanName(baseName);
    return applicationContext.getBean(beanName, DataSource.class);
  }

  @Override
  public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
    return bean;
  }
}
