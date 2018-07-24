package org.protoframework.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.sql.DataSource;

/**
 * @author: yuanwq
 * @date: 2018/7/23
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = JdbcConfiguration.class)
public class TestMessageDao {
  @Autowired
  private DataSource dataSource;
  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Test
  public void test() {
    System.out.println(dataSource.getClass());
    System.out.println(JdbcTestUtils.countRowsInTable(jdbcTemplate, "db_msg"));
  }
}
