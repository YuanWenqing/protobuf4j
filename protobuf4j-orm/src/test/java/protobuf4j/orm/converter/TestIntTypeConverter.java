package protobuf4j.orm.converter;

import org.junit.Test;

import static org.junit.Assert.*;

public class TestIntTypeConverter {
  ITypeConverter converter = new IntTypeConverter();

  @Test
  public void testToSqlValue() {
    assertEquals(0, converter.toSqlValue(0));
    assertEquals(1, converter.toSqlValue(1));
    assertEquals(-1, converter.toSqlValue(-1));
    assertEquals(Integer.MAX_VALUE, converter.toSqlValue(Integer.MAX_VALUE));
    assertEquals(Integer.MIN_VALUE, converter.toSqlValue(Integer.MIN_VALUE));

    try {
      converter.toSqlValue(null);
      fail();
    } catch (TypeConversionException e) {
      System.out.println(e.getMessage());
    }
  }

  @Test
  public void testFromSqlValue() {
    assertEquals(0, converter.fromSqlValue(0));
    assertEquals(1, converter.fromSqlValue(1));
    assertEquals(-1, converter.fromSqlValue(-1));
    assertEquals(Integer.MAX_VALUE, converter.fromSqlValue(Integer.MAX_VALUE));
    assertEquals(Integer.MIN_VALUE, converter.fromSqlValue(Integer.MIN_VALUE));

    try {
      converter.fromSqlValue("");
      fail();
    } catch (TypeConversionException e) {
      System.out.println(e.getMessage());
    }

  }
}
