package protobuf4j.orm.converter;

import org.junit.Test;

import static org.junit.Assert.*;

public class TestBooleanFieldConverter {
  IFieldConverter converter = new BooleanFieldConverter();

  @Test
  public void testToSqlValue() {
    assertEquals(0, converter.toSqlValue(null, 0));
    assertEquals(1, converter.toSqlValue(null, 1));
    assertEquals(1, converter.toSqlValue(null, 2));

    assertEquals(0, converter.toSqlValue(null, false));
    assertEquals(1, converter.toSqlValue(null, true));

    try {
      converter.toSqlValue(null, "");
      fail();
    } catch (FieldConversionException e) {
      System.out.println(e.getMessage());
    }
  }

  @Test
  public void testfromSqlValue() {
    assertEquals(Boolean.TRUE, converter.fromSqlValue(null, 1));
    assertEquals(Boolean.TRUE, converter.fromSqlValue(null, 2));
    assertEquals(Boolean.FALSE, converter.fromSqlValue(null, 0));
    assertEquals(Boolean.FALSE, converter.fromSqlValue(null, null));

    try {
      converter.fromSqlValue(null, "");
      fail();
    } catch (FieldConversionException e) {
      System.out.println(e.getMessage());
    }

  }
}
