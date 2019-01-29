package protobuf4j.orm.sql;

import com.google.common.collect.Lists;
import org.junit.Test;
import protobuf4j.orm.sql.expr.ArithmeticOp;
import protobuf4j.orm.sql.expr.LogicalOp;
import protobuf4j.orm.sql.expr.RelationalOp;
import protobuf4j.orm.sql.expr.Value;

import java.util.List;

import static org.junit.Assert.*;

/**
 * author: yuanwq
 * date: 2018/7/17
 */
public class TestValue {
  @Test
  public void testValue() {
    Value value = Value.of(1);
    System.out.println(value);

    assertNull(value.getField());
    assertEquals(1, value.getValue());

    assertTrue(value.comparePrecedence(ArithmeticOp.ADD) > 0);
    assertTrue(value.comparePrecedence(LogicalOp.AND) > 0);
    assertTrue(value.comparePrecedence(RelationalOp.EQ) > 0);

    assertEquals("?", value.toSqlTemplate(new StringBuilder()).toString());
    assertEquals("1", value.toSolidSql(new StringBuilder()).toString());
    List<ISqlValue> sqlValues = value.collectSqlValue(Lists.newArrayList());
    assertEquals(1, sqlValues.size());
    assertEquals(value, sqlValues.get(0));

    value = Value.of("a", "field");
    System.out.println(value);

    assertEquals("a", value.getValue());
    assertEquals("field", value.getField());

    assertTrue(value.comparePrecedence(ArithmeticOp.ADD) > 0);
    assertTrue(value.comparePrecedence(LogicalOp.AND) > 0);
    assertTrue(value.comparePrecedence(RelationalOp.EQ) > 0);

    assertEquals("?", value.toSqlTemplate(new StringBuilder()).toString());
    assertEquals("'a'", value.toSolidSql(new StringBuilder()).toString());
    sqlValues = value.collectSqlValue(Lists.newArrayList());
    assertEquals(1, sqlValues.size());
    assertEquals(value, sqlValues.get(0));
  }

  @Test
  public void testValueEmbed() {
    Value a = Value.of("A");
    Value b = Value.of(a, "field");
    assertNotEquals(a, b);
    assertNotEquals(a, b.getValue());
    assertTrue(a.getValue() instanceof String);
    assertTrue(b.getValue() instanceof String);
    assertEquals("field", b.getField());
  }
}
