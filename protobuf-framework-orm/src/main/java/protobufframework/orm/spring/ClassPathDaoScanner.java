package org.protoframework.orm.spring;

import org.protoframework.orm.dao.IMessageDao;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;

import java.util.Set;

/**
 * 参考{@code org.mybatis.spring.mapper.ClassPathMapperScanner}
 *
 * @author: yuanwq
 * @date: 2018/8/5
 */
public class ClassPathDaoScanner extends ClassPathBeanDefinitionScanner {
  public ClassPathDaoScanner(BeanDefinitionRegistry registry) {
    super(registry, false);

    registerFilters();
  }

  private void registerFilters() {
    addIncludeFilter(new AnnotationTypeFilter(Dao.class));
    addIncludeFilter(new AssignableTypeFilter(IMessageDao.class));
  }

  @Override
  protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
    Set<BeanDefinitionHolder> holders = super.doScan(basePackages);
    for (BeanDefinitionHolder holder : holders) {
      // TODO:
    }

    return holders;
  }
}
