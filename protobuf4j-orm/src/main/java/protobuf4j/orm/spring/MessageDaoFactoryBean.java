package org.protoframework.orm.spring;

import org.springframework.beans.factory.FactoryBean;

/**
 * 参考{@code org.mybatis.spring.mapper.MapperFactoryBean}
 *
 * @author: yuanwq
 * @date: 2018/8/5
 */
public class MessageDaoFactoryBean<T> implements FactoryBean<T> {
  private final Class<? extends T> daoInterface;

  public MessageDaoFactoryBean(Class<? extends T> daoInterface) {
    this.daoInterface = daoInterface;
  }

  @Override
  public T getObject() throws Exception {
    // TODO: impl 2018/8/5
    return null;
  }

  @Override
  public Class<?> getObjectType() {
    return daoInterface;
  }

  @Override
  public boolean isSingleton() {
    return true;
  }
}
