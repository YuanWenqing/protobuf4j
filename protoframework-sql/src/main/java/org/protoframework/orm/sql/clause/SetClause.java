package org.protoframework.orm.sql.clause;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import lombok.Data;
import org.protoframework.orm.sql.AbstractSqlObject;
import org.protoframework.orm.sql.IExpression;
import org.protoframework.orm.sql.ISqlValue;
import org.protoframework.orm.sql.expr.Column;
import org.protoframework.orm.sql.expr.Value;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author: yuanwq
 * @date: 2018/7/12
 */
@Data
public class SetClause extends AbstractSqlObject {
  private final List<SetItem> setItems = Lists.newArrayList();

  public boolean isEmpty() {
    return setItems.isEmpty();
  }

  public SetClause clear() {
    setItems.clear();
    return this;
  }

  public SetClause addSetItem(SetItem setItem) {
    this.setItems.add(setItem);
    return this;
  }

  public SetClause setExpression(String column, IExpression expression) {
    return addSetItem(new SetItem(Column.of(column), expression));
  }

  public SetClause setValue(String column, Object value) {
    return setExpression(column, Value.of(value, column));
  }

  public SetClause setColumn(String column, String otherColumn) {
    return setExpression(column, Column.of(otherColumn));
  }

  @Override
  public StringBuilder toSqlTemplate(@Nonnull StringBuilder sb) {
    Preconditions.checkArgument(!setItems.isEmpty(), "nothing to set");
    sb.append("SET ");
    boolean first = true;
    for (SetItem setItem : setItems) {
      if (first) {
        first = false;
      } else {
        sb.append(",");
      }
      setItem.toSqlTemplate(sb);
    }
    return sb;
  }

  @Override
  public StringBuilder toSolidSql(@Nonnull StringBuilder sb) {
    sb.append("SET ");
    boolean first = true;
    for (SetItem setItem : setItems) {
      if (first) {
        first = false;
      } else {
        sb.append(",");
      }
      setItem.toSolidSql(sb);
    }
    return sb;
  }

  @Override
  public List<ISqlValue> collectSqlValue(@Nonnull List<ISqlValue> sqlValues) {
    for (SetItem setItem : setItems) {
      setItem.collectSqlValue(sqlValues);
    }
    return sqlValues;
  }
}
