package org.protoframework.orm.spring;

import org.apache.commons.lang3.StringUtils;
import org.protoframework.orm.dao.IMessageDao;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNotOfRequiredTypeException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import static com.google.common.base.Preconditions.*;

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
      checkArgument(StringUtils.isNotBlank(routing.value()),
          "Blank value of DataSourceRouting on " + bean.getClass().getName());
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
