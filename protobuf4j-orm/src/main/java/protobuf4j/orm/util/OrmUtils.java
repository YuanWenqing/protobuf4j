package protobuf4j.orm.util;

import com.google.common.base.CaseFormat;

public abstract class OrmUtils {
  public static String tableName(Class<?> cls) {
    return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, cls.getSimpleName());
  }

}
