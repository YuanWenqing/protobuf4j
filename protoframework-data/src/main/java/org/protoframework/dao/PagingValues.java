package org.protoframework.dao;

import java.util.List;

/**
 * 分页结果
 * <p>
 * @author: yuanwq
 * @date: 2018/1/9
 */
public class PagingValues<T> {

  private final List<T> values;
  private final int total;

  private PagingValues(List<T> values, int total) {
    this.values = values;
    this.total = total;
  }

  public static <T> PagingValues<T> of(List<T> values, int total) {
    return new PagingValues<>(values, total);
  }

  public static <T> PagingValues<T> of(List<T> values) {
    return new PagingValues<>(values, values.size());
  }

  public PagingValues<T> add(T result) {
    this.values.add(result);
    return this;
  }

  public PagingValues<T> addAll(List<T> values) {
    this.values.addAll(values);
    return this;
  }

  public List<T> getValues() {
    return values;
  }

  public int getTotal() {
    return total;
  }

}
