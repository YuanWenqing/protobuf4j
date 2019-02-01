package protobuf4j.orm.converter;

import org.junit.Test;

import static org.junit.Assert.*;

public class TestStringFieldConverter {
  IFieldConverter converter = new StringFieldConverter();

  @Test
  public void testToSqlValue() {
    assertEquals("", converter.toSqlValue(null, ""));
    assertEquals("", converter.toSqlValue(null, null));
    try {
      converter.toSqlValue(null, 1);
      fail();
    } catch (FieldConversionException e) {
      System.out.println(e.getMessage());
    }
  }

  @Test
  public void testFromSqlValue() {
    assertEquals("", converter.fromSqlValue(null, ""));
    assertEquals("1", converter.fromSqlValue(null, "1"));
    assertEquals("", converter.fromSqlValue(null, null));

    try {
      converter.fromSqlValue(null, 1);
      fail();
    } catch (FieldConversionException e) {
      System.out.println(e.getMessage());
    }

  }
}
