package protobuf4j.orm.converter;

import com.google.protobuf.ByteString;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestByteStringFieldConverter {
  IFieldConverter converter = new ByteStringFieldConverter();

  @Test
  public void testToSqlValue() {
    assertEquals("", converter.toSqlValue(ByteString.EMPTY));
    String s = "sldjflsdjfl";
    ByteString byteString = ByteString.copyFromUtf8(s);
    assertEquals(s, converter.toSqlValue(byteString));

    try {
      converter.toSqlValue("");
      fail();
    } catch (TypeConversionException e) {
      System.out.println(e.getMessage());
    }
  }

  @Test
  public void testFromSqlValue() {
    assertEquals(ByteString.EMPTY, converter.fromSqlValue(""));
    String s = "sldjflsdjfl";
    ByteString byteString = ByteString.copyFromUtf8(s);
    assertEquals(byteString, converter.fromSqlValue(s));
    assertEquals(ByteString.EMPTY, converter.fromSqlValue(null));

    try {
      converter.fromSqlValue(1);
      fail();
    } catch (TypeConversionException e) {
      System.out.println(e.getMessage());
    }

  }
}
