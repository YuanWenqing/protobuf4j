package org.protoframework.orm.dao;

import org.springframework.beans.factory.Aware;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author: yuanwq
 * @date: 2018/8/2
 */
public interface JdbcTemplateAware extends Aware {

  void setJdbcTemplate(JdbcTemplate jdbcTemplate);
}
