package top.fangwz.springboot.datasource;

/**
 * @author: yuanwq
 * @date: 2018/8/31
 */
public class DataSourceUtils {
  private DataSourceUtils() {
  }

  private static final String SUFFIX_DATA_SOURCE = "DataSource";
  private static final String SUFFIX_JDBC_TEMPLATE = "JdbcTemplate";

  public static String generateDataSourceBeanName(String baseName) {
    return baseName + SUFFIX_DATA_SOURCE;
  }

  public static String generateJdbcTemplateBeanName(String baseName) {
    return baseName + SUFFIX_JDBC_TEMPLATE;
  }
}
