package protobuf4j.orm.converter;

import org.junit.Test;

import static org.junit.Assert.*;

public class TestDoubleFieldConverter {
  IFieldValueConverter converter = new DoubleFieldConverter();

  @Test
  public void testToSqlValue() {
    assertEquals(0d, converter.toSqlValue(0));
    assertEquals(1d, converter.toSqlValue(1d));
    assertEquals(-1d, converter.toSqlValue(-1f));
    assertEquals(Long.MAX_VALUE + 0.0d, converter.toSqlValue(Long.MAX_VALUE));
    assertEquals(Integer.MIN_VALUE + 0.0d, converter.toSqlValue(Integer.MIN_VALUE + 0.0f));

    try {
      converter.toSqlValue(null);
      fail();
    } catch (FieldConversionException e) {
      System.out.println(e.getMessage());
    }
  }

  @Test
  public void testFromSqlValue() {
    assertEquals(0d, converter.fromSqlValue(null));
    assertEquals(0d, converter.fromSqlValue(0));
    assertEquals(1d, converter.fromSqlValue(1d));
    assertEquals(-1d, converter.fromSqlValue(-1f));
    assertEquals(Long.MAX_VALUE + 0.0d, converter.fromSqlValue(Long.MAX_VALUE));
    assertEquals(Integer.MIN_VALUE + 0.0d, converter.fromSqlValue(Integer.MIN_VALUE + 0.0f));

    try {
      converter.fromSqlValue("");
      fail();
    } catch (FieldConversionException e) {
      System.out.println(e.getMessage());
    }

  }
}
