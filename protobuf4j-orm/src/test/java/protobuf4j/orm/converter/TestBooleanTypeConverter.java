package protobuf4j.orm.converter;

import org.junit.Test;

import static org.junit.Assert.*;

public class TestBooleanTypeConverter {
  ITypeConverter converter = new BooleanTypeConverter();

  @Test
  public void testToSqlValue() {
    assertEquals(0, converter.toSqlValue(0));
    assertEquals(1, converter.toSqlValue(1));
    assertEquals(1, converter.toSqlValue(2));

    assertEquals(0, converter.toSqlValue(false));
    assertEquals(1, converter.toSqlValue(true));

    try {
      converter.toSqlValue("");
      fail();
    } catch (TypeConversionException e) {
      System.out.println(e.getMessage());
    }
  }

  @Test
  public void testFromSqlValue() {
    assertEquals(Boolean.TRUE, converter.fromSqlValue(1));
    assertEquals(Boolean.TRUE, converter.fromSqlValue(2));
    assertEquals(Boolean.FALSE, converter.fromSqlValue(0));

    try {
      converter.fromSqlValue(null);
      fail();
    } catch (TypeConversionException e) {
      System.out.println(e.getMessage());
    }

  }
}
