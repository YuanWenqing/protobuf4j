package protobuf4j.orm.dao;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.protobuf.ByteString;
import com.google.protobuf.Descriptors;
import com.google.protobuf.MapEntry;
import org.junit.Test;
import org.springframework.dao.TypeMismatchDataAccessException;
import protobuf4j.core.ProtoMessageHelper;
import protobuf4j.test.proto.TestModel;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * author: yuanwq
 * date: 2018/7/23
 */
public class TestProtoSqlHandler {
  private ProtoMessageHelper<TestModel.MsgA> helperA =
      ProtoMessageHelper.getHelper(TestModel.MsgA.class);
  ProtoSqlHandler<TestModel.MsgA> sqlHandlerA = new ProtoSqlHandler<>(TestModel.MsgA.class);
  private ProtoMessageHelper<TestModel.MsgB> helperB =
      ProtoMessageHelper.getHelper(TestModel.MsgB.class);

  @Test
  public void testResolveSqlValueTypeSimple() {
    assertEquals(Integer.class,
        sqlHandlerA.resolveSqlValueType(helperA.getFieldDescriptor("int32")));
    assertEquals(Long.class, sqlHandlerA.resolveSqlValueType(helperA.getFieldDescriptor("int64")));
    assertEquals(Float.class, sqlHandlerA.resolveSqlValueType(helperA.getFieldDescriptor("float")));
    assertEquals(Double.class,
        sqlHandlerA.resolveSqlValueType(helperA.getFieldDescriptor("double")));
    assertEquals(Integer.class,
        sqlHandlerA.resolveSqlValueType(helperA.getFieldDescriptor("bool")));
    assertEquals(String.class,
        sqlHandlerA.resolveSqlValueType(helperA.getFieldDescriptor("string")));
    assertEquals(Integer.class,
        sqlHandlerA.resolveSqlValueType(helperA.getFieldDescriptor("enuma")));
    assertEquals(String.class,
        sqlHandlerA.resolveSqlValueType(helperA.getFieldDescriptor("bytes")));

    try {
      sqlHandlerA.resolveSqlValueType(helperA.getFieldDescriptor("msgb"));
      fail();
    } catch (TypeMismatchDataAccessException e) {
      System.out.println(e.getMessage());
    }
  }

  @Test
  public void testResolveSqlValueTypeRepeated() {
    assertEquals(String.class,
        sqlHandlerA.resolveSqlValueType(helperA.getFieldDescriptor("int32_arr")));
    assertEquals(String.class,
        sqlHandlerA.resolveSqlValueType(helperA.getFieldDescriptor("int64_arr")));
    assertEquals(String.class,
        sqlHandlerA.resolveSqlValueType(helperA.getFieldDescriptor("float_arr")));
    assertEquals(String.class,
        sqlHandlerA.resolveSqlValueType(helperA.getFieldDescriptor("double_arr")));
    assertEquals(String.class,
        sqlHandlerA.resolveSqlValueType(helperA.getFieldDescriptor("bool_arr")));
    assertEquals(String.class,
        sqlHandlerA.resolveSqlValueType(helperA.getFieldDescriptor("string_arr")));
    assertEquals(String.class,
        sqlHandlerA.resolveSqlValueType(helperA.getFieldDescriptor("enuma_arr")));
    assertEquals(String.class,
        sqlHandlerA.resolveSqlValueType(helperA.getFieldDescriptor("bytes_arr")));
    assertEquals(String.class,
        sqlHandlerA.resolveSqlValueType(helperA.getFieldDescriptor("msgb_arr")));
  }

  @Test
  public void testResolveSqlValueTypeMap() {
    assertEquals(String.class,
        sqlHandlerA.resolveSqlValueType(helperA.getFieldDescriptor("int32_map")));
    assertEquals(String.class,
        sqlHandlerA.resolveSqlValueType(helperA.getFieldDescriptor("int64_map")));
    assertEquals(String.class,
        sqlHandlerA.resolveSqlValueType(helperA.getFieldDescriptor("float_map")));
    assertEquals(String.class,
        sqlHandlerA.resolveSqlValueType(helperA.getFieldDescriptor("double_map")));
    assertEquals(String.class,
        sqlHandlerA.resolveSqlValueType(helperA.getFieldDescriptor("bool_map")));
    assertEquals(String.class,
        sqlHandlerA.resolveSqlValueType(helperA.getFieldDescriptor("string_map")));
    assertEquals(String.class,
        sqlHandlerA.resolveSqlValueType(helperA.getFieldDescriptor("enuma_map")));
    assertEquals(String.class,
        sqlHandlerA.resolveSqlValueType(helperA.getFieldDescriptor("bytes_map")));
    assertEquals(String.class,
        sqlHandlerA.resolveSqlValueType(helperA.getFieldDescriptor("msgb_map")));
  }

