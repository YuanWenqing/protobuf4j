package protobuf4j.orm.converter;

import org.junit.Test;

import static org.junit.Assert.*;

public class TestLongFieldConverter {
  IFieldValueConverter converter = new LongFieldConverter();

  @Test
  public void testToSqlValue() {
    assertEquals(0L, converter.toSqlValue(0));
    assertEquals(1L, converter.toSqlValue(1L));
    assertEquals(-1L, converter.toSqlValue(-1));
    assertEquals(Long.MAX_VALUE, converter.toSqlValue(Long.MAX_VALUE));
    assertEquals(Long.MIN_VALUE, converter.toSqlValue(Long.MIN_VALUE));

    try {
      converter.toSqlValue(null);
      fail();
    } catch (FieldConversionException e) {
      System.out.println(e.getMessage());
    }
  }

  @Test
  public void testFromSqlValue() {
    assertEquals(0L, converter.fromSqlValue(null));
    assertEquals(0L, converter.fromSqlValue(0L));
    assertEquals(1L, converter.fromSqlValue(1));
    assertEquals(-1L, converter.fromSqlValue(-1));
    assertEquals(Long.MAX_VALUE, converter.fromSqlValue(Long.MAX_VALUE));
    assertEquals(Long.MIN_VALUE, converter.fromSqlValue(Long.MIN_VALUE));

    try {
      converter.fromSqlValue("");
      fail();
    } catch (FieldConversionException e) {
      System.out.println(e.getMessage());
    }

  }
}
