package protobuf4j.orm.converter;

import org.junit.Test;

import static org.junit.Assert.*;

public class TestFloatTypeConverter {
  ITypeConverter converter = new FloatTypeConverter();

  @Test
  public void testToSqlValue() {
    assertEquals(0f, converter.toSqlValue(0));
    assertEquals(1f, converter.toSqlValue(1d));
    assertEquals(-1f, converter.toSqlValue(-1f));
    assertEquals(Long.MAX_VALUE + 0.0f, converter.toSqlValue(Long.MAX_VALUE));
    assertEquals(Integer.MIN_VALUE + 0.0f, converter.toSqlValue(Integer.MIN_VALUE + 0.0f));

    try {
      converter.toSqlValue(null);
      fail();
    } catch (TypeConversionException e) {
      System.out.println(e.getMessage());
    }
  }

  @Test
  public void testFromSqlValue() {
    assertEquals(0f, converter.fromSqlValue(0));
    assertEquals(1f, converter.fromSqlValue(1d));
    assertEquals(-1f, converter.fromSqlValue(-1f));
    assertEquals(Long.MAX_VALUE + 0.0f, converter.fromSqlValue(Long.MAX_VALUE));
    assertEquals(Integer.MIN_VALUE + 0.0f, converter.fromSqlValue(Integer.MIN_VALUE + 0.0f));

    try {
      converter.fromSqlValue("");
      fail();
    } catch (TypeConversionException e) {
      System.out.println(e.getMessage());
    }

  }
}
