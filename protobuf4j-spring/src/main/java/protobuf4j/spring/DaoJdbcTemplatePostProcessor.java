package protobuf4j.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.core.JdbcTemplate;
import protobuf4j.orm.dao.IMessageDao;

/**
 * author: yuanwq
 * date: 2018/9/24
 */
class DaoJdbcTemplatePostProcessor implements BeanPostProcessor, ApplicationContextAware {
  private ApplicationContext applicationContext;
  private JdbcTemplate jdbcTemplate;

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  @Override
  public Object postProcessBeforeInitialization(Object bean, String beanName)
      throws BeansException {
    if (!(bean instanceof IMessageDao)) {
      return bean;
    }
    IMessageDao<?> dao = (IMessageDao<?>) bean;
    if (jdbcTemplate == null) {
      jdbcTemplate = applicationContext.getBean(JdbcTemplate.class);
    }
    dao.setJdbcTemplate(jdbcTemplate);
    return dao;
  }
}
