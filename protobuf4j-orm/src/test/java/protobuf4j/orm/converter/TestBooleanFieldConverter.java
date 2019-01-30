package protobuf4j.orm.converter;

import org.junit.Test;

import static org.junit.Assert.*;

public class TestBooleanFieldConverter {
  IFieldConverter converter = new BooleanFieldConverter();

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
    } catch (FieldConversionException e) {
      System.out.println(e.getMessage());
    }
  }

  @Test
  public void testFromSqlValue() {
    assertEquals(Boolean.TRUE, converter.fromSqlValue(1));
    assertEquals(Boolean.TRUE, converter.fromSqlValue(2));
    assertEquals(Boolean.FALSE, converter.fromSqlValue(0));
    assertEquals(Boolean.FALSE, converter.fromSqlValue(null));

    try {
      converter.fromSqlValue("");
      fail();
    } catch (FieldConversionException e) {
      System.out.println(e.getMessage());
    }

  }
}
