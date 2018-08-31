package top.fangwz.springboot.datasource;

import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 配合 {@link DataSourceRouting} 获取指定名称的 {@link JdbcTemplate}
 *
 * @author: yuanwq
 * @date: 2018/8/2
 * @see DataSourceRouting
 * @see DataSourceRoutingPostProcessor
 */
public interface JdbcTemplateAware extends RoutingAware {

  void setJdbcTemplate(JdbcTemplate jdbcTemplate);
}