  @Test
  public void testTimestamp() {
//    assertFalse(sqlHandlerA.isTimestampField(helperA.getFieldDescriptor("float")));
//    assertFalse(sqlHandlerA.isTimestampField(helperA.getFieldDescriptor("int64")));
//
//    ProtoSqlHandler<TestModel.MsgA> sqlHandlerA = new ProtoSqlHandler<>(TestModel.MsgA.class);
//
//    Descriptors.FieldDescriptor timeFd = helperB.getFieldDescriptor("create_time");
//    assertTrue(sqlHandlerA.isTimestampField(timeFd));
//    assertEquals(Timestamp.class, sqlHandlerA.resolveSqlValueType(timeFd));
//
//    long millis = System.currentTimeMillis();
//    Timestamp timestamp = new Timestamp(millis);
//    assertEquals(timestamp, sqlHandlerA.toSqlValue(TestModel.MsgB.class, "create_time", millis));
//    assertEquals(timestamp,
//        sqlHandlerA.toSqlValue(TestModel.MsgB.class, "create_time", timestamp));
//    assertEquals(millis, sqlHandlerA.fromSqlValue(TestModel.MsgB.class, "create_time", timestamp));
//
//    Date sqlDate = new Date(millis);
//    assertEquals(sqlDate, sqlHandlerA.toSqlValue(TestModel.MsgB.class, "create_time", sqlDate));
//    java.util.Date utilDate = new java.util.Date(millis);
//    assertEquals(utilDate, sqlHandlerA.toSqlValue(TestModel.MsgB.class, "create_time", utilDate));
//
//    try {
//      sqlHandlerA.toSqlValue(helperB.getFieldDescriptor("create_time"), "");
//      fail();
//    } catch (TypeMismatchDataAccessException e) {
//      System.out.println(e.getMessage());
//    }
  }

  @Test
  public void testToSqlValueSimple() {
    assertEquals(1, sqlHandlerA.toSqlValue(helperA.getFieldDescriptor("int32"), 1));
    assertEquals(1L, sqlHandlerA.toSqlValue(helperA.getFieldDescriptor("int64"), 1));
    assertEquals(1.1f, sqlHandlerA.toSqlValue(helperA.getFieldDescriptor("float"), 1.1));
    assertEquals(1.1, sqlHandlerA.toSqlValue(helperA.getFieldDescriptor("double"), 1.1));
    assertEquals(1, sqlHandlerA.toSqlValue(helperA.getFieldDescriptor("bool"), true));
    assertEquals(0, sqlHandlerA.toSqlValue(helperA.getFieldDescriptor("bool"), false));
    assertEquals("1", sqlHandlerA.toSqlValue(helperA.getFieldDescriptor("string"), "1"));
    assertEquals("str", sqlHandlerA.toSqlValue(helperA.getFieldDescriptor("string"), "str"));
    assertEquals(0,
        sqlHandlerA.toSqlValue(helperA.getFieldDescriptor("enuma"), TestModel.EnumA.EA0));
    assertEquals(2,
        sqlHandlerA.toSqlValue(helperA.getFieldDescriptor("enuma"), TestModel.EnumA.EA2));
    assertEquals(4,
        sqlHandlerA.toSqlValue(helperA.getFieldDescriptor("enuma"), TestModel.EnumA.EA4));

    // 兼容
    assertEquals(1L, sqlHandlerA.toSqlValue(helperA.getFieldDescriptor("int64"), 1));
    assertEquals(1f, sqlHandlerA.toSqlValue(helperA.getFieldDescriptor("float"), 1));
    assertEquals(1, sqlHandlerA.toSqlValue(helperA.getFieldDescriptor("bool"), 1));
    assertEquals(2, sqlHandlerA.toSqlValue(helperA.getFieldDescriptor("enuma"), 2));
  }

