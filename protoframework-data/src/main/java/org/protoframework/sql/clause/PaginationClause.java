package org.protoframework.sql.clause;

import com.google.common.base.Preconditions;
import org.protoframework.sql.AbstractSqlStatement;
import org.protoframework.sql.ISqlValue;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * 分页子句：{@code LIMIT <limit> OFFSET <offset>}
 * <p>
 *
 * @author: yuanwq
 * @date: 2018/7/12
 */
public abstract class PaginationClause extends AbstractSqlStatement {
  protected final int limit;

  public PaginationClause(int limit) {
    this.limit = limit;
  }

  public static Builder newBuilder(int limit) {
    return new Builder(limit);
  }

  public int getLimit() {
    return limit;
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
    private Integer defaultLimit;
    private Integer defaultOffset;
    private Integer defaultPageNo;

    private Builder(int limit) {
      this.limit = limit;
    }

    /**
     * 设置默认的limit，当构造时limit非法时使用默认值
     */
    public Builder setDefaultLimit(int defaultLimit) {
      this.defaultLimit = defaultLimit;
      return this;
    }

    /**
     * 设置默认的offset，当构造时offset非法时使用默认值
     */
    public Builder setDefaultOffset(int defaultOffset) {
      this.defaultOffset = defaultOffset;
      return this;
    }

    /**
     * 设置默认的pageNo，当构造时pageNo非法时使用默认值
     */
    public Builder setDefaultPageNo(int defaultPageNo) {
      this.defaultPageNo = defaultPageNo;
      return this;
    }

    public PaginationClause build() {
      return buildByOffset(0);
    }

    public PaginationClause buildByOffset(int offset) {
      if (defaultLimit != null && limit < 0) {
        limit = defaultLimit.intValue();
      }
      if (defaultOffset != null && offset < 0) {
        offset = defaultOffset.intValue();
      }
      Preconditions.checkArgument(limit >= 0, "limit(>=0): " + limit);
      Preconditions.checkArgument(offset >= 0, "offset(>=0): " + offset);
      return new OffsetLimit(limit, offset);
    }

    public PaginationClause buildByPageNo(int pageNo) {
      if (defaultLimit != null && limit < 0) {
        limit = defaultLimit.intValue();
      }
      if (defaultPageNo != null && pageNo <= 0) {
        pageNo = defaultPageNo.intValue();
      }
      Preconditions.checkArgument(limit >= 0, "limit(>=0): " + limit);
      Preconditions.checkArgument(pageNo > 0, "pageNo(>0): " + pageNo);
      return new PageNoLimit(limit, pageNo);
    }
  }

}
