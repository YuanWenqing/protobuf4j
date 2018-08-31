package top.fangwz.springboot.datasource;

import org.junit.Test;

import java.io.IOException;
import java.util.Properties;

import static org.junit.Assert.*;

/**
 * @author: yuanwq
 * @date: 2018/8/30
 */
public class TestPropertiesLoader {
  @Test
  public void testDefaultLoader() throws IOException {
    DefaultPropertiesLoader loader = new DefaultPropertiesLoader();
    Properties properties = loader.load("classpath:application.properties");
    assertFalse(properties.isEmpty());

    try {
      loader.load("a");
      fail();
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }
}
