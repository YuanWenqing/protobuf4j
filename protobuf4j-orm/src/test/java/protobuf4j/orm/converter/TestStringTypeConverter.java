package protobuf4j.orm.converter;

import org.junit.Test;

import static org.junit.Assert.*;

public class TestStringTypeConverter {
  ITypeConverter converter = new StringTypeConverter();

  @Test
  public void testToSqlValue() {
    assertEquals("", converter.toSqlValue(""));
    assertEquals("1", converter.toSqlValue(1));
  }

  @Test
  public void testFromSqlValue() {
    assertEquals("", converter.fromSqlValue(""));
    assertEquals("1", converter.fromSqlValue("1"));

    try {
      converter.fromSqlValue(null);
      fail();
    } catch (TypeConversionException e) {
      System.out.println(e.getMessage());
    }

  }
}
