package top.fangwz.springboot.datasource;

import javax.sql.DataSource;

/**
 * 配合 {@link DataSourceRouting} 获取指定名称的 {@link DataSource}
 *
 * @author: yuanwq
 * @date: 2018/8/3
 * @see DataSourceRouting
 * @see DataSourceRoutingPostProcessor
 */
public interface DataSourceAware extends RoutingAware {
  void setDataSource(DataSource dataSource);
}
