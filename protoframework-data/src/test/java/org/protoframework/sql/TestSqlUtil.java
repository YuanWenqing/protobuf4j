package org.protoframework.sql;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author: yuanwq
 * @date: 2018/7/17
 */
public class TestSqlUtil {
  @Test
  public void testLike() {
    assertEquals("%a", SqlUtil.likeSuffix("a"));
    assertEquals("a%", SqlUtil.likePrefix("a"));
    assertEquals("%a%", SqlUtil.likeSub("a"));

    assertEquals("%%a%", SqlUtil.likePrefix("%a"));
  }
}
