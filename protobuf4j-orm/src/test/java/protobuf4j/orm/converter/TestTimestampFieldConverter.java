package protobuf4j.orm.converter;

import com.google.protobuf.util.Timestamps;
import org.junit.Test;

import java.sql.Timestamp;

import static org.junit.Assert.*;

public class TestTimestampFieldConverter {
  IFieldConverter converter = new TimestampFieldConverter();

  @Test
  public void testToSqlValue() {
    assertEquals(new Timestamp(0), converter.toSqlValue(null, 0));
    assertEquals(new Timestamp(1), converter.toSqlValue(null, 1));
    assertEquals(new Timestamp(0), converter.toSqlValue(null, Timestamps.fromMillis(0)));
    assertEquals(new Timestamp(1), converter.toSqlValue(null, Timestamps.fromMillis(1)));
    long now = System.currentTimeMillis();
    assertEquals(new Timestamp(now), converter.toSqlValue(null, now));

    try {
      converter.toSqlValue(null, null);
      fail();
    } catch (FieldConversionException e) {
      System.out.println(e.getMessage());
    }
  }

  @Test
  public void testFromSqlValue() {
    assertEquals(Timestamps.fromMillis(0), converter.fromSqlValue(null, null));
    assertEquals(Timestamps.fromMillis(0), converter.fromSqlValue(null, new Timestamp(0)));
    assertEquals(Timestamps.fromMillis(1), converter.fromSqlValue(null, new Timestamp(1)));
    long now = System.currentTimeMillis();
    assertEquals(Timestamps.fromMillis(now), converter.fromSqlValue(null, new Timestamp(now)));

    try {
      converter.fromSqlValue(null, "");
      fail();
    } catch (FieldConversionException e) {
      System.out.println(e.getMessage());
    }

  }
}
