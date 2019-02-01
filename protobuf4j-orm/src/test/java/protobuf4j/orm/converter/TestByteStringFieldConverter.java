package protobuf4j.orm.converter;

import com.google.protobuf.ByteString;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestByteStringFieldConverter {
  IFieldConverter converter = new ByteStringFieldConverter();

  @Test
  public void testToSqlValue() {
    assertEquals("", converter.toSqlValue(null, ByteString.EMPTY));
    String s = "sldjflsdjfl";
    ByteString byteString = ByteString.copyFromUtf8(s);
    assertEquals(s, converter.toSqlValue(null, byteString));
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
    assertEquals(ByteString.EMPTY, converter.fromSqlValue(null, ""));
    String s = "sldjflsdjfl";
    ByteString byteString = ByteString.copyFromUtf8(s);
    assertEquals(byteString, converter.fromSqlValue(null, s));
    assertEquals(ByteString.EMPTY, converter.fromSqlValue(null, null));

    try {
      converter.fromSqlValue(null, 1);
      fail();
    } catch (FieldConversionException e) {
      System.out.println(e.getMessage());
    }
  }
}
