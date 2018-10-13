package org.protoframework.orm.sql;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author: yuanwq
 * @date: 2018/7/11
 */
public interface ISqlObject {
  /**
   * 构造jdbc所用的sqlTemplate
   *
   * @return {@code sb} for chain invocation
   */
  StringBuilder toSqlTemplate(@Nonnull StringBuilder sb);

  /**
   * 使用对应参数构造实际的sql语句，主要用于可视化
   * <p>
   * 由于类型映射原因，构造的sql语句可能不完全满足sql语法，即有可能不可执行
   *
   * @return {@code sb} for chain invocation
   */
  StringBuilder toSolidSql(@Nonnull StringBuilder sb);

  /**
   * 收集{@code SqlTemplate}中的参数值
   *
   * @return {@code sqlValues} for chain invocation
   */
  List<ISqlValue> collectSqlValue(@Nonnull List<ISqlValue> sqlValues);

  /**
   * 显示声明，要求实现类重写{@code toString}
   */
  String toString();
}
