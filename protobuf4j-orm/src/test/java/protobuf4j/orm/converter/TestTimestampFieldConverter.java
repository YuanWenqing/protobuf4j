package protobuf4j.orm.converter;

import com.google.protobuf.util.Timestamps;
import org.junit.Test;

import java.sql.Timestamp;

import static org.junit.Assert.*;

public class TestTimestampFieldConverter {
  IFieldConverter converter = new TimestampFieldConverter();

  @Test
  public void testToSqlValue() {
    assertEquals(new Timestamp(0), converter.toSqlValue(0));
    assertEquals(new Timestamp(1), converter.toSqlValue(1));
    assertEquals(new Timestamp(0), converter.toSqlValue(Timestamps.fromMillis(0)));
    assertEquals(new Timestamp(1), converter.toSqlValue(Timestamps.fromMillis(1)));
    long now = System.currentTimeMillis();
    assertEquals(new Timestamp(now), converter.toSqlValue(now));

    try {
      converter.toSqlValue(null);
      fail();
    } catch (FieldConversionException e) {
      System.out.println(e.getMessage());
    }
  }

  @Test
  public void testFromSqlValue() {
    assertEquals(Timestamps.fromMillis(0), converter.fromSqlValue(null));
    assertEquals(Timestamps.fromMillis(0), converter.fromSqlValue(new Timestamp(0)));
    assertEquals(Timestamps.fromMillis(1), converter.fromSqlValue(new Timestamp(1)));
    long now = System.currentTimeMillis();
    assertEquals(Timestamps.fromMillis(now), converter.fromSqlValue(new Timestamp(now)));

    try {
      converter.fromSqlValue("");
      fail();
    } catch (FieldConversionException e) {
      System.out.println(e.getMessage());
    }

  }
}
