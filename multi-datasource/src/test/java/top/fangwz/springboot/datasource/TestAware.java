package top.fangwz.springboot.datasource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.BeanNotOfRequiredTypeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;

import static org.junit.Assert.*;

/**
 * @author: yuanwq
 * @date: 2018/8/31
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(classes = TestAware.ConfigurationForTest.class)
public class TestAware {
  @Configuration
  @EnableMultiDataSource
  @ComponentScan
  public static class ConfigurationForTest {

  }

  @DataSourceRouting("a")
  @Component
  public static class DataSourceAwareBean implements DataSourceAware {
    DataSource dataSource;

    public DataSourceAwareBean() {
      System.err.println("init: " + getClass());
    }

    @Override
    public void setDataSource(DataSource dataSource) {
      this.dataSource = dataSource;
    }
  }

  @DataSourceRouting("b")
  @Component
  public static class JdbcTemplateAwareBean implements JdbcTemplateAware {
    JdbcTemplate jdbcTemplate;

    public JdbcTemplateAwareBean() {
      System.err.println("init: " + getClass());
    }

    @Override
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
      this.jdbcTemplate = jdbcTemplate;
    }
  }

  @Autowired
  private DataSourceAwareBean dataSourceAwareBean;
  @Autowired
  private JdbcTemplateAwareBean jdbcTemplateAwareBean;
  @Autowired
  @Qualifier("aDataSource")
  private DataSource aDataSource;
  @Autowired
  @Qualifier("bJdbcTemplate")
  private JdbcTemplate bJdbcTemplate;
  @Autowired
  private DataSourceRoutingPostProcessor dataSourceRoutingPostProcessor;

  @Test
  public void testDataSourceAware() {
    assertNotNull(dataSourceAwareBean.dataSource);
    assertEquals(aDataSource, dataSourceAwareBean.dataSource);
  }

  @Test
  public void testJdbcTemplateAware() {
    assertNotNull(jdbcTemplateAwareBean.jdbcTemplate);
    assertEquals(bJdbcTemplate, jdbcTemplateAwareBean.jdbcTemplate);
  }

  @DataSourceRouting("a")
  public class NotOfRequiredTypeBean {

  }

  @Test
  public void testNotOfRequiredType() {
    try {
      NotOfRequiredTypeBean bean = new NotOfRequiredTypeBean();
      dataSourceRoutingPostProcessor
          .postProcessBeforeInitialization(bean, bean.getClass().getName());
    } catch (BeanNotOfRequiredTypeException e) {
      System.err.println(e.getMessage());
    }
  }
}
