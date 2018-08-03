package org.protoframework.orm.spring;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
}
