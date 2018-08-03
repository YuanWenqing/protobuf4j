package org.protoframework.orm.spring;

import javax.sql.DataSource;

/**
 * @author: yuanwq
 * @date: 2018/8/3
 */
public interface DataSourceAware {
  void setDataSource(DataSource dataSource);
}
