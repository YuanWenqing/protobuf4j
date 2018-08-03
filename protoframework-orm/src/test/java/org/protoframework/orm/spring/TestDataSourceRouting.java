package org.protoframework.orm.spring;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;

import static org.junit.Assert.*;

/**
 * @author: yuanwq
 * @date: 2018/8/3
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = MultiDataSourceConfig.class)
public class TestDataSourceRouting {

  @Autowired
  @Qualifier("aDataSource")
  private DataSource aDataSource;
  @Autowired
  @Qualifier("bDataSource")
  private DataSource bDataSource;

  @Autowired
  private ADao aDao;
  @Autowired
  private BDao bDao;
  @Autowired
  private CDao cDao;

  @Test
  public void testDataSourceRouting() {
    assertNotNull(aDao);
    assertNotNull(aDao.getJdbcTemplate());

    assertNotNull(bDao);
    assertNotNull(bDao.getJdbcTemplate());

    assertNotNull(aDataSource);
    assertNotNull(bDataSource);

    assertEquals(aDataSource, aDao.getJdbcTemplate().getDataSource());
    assertEquals(bDataSource, bDao.getJdbcTemplate().getDataSource());
    assertEquals(cDao.getJdbcTemplate(), bDao.getJdbcTemplate());
  }

  @Autowired
  private DSAware dsAware;
  @Autowired
  private JTAware jtAware;

  @Test
  public void testAware() {
    assertEquals(aDataSource, dsAware.dataSource);
    assertEquals(bDataSource, jtAware.jdbcTemplate.getDataSource());
  }

  @Component
  @DataSourceRouting("a")
  public static class DSAware implements DataSourceAware {
    private DataSource dataSource;

    @Override
    public void setDataSource(DataSource dataSource) {
      this.dataSource = dataSource;
    }
  }

  @Component
  @DataSourceRouting("b")
  public static class JTAware implements JdbcTemplateAware {
    private JdbcTemplate jdbcTemplate;

    @Override
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
      this.jdbcTemplate = jdbcTemplate;
    }
  }

}
