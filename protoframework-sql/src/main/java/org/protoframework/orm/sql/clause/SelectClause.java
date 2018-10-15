package org.protoframework.orm.sql.clause;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import lombok.Data;
import org.protoframework.orm.sql.AbstractSqlObject;
import org.protoframework.orm.sql.IExpression;
import org.protoframework.orm.sql.ISqlValue;
import org.protoframework.orm.sql.SqlUtil;
import org.protoframework.orm.sql.expr.Column;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author: yuanwq
 * @date: 2018/7/12
 */
@Data
public class SelectClause extends AbstractSqlObject {
  private final List<SelectItem> selectItems = Lists.newArrayList();

  public boolean isEmpty() {
    return selectItems.isEmpty();
  }

  public SelectClause clear() {
    selectItems.clear();
    return this;
  }

  public SelectClause select(SelectItem selectItem) {
    this.selectItems.add(selectItem);
    return this;
  }

  public SelectClause select(IExpression expr) {
    return select(new SelectItem(expr));
  }

  public SelectClause select(String column) {
    return select(Column.of(column));
  }

  public SelectClause star() {
    return select(SqlUtil.SELECT_STAR);
  }

  @Override
  public StringBuilder toSqlTemplate(@Nonnull StringBuilder sb) {
    Preconditions.checkArgument(!selectItems.isEmpty(), "nothing to select");
    sb.append("SELECT ");
    boolean first = true;
    for (SelectItem selectItem : selectItems) {
      if (first) {
        first = false;
      } else {
        sb.append(",");
      }
      selectItem.toSqlTemplate(sb);
    }
    return sb;
  }

  @Override
  public StringBuilder toSolidSql(@Nonnull StringBuilder sb) {
    sb.append("SELECT ");
    boolean first = true;
    for (SelectItem selectItem : selectItems) {
      if (first) {
        first = false;
      } else {
        sb.append(",");
      }
      selectItem.toSolidSql(sb);
    }
    return sb;
  }

  @Override
  public List<ISqlValue> collectSqlValue(@Nonnull List<ISqlValue> sqlValues) {
    for (SelectItem selectItem : selectItems) {
      selectItem.collectSqlValue(sqlValues);
    }
    return sqlValues;
  }

}
