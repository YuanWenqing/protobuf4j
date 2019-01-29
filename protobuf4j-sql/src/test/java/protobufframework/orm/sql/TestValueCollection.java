package protobufframework.orm.sql;

import com.google.common.collect.Lists;
import org.junit.Test;
import protobufframework.orm.sql.expr.*;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

/**
 * author: yuanwq
 * date: 2018/7/17
 */
public class TestValueCollection {
  @Test
  public void testValueCollection() {
    ValueCollection collection = ValueCollection.of(Collections.emptyList());
    System.out.println(collection);

    assertTrue(collection.isEmpty());
    assertNull(collection.getField());

    collection = ValueCollection.of(Lists.newArrayList(Value.of(1), 2), "a");
    System.out.println(collection);

    assertFalse(collection.isEmpty());
    assertEquals("a", collection.getField());
    assertEquals(2, collection.getValues().size());

    assertFalse(collection.getValues().get(0) instanceof Value);
    assertTrue(collection.getValues().get(0) instanceof Integer);
    assertEquals(1, collection.getValues().get(0));
    assertFalse(collection.getValues().get(1) instanceof Value);
    assertTrue(collection.getValues().get(1) instanceof Integer);
    assertEquals(2, collection.getValues().get(1));

    assertTrue(collection.comparePrecedence(ArithmeticOp.ADD) > 0);
    assertTrue(collection.comparePrecedence(LogicalOp.AND) > 0);
    assertTrue(collection.comparePrecedence(RelationalOp.EQ) > 0);

    assertEquals("(?,?)", collection.toSqlTemplate(new StringBuilder()).toString());
    assertEquals("(1,2)", collection.toSolidSql(new StringBuilder()).toString());
    List<ISqlValue> sqlValues = collection.collectSqlValue(Lists.newArrayList());
    assertEquals(2, sqlValues.size());
    assertEquals("a", sqlValues.get(0).getField());
    assertEquals(1, sqlValues.get(0).getValue());
    assertEquals("a", sqlValues.get(1).getField());
    assertEquals(2, sqlValues.get(1).getValue());
  }
}
