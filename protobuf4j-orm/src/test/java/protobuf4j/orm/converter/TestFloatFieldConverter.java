package protobuf4j.orm.converter;

import org.junit.Test;

import static org.junit.Assert.*;

public class TestFloatFieldConverter {
  IFieldConverter converter = new FloatFieldConverter();

  @Test
  public void testToSqlValue() {
    assertEquals(0f, converter.toSqlValue(null, 0));
    assertEquals(1f, converter.toSqlValue(null, 1d));
    assertEquals(-1f, converter.toSqlValue(null, -1f));
    assertEquals(Long.MAX_VALUE + 0.0f, converter.toSqlValue(null, Long.MAX_VALUE));
    assertEquals(Integer.MIN_VALUE + 0.0f, converter.toSqlValue(null, Integer.MIN_VALUE + 0.0f));

    try {
      converter.toSqlValue(null, null);
      fail();
    } catch (FieldConversionException e) {
      System.out.println(e.getMessage());
    }
  }

  @Test
  public void testFromSqlValue() {
    assertEquals(0f, converter.fromSqlValue(null, null));
    assertEquals(0f, converter.fromSqlValue(null, 0));
    assertEquals(1f, converter.fromSqlValue(null, 1d));
    assertEquals(-1f, converter.fromSqlValue(null, -1f));
    assertEquals(Long.MAX_VALUE + 0.0f, converter.fromSqlValue(null, Long.MAX_VALUE));
    assertEquals(Integer.MIN_VALUE + 0.0f, converter.fromSqlValue(null, Integer.MIN_VALUE + 0.0f));

    try {
      converter.fromSqlValue(null, "");
      fail();
    } catch (FieldConversionException e) {
      System.out.println(e.getMessage());
    }

  }
}
