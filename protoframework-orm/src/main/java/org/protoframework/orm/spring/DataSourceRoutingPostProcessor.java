package org.protoframework.orm.spring;

import org.protoframework.orm.dao.IMessageDao;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNotOfRequiredTypeException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author: yuanwq
 * @date: 2018/8/2
 */
public class DataSourceRoutingPostProcessor implements BeanPostProcessor {
  private JdbcRoutingResolver jdbcRoutingResolver;

  public void setJdbcRoutingResolver(JdbcRoutingResolver jdbcRoutingResolver) {
    this.jdbcRoutingResolver = jdbcRoutingResolver;
  }

  @Override
  public Object postProcessBeforeInitialization(Object bean, String beanName)
      throws BeansException {
    DataSourceRouting routing =
        AnnotationUtils.findAnnotation(bean.getClass(), DataSourceRouting.class);
    if (routing != null) {
      if (!(bean instanceof IMessageDao)) {
        throw new BeanNotOfRequiredTypeException(beanName, IMessageDao.class, bean.getClass());
      }
      JdbcTemplate jdbcTemplate = jdbcRoutingResolver.findJdbcTemplate(routing.value());
      ((IMessageDao) bean).setJdbcTemplate(jdbcTemplate);
    }
    return bean;
  }

  @Override
  public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
    return bean;
  }
}
