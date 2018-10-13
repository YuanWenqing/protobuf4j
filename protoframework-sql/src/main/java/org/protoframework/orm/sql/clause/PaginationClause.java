package org.protoframework.orm.sql.clause;

import com.google.common.base.Preconditions;
import lombok.Data;
import org.protoframework.orm.sql.AbstractSqlObject;
import org.protoframework.orm.sql.ISqlValue;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * 分页子句：{@code LIMIT <limit> OFFSET <offset>}
 * <p>
 *
 * @author: yuanwq
 * @date: 2018/7/12
 */
@Data
public abstract class PaginationClause extends AbstractSqlObject {
  protected final int limit;

  public static Builder newBuilder(int limit) {
    return new Builder(limit);
  }

  public abstract int getOffset();

  public abstract PaginationClause next();

  public int totalPages(int totalItems) {
    if (limit == 0 || totalItems <= 0) {
      return 0;
    }
    return (totalItems - 1) / limit + 1;
  }

  @Override
  public StringBuilder toSqlTemplate(@Nonnull StringBuilder sb) {
    sb.append("LIMIT ").append(limit);
    int offset = getOffset();
    if (offset > 0) {
      sb.append(" OFFSET ").append(offset);
    }
    return sb;
  }

  @Override
  public StringBuilder toSolidSql(@Nonnull StringBuilder sb) {
    return toSqlTemplate(sb);
  }

  @Override
  public List<ISqlValue> collectSqlValue(@Nonnull List<ISqlValue> sqlValues) {
    return sqlValues;
  }

  private static class OffsetLimit extends PaginationClause {
    private final int offset;

    public OffsetLimit(int limit, int offset) {
      super(limit);
      this.offset = offset;
    }

    @Override
    public int getOffset() {
      return offset;
    }

    @Override
    public PaginationClause next() {
      return new OffsetLimit(limit, offset + limit);
    }
  }

  private static class PageNoLimit extends PaginationClause {
    private final int pageNo;

    public PageNoLimit(int limit, int pageNo) {
      super(limit);
      this.pageNo = pageNo;
    }

    @Override
    public int getOffset() {
      return (pageNo - 1) * limit;
    }

    @Override
    public PaginationClause next() {
      return new PageNoLimit(limit, pageNo + 1);
    }
  }

  public static class Builder {
    private int limit;
    private Builder(int limit) {
      this.limit = limit;
    }

    public PaginationClause build() {
      return buildByOffset(0);
    }

    public PaginationClause buildByOffset(int offset) {
      Preconditions.checkArgument(limit >= 0, "limit(>=0): " + limit);
      Preconditions.checkArgument(offset >= 0, "offset(>=0): " + offset);
      return new OffsetLimit(limit, offset);
    }

    public PaginationClause buildByPageNo(int pageNo) {
      Preconditions.checkArgument(limit >= 0, "limit(>=0): " + limit);
      Preconditions.checkArgument(pageNo > 0, "pageNo(>0): " + pageNo);
      return new PageNoLimit(limit, pageNo);
    }
  }

}
