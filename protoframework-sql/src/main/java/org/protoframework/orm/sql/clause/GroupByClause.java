package org.protoframework.orm.sql.clause;

import com.google.common.collect.Lists;
import lombok.Data;
import org.protoframework.orm.sql.AbstractSqlObject;
import org.protoframework.orm.sql.Direction;
import org.protoframework.orm.sql.IExpression;
import org.protoframework.orm.sql.ISqlValue;
import org.protoframework.orm.sql.expr.Column;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author: yuanwq
 * @date: 2018/7/12
 */
@Data
public class GroupByClause extends AbstractSqlObject {
  private final List<GroupByItem> groupByItems = Lists.newArrayList();

  public GroupByClause clear() {
    groupByItems.clear();
    return this;
  }

  private GroupByClause addGroupByItem(GroupByItem groupByItem) {
    this.groupByItems.add(groupByItem);
    return this;
  }

  public GroupByClause by(IExpression expr) {
    return addGroupByItem(new GroupByItem(expr));
  }

  public GroupByClause by(String column) {
    return by(Column.of(column));
  }

  public GroupByClause asc(IExpression expr) {
    return addGroupByItem(new GroupByItem(expr, Direction.ASC));
  }

  public GroupByClause asc(String column) {
    return asc(Column.of(column));
  }

  public GroupByClause desc(IExpression expr) {
    return addGroupByItem(new GroupByItem(expr, Direction.DESC));
  }

  public GroupByClause desc(String column) {
    return desc(Column.of(column));
  }

  public boolean isEmpty() {
    return groupByItems.isEmpty();
  }

  @Override
  public StringBuilder toSqlTemplate(@Nonnull StringBuilder sb) {
    if (groupByItems.isEmpty()) {
      return sb;
    }
    sb.append("GROUP BY ");
    boolean first = true;
    for (GroupByItem expr : groupByItems) {
      if (first) {
        first = false;
      } else {
        sb.append(",");
      }
      expr.toSqlTemplate(sb);
    }
    return sb;
  }

  @Override
  public StringBuilder toSolidSql(@Nonnull StringBuilder sb) {
    if (groupByItems.isEmpty()) {
      return sb;
    }
    sb.append("GROUP BY ");
    boolean first = true;
    for (GroupByItem expr : groupByItems) {
      if (first) {
        first = false;
      } else {
        sb.append(",");
      }
      expr.toSolidSql(sb);
    }
    return sb;
  }

  @Override
  public List<ISqlValue> collectSqlValue(@Nonnull List<ISqlValue> sqlValues) {
    for (GroupByItem expr : groupByItems) {
      expr.collectSqlValue(sqlValues);
    }
    return sqlValues;
  }

}
