package top.fangwz.springboot.datasource;

import com.google.common.collect.Maps;
import lombok.Data;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

import static com.google.common.base.Preconditions.*;

/**
 * @author: yuanwq
 * @date: 2018/8/29
 */
@ConfigurationProperties("multi-datasource")
@Data
public class MultiDataSourceProperties {

  private Map<String, DataSourceProperties> multi = Maps.newLinkedHashMap();

  public void addDataSourceProperties(String name, DataSourceProperties properties) {
    checkArgument(!multi.containsKey(name), "duplicated name of DataSource: " + name);
    this.multi.put(name, properties);
  }

  public DataSourceProperties getDataSourceProperties(String name) {
    return this.multi.get(name);
  }
}