  @Test
  public void testToSqlValueTypeMismatch() {
    try {
      sqlHandlerA.toSqlValue(helperA.getFieldDescriptor("int32"), 1.1);
      fail();
    } catch (TypeMismatchDataAccessException e) {
      System.out.println(e.getMessage());
    }
    try {
      sqlHandlerA.toSqlValue(helperA.getFieldDescriptor("int64"), "");
      fail();
    } catch (TypeMismatchDataAccessException e) {
      System.out.println(e.getMessage());
    }
    try {
      sqlHandlerA.toSqlValue(helperA.getFieldDescriptor("float"), "");
      fail();
    } catch (TypeMismatchDataAccessException e) {
      System.out.println(e.getMessage());
    }
    try {
      sqlHandlerA.toSqlValue(helperA.getFieldDescriptor("double"), "");
      fail();
    } catch (TypeMismatchDataAccessException e) {
      System.out.println(e.getMessage());
    }
    try {
      sqlHandlerA.toSqlValue(helperA.getFieldDescriptor("bool"), "");
      fail();
    } catch (TypeMismatchDataAccessException e) {
      System.out.println(e.getMessage());
    }
    try {
      sqlHandlerA.toSqlValue(helperA.getFieldDescriptor("enuma"), "");
      fail();
    } catch (TypeMismatchDataAccessException e) {
      System.out.println(e.getMessage());
    }
  }

  @Test
  public void testToSqlValueRepeated() {
    assertEquals("",
        sqlHandlerA.toSqlValue(helperA.getFieldDescriptor("int32_arr"), Lists.newArrayList()));
    assertEquals("",
        sqlHandlerA.toSqlValue(helperA.getFieldDescriptor("int32_arr"), Sets.newHashSet()));
    assertEquals("1,2,",
        sqlHandlerA.toSqlValue(helperA.getFieldDescriptor("int32_arr"), Lists.newArrayList(1, 2)));
    assertEquals("1,2,", sqlHandlerA
        .toSqlValue(helperA.getFieldDescriptor("int64_arr"), Lists.newArrayList(1L, 2L)));
    assertEquals("1.1,2.2,", sqlHandlerA
        .toSqlValue(helperA.getFieldDescriptor("float_arr"), Lists.newArrayList(1.1f, 2.2f)));
    assertEquals("1.1,2.2,", sqlHandlerA
        .toSqlValue(helperA.getFieldDescriptor("double_arr"), Lists.newArrayList(1.1, 2.2)));
    assertEquals("1,0,", sqlHandlerA
        .toSqlValue(helperA.getFieldDescriptor("bool_arr"), Lists.newArrayList(true, false)));
    assertEquals(",a,", sqlHandlerA
        .toSqlValue(helperA.getFieldDescriptor("string_arr"), Lists.newArrayList("", "a")));
    assertEquals(",",
        sqlHandlerA.toSqlValue(helperA.getFieldDescriptor("string_arr"), Lists.newArrayList("")));
    assertEquals("%2c,",
        sqlHandlerA.toSqlValue(helperA.getFieldDescriptor("string_arr"), Lists.newArrayList(",")));
    assertEquals("%2c%25,",
        sqlHandlerA.toSqlValue(helperA.getFieldDescriptor("string_arr"), Lists.newArrayList(",%")));
    assertEquals("2,0,", sqlHandlerA.toSqlValue(helperA.getFieldDescriptor("enuma_arr"),
        Lists.newArrayList(TestModel.EnumA.EA2, TestModel.EnumA.EA0)));

    // 兼容
    assertEquals("1,2,",
        sqlHandlerA.toSqlValue(helperA.getFieldDescriptor("int64_arr"), Lists.newArrayList(1, 2)));
    assertEquals("1.0,2.0,",
        sqlHandlerA.toSqlValue(helperA.getFieldDescriptor("double_arr"), Lists.newArrayList(1, 2)));
    assertEquals("0,1,",
        sqlHandlerA.toSqlValue(helperA.getFieldDescriptor("bool_arr"), Lists.newArrayList(0, 1)));
    assertEquals("2,",
        sqlHandlerA.toSqlValue(helperA.getFieldDescriptor("enuma_arr"), Lists.newArrayList(2)));

    try {
      sqlHandlerA.toSqlValue(helperA.getFieldDescriptor("int32_arr"), 1);
      fail();
    } catch (TypeMismatchDataAccessException e) {
      System.out.println(e.getMessage());
    }
    try {
      sqlHandlerA.toSqlValue(helperA.getFieldDescriptor("bytes_arr"), Lists.newArrayList());
      fail();
    } catch (TypeMismatchDataAccessException e) {
      System.out.println(e.getMessage());
    }
    try {
      sqlHandlerA.toSqlValue(helperA.getFieldDescriptor("msgb_arr"), Lists.newArrayList());
      fail();
    } catch (TypeMismatchDataAccessException e) {
      System.out.println(e.getMessage());
    }
  }

  @SuppressWarnings({"rawtype", "unchecked"})
  private Map map(Object... kv) {
    Map map = new LinkedHashMap();
    if (kv == null || kv.length == 0) {
      return map;
    }
    for (int i = 0; i < kv.length; i += 2) {
      map.put(kv[i], kv[i + 1]);
    }
    return map;
  }

