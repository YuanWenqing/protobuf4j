package top.fangwz.springboot.datasource;

import com.google.common.base.Splitter;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author: yuanwq
 * @date: 2018/8/29
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration(classes = TestMultiDataSource.ConfigurationForTest.class)
public class TestMultiDataSource {
  @Configuration
  @EnableMultiDataSource
  public static class ConfigurationForTest {

  }

  @Autowired
  @Qualifier("aJdbcTemplate")
  private JdbcTemplate aJdbcTemplate;
  @Autowired
  @Qualifier("bJdbcTemplate")
  private JdbcTemplate bJdbcTemplate;

  @Test
  public void testJdbcTemplate() throws IOException {
    String initSql = readInitSql();
    Splitter.on(";").trimResults().omitEmptyStrings().split(initSql)
        .forEach(sql -> aJdbcTemplate.execute(sql));
    Map<String, Object> map = aJdbcTemplate.queryForMap("select * from user where id =1");
    assertEquals("a", map.get("name"));
  }

  private String readInitSql() throws IOException {
    InputStream inputStream = getClass().getClassLoader().getResourceAsStream("init.sql");
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    IOUtils.copy(inputStream, outputStream);
    return new String(outputStream.toByteArray());
  }
}
