package org.protoframework.dao;

import com.google.protobuf.Message;

import javax.annotation.Nonnull;
import java.util.concurrent.ConcurrentHashMap;

import static com.google.common.base.Preconditions.*;

/**
 * author: yuanwq
 * date: 2018/7/15
 */
public class SqlConverterRegistry {
  private static final SqlConverterRegistry instance = buildRegistry();

  private static SqlConverterRegistry buildRegistry() {
    SqlConverterRegistry registry = new SqlConverterRegistry();
    registry.register(Message.class, ProtoSqlConverter.getInstance());
    return registry;
  }

  public static SqlConverterRegistry getInstance() {
    return instance;
  }

  private final ConcurrentHashMap<Class<?>, ISqlConverter<?>> cache;

  private SqlConverterRegistry() {
    this.cache = new ConcurrentHashMap<>(100);
  }

  public <T> void register(@Nonnull Class<T> beanClass,
      @Nonnull ISqlConverter<? super T> sqlConverter) {
    checkNotNull(beanClass);
    this.cache.put(beanClass, sqlConverter);
  }

  @SuppressWarnings("unchecked")
  public <T> ISqlConverter<? super T> findSqlConverter(@Nonnull Class<T> beanClass) {
    checkNotNull(beanClass);
    if (this.cache.containsKey(beanClass)) {
      return (ISqlConverter<? super T>) this.cache.get(beanClass);
    }
    Class<?> superClass = beanClass.getSuperclass();
    if (superClass == null) {
      return null;
    }
    ISqlConverter<?> converter = findSqlConverter(superClass);
    if (converter != null) {
      this.cache.putIfAbsent(beanClass, converter);
      return (ISqlConverter<? super T>) converter;
    }
    for (Class<?> intf : beanClass.getInterfaces()) {
      converter = findSqlConverter(intf);
      if (converter != null) {
        this.cache.putIfAbsent(beanClass, converter);
        return (ISqlConverter<? super T>) converter;
      }
    }
    return null;
  }

}
