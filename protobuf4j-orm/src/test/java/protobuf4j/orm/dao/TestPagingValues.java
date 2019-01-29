package protobuf4j.orm.dao;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.*;

/**
 * author: yuanwq
 * date: 2018/7/23
 */
public class TestPagingValues {
  @Test
  public void test() {
    PagingValues<Integer> values = PagingValues.of(Collections.emptyList());
    assertEquals(Collections.emptyList(), values.getValues());
    assertEquals(0, values.getTotal());

    values = PagingValues.of(Lists.newArrayList(1, 2, 3), 10);
    assertEquals(Lists.newArrayList(1, 2, 3), values.getValues());
    assertEquals(10, values.getTotal());

    values.add(4);
    assertEquals(Lists.newArrayList(1, 2, 3, 4), values.getValues());
    values.addAll(Lists.newArrayList(5, 6));
    assertEquals(Lists.newArrayList(1, 2, 3, 4, 5, 6), values.getValues());

  }
}