  @Test
  public void testToSqlValueMap() {
    // map
    assertEquals("", sqlHandlerA.toSqlValue(helperA.getFieldDescriptor("int32_map"), map()));
    assertEquals("a:1;b:2;",
        sqlHandlerA.toSqlValue(helperA.getFieldDescriptor("int32_map"), map("a", 1, "b", 2)));
    assertEquals("1:1;",
        sqlHandlerA.toSqlValue(helperA.getFieldDescriptor("bool_map"), map(1, true)));
    assertEquals(":a;",
        sqlHandlerA.toSqlValue(helperA.getFieldDescriptor("string_map"), map("", "a")));
    assertEquals("%3a:%3b;",
        sqlHandlerA.toSqlValue(helperA.getFieldDescriptor("string_map"), map(":", ";")));
    assertEquals("%3a:%25;",
        sqlHandlerA.toSqlValue(helperA.getFieldDescriptor("string_map"), map(":", "%")));
    assertEquals("a:0;", sqlHandlerA
        .toSqlValue(helperA.getFieldDescriptor("enuma_map"), map("a", TestModel.EnumA.EA0)));

    // list
    TestModel.MsgA msga =
        TestModel.MsgA.newBuilder().putInt64Map("a", 1).putInt64Map("b", 2).build();
    assertEquals("a:1;b:2;",
        sqlHandlerA.toSqlValue("int64_map", helperA.getFieldValue(msga, "int64_map")));

    // 兼容
    assertEquals(":2;",
        sqlHandlerA.toSqlValue(helperA.getFieldDescriptor("int64_map"), map("", 2)));
    assertEquals("a:2.0;",
        sqlHandlerA.toSqlValue(helperA.getFieldDescriptor("double_map"), map("a", 2)));
    assertEquals("0:1;", sqlHandlerA.toSqlValue(helperA.getFieldDescriptor("bool_map"), map(0, 1)));
    assertEquals("a:2;",
        sqlHandlerA.toSqlValue(helperA.getFieldDescriptor("enuma_map"), map("a", 2)));

    try {
      sqlHandlerA.toSqlValue(helperA.getFieldDescriptor("int32_map"), 1);
      fail();
    } catch (TypeMismatchDataAccessException e) {
      System.out.println(e.getMessage());
    }
    try {
      sqlHandlerA.toSqlValue(helperA.getFieldDescriptor("bytes_map"), map());
      fail();
    } catch (TypeMismatchDataAccessException e) {
      System.out.println(e.getMessage());
    }
    try {
      sqlHandlerA.toSqlValue(helperA.getFieldDescriptor("msgb_map"), map());
      fail();
    } catch (TypeMismatchDataAccessException e) {
      System.out.println(e.getMessage());
    }
  }

  @Test
  @SuppressWarnings("rawtype")
  public void testFromSqlValue() {
    assertEquals(1, sqlHandlerA.fromSqlValue("int32", 1));
    assertEquals(1L, sqlHandlerA.fromSqlValue("int64", 1));
    assertEquals(1L, sqlHandlerA.fromSqlValue("int64", 1L));
    assertEquals(1f, sqlHandlerA.fromSqlValue("float", 1));
    assertEquals(1f, sqlHandlerA.fromSqlValue("float", 1f));
    assertEquals(1d, sqlHandlerA.fromSqlValue("double", 1));
    assertEquals(1.0, sqlHandlerA.fromSqlValue("double", 1.0));
    assertEquals(true, sqlHandlerA.fromSqlValue("bool", 1));
    assertEquals(false, sqlHandlerA.fromSqlValue("bool", 0));
    assertEquals(TestModel.EnumA.EA0.getValueDescriptor(), sqlHandlerA.fromSqlValue("enuma", 0));
    ByteString bytes = ByteString.copyFrom(getClass().getName().getBytes());
    assertEquals(bytes, sqlHandlerA.fromSqlValue("bytes", bytes.toStringUtf8()));

    try {
      sqlHandlerA.fromSqlValue("bool", 1.0);
      fail();
    } catch (TypeMismatchDataAccessException e) {
      System.out.println(e.getMessage());
    }
    try {
      sqlHandlerA.fromSqlValue("msgb", "");
      fail();
    } catch (TypeMismatchDataAccessException e) {
      System.out.println(e.getMessage());
    }
  }

