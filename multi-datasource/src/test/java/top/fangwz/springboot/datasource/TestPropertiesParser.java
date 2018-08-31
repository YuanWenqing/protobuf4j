package top.fangwz.springboot.datasource;

import com.zaxxer.hikari.HikariDataSource;
import org.junit.Test;

import java.util.Properties;

import static org.junit.Assert.*;

/**
 * @author: yuanwq
 * @date: 2018/8/30
 */
public class TestPropertiesParser {
  @Test
  public void testPrefix() {
    PropertiesParser parser = new PropertiesParser();
    parser.setPrefix("p");
    Properties properties = new Properties();
    properties.setProperty("p.a.url", "aaa");
    properties.setProperty("p.a.type", HikariDataSource.class.getName());
    MultiDataSourceProperties multiDataSourceProperties = new MultiDataSourceProperties();
    parser.parse(properties, multiDataSourceProperties);
    assertEquals(1, multiDataSourceProperties.getMulti().size());
    assertNotNull(multiDataSourceProperties.getDataSourceProperties("a"));
    assertEquals("aaa", multiDataSourceProperties.getDataSourceProperties("a").getUrl());
    assertEquals(HikariDataSource.class,
        multiDataSourceProperties.getDataSourceProperties("a").getType());
  }

  @Test
  public void testEmptyPrefix() {
    PropertiesParser parser = new PropertiesParser();
    parser.setPrefix("");
    Properties properties = new Properties();
    properties.setProperty("a.url", "aaa");
    properties.setProperty("a.type", HikariDataSource.class.getName());
    MultiDataSourceProperties multiDataSourceProperties = new MultiDataSourceProperties();
    parser.parse(properties, multiDataSourceProperties);
    assertEquals(1, multiDataSourceProperties.getMulti().size());
    assertNotNull(multiDataSourceProperties.getDataSourceProperties("a"));
    assertEquals("aaa", multiDataSourceProperties.getDataSourceProperties("a").getUrl());
    assertEquals(HikariDataSource.class,
        multiDataSourceProperties.getDataSourceProperties("a").getType());
  }

  @Test
  public void testWrongType() {
    PropertiesParser parser = new PropertiesParser();
    Properties properties = new Properties();
    properties.setProperty("a.type", "a");
    MultiDataSourceProperties multiDataSourceProperties = new MultiDataSourceProperties();
    try {
      parser.parse(properties, multiDataSourceProperties);
      fail();
    } catch (RuntimeException e) {
      System.out.println(e.getClass() + ": " + e.getMessage());
    }
  }

  @Test
  public void testIllegalPrefix() {
    PropertiesParser parser = new PropertiesParser();
    try {
      parser.setPrefix(null);
      fail();
    } catch (NullPointerException e) {
    }
    try {
      parser.setPrefix("a b");
      fail();
    } catch (IllegalArgumentException e) {
    }
    try {
      parser.setPrefix(".");
      fail();
    } catch (IllegalArgumentException e) {
    }
    try {
      parser.setPrefix("a.");
      fail();
    } catch (IllegalArgumentException e) {
    }

  }
}
