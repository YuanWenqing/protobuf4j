package top.fangwz.springboot.datasource;

import com.google.common.base.Suppliers;
import org.springframework.beans.factory.Aware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

/**
 * {@link ImportBeanDefinitionRegistrar}实现类只能通过 {@link Aware} 接口注入Spring容器本身的基础bean，
 * 其他的bean都需要在Configuration处理完bean定义之后才真正开始进行相关properties设置和初始化操作，
 * 而ImportBeanDefinitionRegistrar本身就是注册bean定义逻辑的一部分。
 * 另外，ConfigurationProperties是在ConfigurationPropertiesBindingPostProcessor中才处理的，
 * 所以，不能用ConfigurationProperties的方式读取配置，这里需要额外处理配置文件的解析。
 *
 * @author: yuanwq
 * @date: 2018/8/29
 * @see PropertiesLoader
 * @see PropertiesParser
 */
class MultiDataSourceImportRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware {

  private ResourceLoader resourceLoader = new DefaultResourceLoader();

  @Override
  public void setResourceLoader(ResourceLoader resourceLoader) {
    if (resourceLoader != null) {
      this.resourceLoader = resourceLoader;
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,
      BeanDefinitionRegistry registry) {
    Map<String, Object> attributes =
        importingClassMetadata.getAnnotationAttributes(EnableMultiDataSource.class.getName());
    PropertiesLoader loader = createLoader(attributes);
    Properties properties;
    try {
      properties = loader.load((String) attributes.get("location"));
    } catch (IOException e) {
      // TODO: find a proper spring exception
      throw new RuntimeException("fail to load multi-datasource properties configuration", e);
    }
    MultiDataSourceProperties multiDataSourceProperties = new MultiDataSourceProperties();
    PropertiesParser parser = createParser(attributes);
    parser.parse(properties, multiDataSourceProperties);
    registerDataSourceAndJdbc(multiDataSourceProperties, registry);
  }

  @SuppressWarnings("unchecked")
  private PropertiesLoader createLoader(Map<String, Object> attributes) {
    PropertiesLoader loader;
    Class<? extends PropertiesLoader> loaderClass =
        (Class<? extends PropertiesLoader>) attributes.get("loader");
    try {
      loader = loaderClass.newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      // TODO: find a proper spring exception
      throw new RuntimeException("fail to instantiate a loader: " + loaderClass.getName(), e);
    }
    loader.setResourceLoader(resourceLoader);
    return loader;
  }

  private PropertiesParser createParser(Map<String, Object> attributes) {
    PropertiesParser parser = new PropertiesParser();
    parser.setPrefix((String) attributes.get("prefix"));
    return parser;
  }

  private void registerDataSourceAndJdbc(MultiDataSourceProperties properties,
      BeanDefinitionRegistry registry) {
    for (Map.Entry<String, DataSourceProperties> entry : properties.getMulti().entrySet()) {
      String dataSourceName = DataSourceUtils.generateDataSourceBeanName(entry.getKey());
      DataSource dataSource = entry.getValue().initializeDataSourceBuilder().build();
      BeanDefinition dataSourceBean = BeanDefinitionBuilder
          .genericBeanDefinition(DataSource.class, Suppliers.ofInstance(dataSource))
          .getBeanDefinition();
      registry.registerBeanDefinition(dataSourceName, dataSourceBean);
      BeanDefinition jdbcBean = BeanDefinitionBuilder.genericBeanDefinition(JdbcTemplate.class)
          .addConstructorArgReference(dataSourceName).getBeanDefinition();
      String jdbcName = DataSourceUtils.generateJdbcTemplateBeanName(entry.getKey());
      registry.registerBeanDefinition(jdbcName, jdbcBean);
    }
  }

}
