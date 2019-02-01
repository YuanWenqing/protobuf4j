package protobuf4j.orm.converter;

import org.junit.Test;

import static org.junit.Assert.*;

public class TestLongFieldConverter {
  IFieldConverter converter = new LongFieldConverter();

  @Test
  public void testToSqlValue() {
    assertEquals(0L, converter.toSqlValue(null, 0));
    assertEquals(1L, converter.toSqlValue(null, 1L));
    assertEquals(-1L, converter.toSqlValue(null, -1));
    assertEquals(Long.MAX_VALUE, converter.toSqlValue(null, Long.MAX_VALUE));
    assertEquals(Long.MIN_VALUE, converter.toSqlValue(null, Long.MIN_VALUE));

    try {
      converter.toSqlValue(null, null);
      fail();
    } catch (FieldConversionException e) {
      System.out.println(e.getMessage());
    }
  }

  @Test
  public void testFromSqlValue() {
    assertEquals(0L, converter.fromSqlValue(null, null));
    assertEquals(0L, converter.fromSqlValue(null, 0L));
    assertEquals(1L, converter.fromSqlValue(null, 1));
    assertEquals(-1L, converter.fromSqlValue(null, -1));
    assertEquals(Long.MAX_VALUE, converter.fromSqlValue(null, Long.MAX_VALUE));
    assertEquals(Long.MIN_VALUE, converter.fromSqlValue(null, Long.MIN_VALUE));

    try {
      converter.fromSqlValue(null, "");
      fail();
    } catch (FieldConversionException e) {
      System.out.println(e.getMessage());
    }

  }
}
