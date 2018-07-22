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

  private final ConcurrentHashMap<Class<?>, ISqlConverter<?>> cache;

  private SqlConverterRegistry() {
    this.cache = new ConcurrentHashMap<>(100);
  }

  public static <T> void register(@Nonnull Class<T> beanClass,
      @Nonnull ISqlConverter<? super T> sqlConverter) {
    checkNotNull(beanClass);
    instance.cache.put(beanClass, sqlConverter);
  }

  @SuppressWarnings("unchecked")
  public static <T> ISqlConverter<? super T> findSqlConverter(@Nonnull Class<T> beanClass) {
    if (instance.cache.contains(beanClass)) {
      return (ISqlConverter<? super T>) instance.cache.get(beanClass);
    }
    ISqlConverter<?> converter = findSqlConverter(beanClass.getSuperclass());
    if (converter != null) {
      instance.cache.putIfAbsent(beanClass, converter);
      return (ISqlConverter<? super T>) converter;
    }
    for (Class<?> intf : beanClass.getInterfaces()) {
      converter = findSqlConverter(intf);
      if (converter != null) {
        instance.cache.putIfAbsent(beanClass, converter);
        return (ISqlConverter<? super T>) converter;
      }
    }
    return null;
  }

}
