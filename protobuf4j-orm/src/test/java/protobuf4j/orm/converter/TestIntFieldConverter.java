package protobuf4j.orm.converter;

import org.junit.Test;

import static org.junit.Assert.*;

public class TestIntFieldConverter {
  IFieldConverter converter = new IntFieldConverter();

  @Test
  public void testToSqlValue() {
    assertEquals(0, converter.toSqlValue(null, 0));
    assertEquals(1, converter.toSqlValue(null, 1));
    assertEquals(-1, converter.toSqlValue(null, -1));
    assertEquals(Integer.MAX_VALUE, converter.toSqlValue(null, Integer.MAX_VALUE));
    assertEquals(Integer.MIN_VALUE, converter.toSqlValue(null, Integer.MIN_VALUE));

    try {
      converter.toSqlValue(null, null);
      fail();
    } catch (FieldConversionException e) {
      System.out.println(e.getMessage());
    }
  }

  @Test
  public void testFromSqlValue() {
    assertEquals(0, converter.fromSqlValue(null, null));
    assertEquals(0, converter.fromSqlValue(null, 0));
    assertEquals(1, converter.fromSqlValue(null, 1));
    assertEquals(-1, converter.fromSqlValue(null, -1));
    assertEquals(Integer.MAX_VALUE, converter.fromSqlValue(null, Integer.MAX_VALUE));
    assertEquals(Integer.MIN_VALUE, converter.fromSqlValue(null, Integer.MIN_VALUE));

    try {
      converter.fromSqlValue(null, "");
      fail();
    } catch (FieldConversionException e) {
      System.out.println(e.getMessage());
    }

  }
}
