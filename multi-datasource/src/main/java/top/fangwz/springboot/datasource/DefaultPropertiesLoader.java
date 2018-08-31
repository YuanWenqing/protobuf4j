package top.fangwz.springboot.datasource;

import lombok.Setter;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author: yuanwq
 * @date: 2018/8/30
 */
@Setter
public class DefaultPropertiesLoader implements PropertiesLoader {
  private ResourceLoader resourceLoader = new DefaultResourceLoader();

  @Override
  public Properties load(String location) throws IOException {
    Resource resource = resourceLoader.getResource(location);
    if (!resource.exists()) {
      throw new IOException("fail to load resource: " + location);
    }
    try (InputStream inputStream = resource.getInputStream()) {
      Properties properties = new Properties();
      properties.load(inputStream);
      return properties;
    }
  }
}
