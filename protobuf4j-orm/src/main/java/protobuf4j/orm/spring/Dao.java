package org.protoframework.orm.spring;

import java.lang.annotation.*;

/**
 * @author: yuanwq
 * @date: 2018/8/5
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Dao {
  Class<?> value();

  String primaryKey() default "";

}
