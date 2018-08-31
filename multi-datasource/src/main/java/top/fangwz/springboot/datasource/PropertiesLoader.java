package top.fangwz.springboot.datasource;

import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.util.Properties;

/**
 * @author: yuanwq
 * @date: 2018/8/30
 */
public interface PropertiesLoader {
  void setResourceLoader(ResourceLoader resourceLoader);

  Properties load(String location) throws IOException;
}
