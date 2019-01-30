package protobuf4j.orm.converter;

import org.junit.Test;

import static org.junit.Assert.*;

public class TestStringFieldConverter {
  IFieldValueConverter converter = new StringFieldConverter();

  @Test
  public void testToSqlValue() {
    assertEquals("", converter.toSqlValue(""));
    assertEquals("", converter.toSqlValue(null));
    try {
      converter.toSqlValue(1);
      fail();
    } catch (FieldConversionException e) {
      System.out.println(e.getMessage());
    }
  }

  @Test
  public void testFromSqlValue() {
    assertEquals("", converter.fromSqlValue(""));
    assertEquals("1", converter.fromSqlValue("1"));
    assertEquals("", converter.fromSqlValue(null));

    try {
      converter.fromSqlValue(1);
      fail();
    } catch (FieldConversionException e) {
      System.out.println(e.getMessage());
    }

  }
}