  @Test
  @SuppressWarnings("rawtype")
  public void testFromSqlValueRepeated() {

    List enumas = (List) sqlHandlerA.fromSqlValue(helperA.getFieldDescriptor("enuma_arr"), "0,2");
    assertEquals(2, enumas.size());
    assertTrue(enumas.get(0) instanceof Descriptors.EnumValueDescriptor);
    assertEquals(TestModel.EnumA.EA0.name(),
        ((Descriptors.EnumValueDescriptor) enumas.get(0)).getName());

    assertEquals(Lists.newArrayList(1),
        sqlHandlerA.fromSqlValue(helperA.getFieldDescriptor("int32_arr"), "1,"));
    assertEquals(Lists.newArrayList(1L),
        sqlHandlerA.fromSqlValue(helperA.getFieldDescriptor("int64_arr"), "1"));
    assertEquals(Lists.newArrayList(1f),
        sqlHandlerA.fromSqlValue(helperA.getFieldDescriptor("float_arr"), "1"));
    assertEquals(Lists.newArrayList(1.0),
        sqlHandlerA.fromSqlValue(helperA.getFieldDescriptor("double_arr"), "1"));
    assertEquals(Lists.newArrayList(true),
        sqlHandlerA.fromSqlValue(helperA.getFieldDescriptor("bool_arr"), "1"));
    assertEquals(Lists.newArrayList(false),
        sqlHandlerA.fromSqlValue(helperA.getFieldDescriptor("bool_arr"), "0"));

    assertEquals(Lists.newArrayList(),
        sqlHandlerA.fromSqlValue(helperA.getFieldDescriptor("string_arr"), null));
    assertEquals(Lists.newArrayList(),
        sqlHandlerA.fromSqlValue(helperA.getFieldDescriptor("string_arr"), ""));
    assertEquals(Lists.newArrayList(""),
        sqlHandlerA.fromSqlValue(helperA.getFieldDescriptor("string_arr"), ","));
    assertEquals(Lists.newArrayList("a"),
        sqlHandlerA.fromSqlValue(helperA.getFieldDescriptor("string_arr"), "a"));
    assertEquals(Lists.newArrayList(","),
        sqlHandlerA.fromSqlValue(helperA.getFieldDescriptor("string_arr"), "%2c,"));
    assertEquals(Lists.newArrayList(","),
        sqlHandlerA.fromSqlValue(helperA.getFieldDescriptor("string_arr"), "%2c"));
    assertEquals(Lists.newArrayList("%"),
        sqlHandlerA.fromSqlValue(helperA.getFieldDescriptor("string_arr"), "%25,"));

    try {
      sqlHandlerA.fromSqlValue("int32_arr", 1);
      fail();
    } catch (TypeMismatchDataAccessException e) {
      System.out.println(e.getMessage());
    }
    try {
      sqlHandlerA.fromSqlValue("bytes_arr", "a");
      fail();
    } catch (TypeMismatchDataAccessException e) {
      System.out.println(e.getMessage());
    }
    try {
      sqlHandlerA.fromSqlValue("msgb_arr", "a");
      fail();
    } catch (TypeMismatchDataAccessException e) {
      System.out.println(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testFromSqlValueMap() {
    assertEquals(Collections.emptyList(), sqlHandlerA.fromSqlValue("int32_map", null));

    List<MapEntry> mapEntries;

    mapEntries =
        (List<MapEntry>) sqlHandlerA.fromSqlValue(helperA.getFieldDescriptor("int64_map"), "");
    assertEquals(0, mapEntries.size());

    mapEntries =
        (List<MapEntry>) sqlHandlerA.fromSqlValue(helperA.getFieldDescriptor("int64_map"), "a:1");
    assertEquals(1, mapEntries.size());
    assertEquals("a", mapEntries.get(0).getKey());
    assertEquals(1L, mapEntries.get(0).getValue());

    mapEntries =
        (List<MapEntry>) sqlHandlerA.fromSqlValue(helperA.getFieldDescriptor("int64_map"), "a:1;");
    assertEquals(1, mapEntries.size());
    assertEquals("a", mapEntries.get(0).getKey());
    assertEquals(1L, mapEntries.get(0).getValue());

    try {
      sqlHandlerA.fromSqlValue("int32_map", 1);
      fail();
    } catch (TypeMismatchDataAccessException e) {
      System.out.println(e.getMessage());
    }
    try {
      sqlHandlerA.fromSqlValue("bytes_map", "a:1");
      fail();
    } catch (TypeMismatchDataAccessException e) {
      System.out.println(e.getMessage());
    }
    try {
      sqlHandlerA.fromSqlValue("msgb_map", "a:1");
      fail();
    } catch (TypeMismatchDataAccessException e) {
      System.out.println(e.getMessage());
    }
  }

}
