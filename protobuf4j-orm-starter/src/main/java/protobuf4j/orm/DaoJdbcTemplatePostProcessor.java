package protobuf4j.orm;

import com.google.common.collect.Maps;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import protobuf4j.orm.dao.IMessageDao;

import java.util.Map;

/**
 * author: yuanwq
 * date: 2018/9/24
 */
class DaoJdbcTemplatePostProcessor implements BeanPostProcessor, ApplicationContextAware {
  private ApplicationContext applicationContext;
  private JdbcTemplate defaultJdbcTemplate;
  private Map<String, JdbcTemplate> jdbcTemplateMap = Maps.newConcurrentMap();

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
    JdbcTemplate jdbcTemplate = findJdbcTemplate(dao);
    dao.setJdbcTemplate(jdbcTemplate);
    return dao;
  }

  private JdbcTemplate findJdbcTemplate(IMessageDao<?> dao) {
    DataSourceRouting dataSourceRouting =
        AnnotationUtils.findAnnotation(dao.getClass(), DataSourceRouting.class);
    if (dataSourceRouting == null) {
      if (defaultJdbcTemplate == null) {
        defaultJdbcTemplate = applicationContext.getBean(JdbcTemplate.class);
      }
      return defaultJdbcTemplate;
    }
    if (!jdbcTemplateMap.containsKey(dataSourceRouting.value())) {
      String name = dataSourceRouting.value() + "JdbcTemplate";
      jdbcTemplateMap.putIfAbsent(dataSourceRouting.value(),
          applicationContext.getBean(name, JdbcTemplate.class));
    }
    return jdbcTemplateMap.get(dataSourceRouting.value());
  }
}
