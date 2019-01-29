package protobufframework.orm.dao;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.protobuf.Descriptors;
import com.google.protobuf.MapEntry;
import org.junit.Test;
import org.springframework.dao.TypeMismatchDataAccessException;
import protobufframework.core.ProtoMessageHelper;
import protobufframework.test.proto.TestModel;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * author: yuanwq
 * date: 2018/7/23
 */
public class TestProtoSqlConverter {
  private ProtoSqlConverter sqlConverter = ProtoSqlConverter.getInstance();
  private ProtoMessageHelper<TestModel.MsgA> helperA =
      ProtoMessageHelper.getHelper(TestModel.MsgA.class);
  private ProtoMessageHelper<TestModel.MsgB> helperB =
      ProtoMessageHelper.getHelper(TestModel.MsgB.class);

  @Test
  public void testTableName() {
    assertEquals("msg_a", sqlConverter.tableName(TestModel.MsgA.class));
  }

  @Test
  public void testResolveSqlValueTypeSimple() {
    assertEquals(int.class, sqlConverter.resolveSqlValueType(helperA.getFieldDescriptor("int32")));
    assertEquals(long.class, sqlConverter.resolveSqlValueType(helperA.getFieldDescriptor("int64")));
    assertEquals(float.class,
        sqlConverter.resolveSqlValueType(helperA.getFieldDescriptor("float")));
    assertEquals(double.class,
        sqlConverter.resolveSqlValueType(helperA.getFieldDescriptor("double")));
    assertEquals(int.class, sqlConverter.resolveSqlValueType(helperA.getFieldDescriptor("bool")));
    assertEquals(String.class,
        sqlConverter.resolveSqlValueType(helperA.getFieldDescriptor("string")));
    assertEquals(int.class, sqlConverter.resolveSqlValueType(helperA.getFieldDescriptor("enuma")));

    try {
      sqlConverter.resolveSqlValueType(helperA.getFieldDescriptor("bytes"));
      fail();
    } catch (TypeMismatchDataAccessException e) {
      System.out.println(e.getMessage());
    }
    try {
      sqlConverter.resolveSqlValueType(helperA.getFieldDescriptor("msgb"));
      fail();
    } catch (TypeMismatchDataAccessException e) {
      System.out.println(e.getMessage());
    }
  }

  @Test
  public void testResolveSqlValueTypeRepeated() {
    assertEquals(String.class,
        sqlConverter.resolveSqlValueType(helperA.getFieldDescriptor("int32_arr")));
    assertEquals(String.class,
        sqlConverter.resolveSqlValueType(helperA.getFieldDescriptor("int64_arr")));
    assertEquals(String.class,
        sqlConverter.resolveSqlValueType(helperA.getFieldDescriptor("float_arr")));
    assertEquals(String.class,
        sqlConverter.resolveSqlValueType(helperA.getFieldDescriptor("double_arr")));
    assertEquals(String.class,
        sqlConverter.resolveSqlValueType(helperA.getFieldDescriptor("bool_arr")));
    assertEquals(String.class,
        sqlConverter.resolveSqlValueType(helperA.getFieldDescriptor("string_arr")));
    assertEquals(String.class,
        sqlConverter.resolveSqlValueType(helperA.getFieldDescriptor("enuma_arr")));
    assertEquals(String.class,
        sqlConverter.resolveSqlValueType(helperA.getFieldDescriptor("bytes_arr")));
    assertEquals(String.class,
        sqlConverter.resolveSqlValueType(helperA.getFieldDescriptor("msgb_arr")));
  }

  @Test
  public void testResolveSqlValueTypeMap() {
    assertEquals(String.class,
        sqlConverter.resolveSqlValueType(helperA.getFieldDescriptor("int32_map")));
    assertEquals(String.class,
        sqlConverter.resolveSqlValueType(helperA.getFieldDescriptor("int64_map")));
    assertEquals(String.class,
        sqlConverter.resolveSqlValueType(helperA.getFieldDescriptor("float_map")));
    assertEquals(String.class,
        sqlConverter.resolveSqlValueType(helperA.getFieldDescriptor("double_map")));
    assertEquals(String.class,
        sqlConverter.resolveSqlValueType(helperA.getFieldDescriptor("bool_map")));
    assertEquals(String.class,
        sqlConverter.resolveSqlValueType(helperA.getFieldDescriptor("string_map")));
    assertEquals(String.class,
        sqlConverter.resolveSqlValueType(helperA.getFieldDescriptor("enuma_map")));
    assertEquals(String.class,
        sqlConverter.resolveSqlValueType(helperA.getFieldDescriptor("bytes_map")));
    assertEquals(String.class,
        sqlConverter.resolveSqlValueType(helperA.getFieldDescriptor("msgb_map")));
  }

  @Test
  public void testTimestamp() {
    assertFalse(sqlConverter.isTimestampField(helperA.getFieldDescriptor("float")));
    assertFalse(sqlConverter.isTimestampField(helperA.getFieldDescriptor("int64")));

    Descriptors.FieldDescriptor timeFd = helperB.getFieldDescriptor("create_time");
    assertTrue(sqlConverter.isTimestampField(timeFd));
    assertEquals(Timestamp.class, sqlConverter.resolveSqlValueType(timeFd));

    long millis = System.currentTimeMillis();
    Timestamp timestamp = new Timestamp(millis);
    assertEquals(timestamp, sqlConverter.toSqlValue(TestModel.MsgB.class, "create_time", millis));
    assertEquals(timestamp,
        sqlConverter.toSqlValue(TestModel.MsgB.class, "create_time", timestamp));
    assertEquals(millis, sqlConverter.fromSqlValue(TestModel.MsgB.class, "create_time", timestamp));

    Date sqlDate = new Date(millis);
    assertEquals(sqlDate, sqlConverter.toSqlValue(TestModel.MsgB.class, "create_time", sqlDate));
    java.util.Date utilDate = new java.util.Date(millis);
    assertEquals(utilDate, sqlConverter.toSqlValue(TestModel.MsgB.class, "create_time", utilDate));

    try {
      sqlConverter.toSqlValue(helperB.getFieldDescriptor("create_time"), "");
      fail();
    } catch (TypeMismatchDataAccessException e) {
      System.out.println(e.getMessage());
    }
  }

  @Test
  public void testToSqlValueSimple() {
    assertEquals(1, sqlConverter.toSqlValue(helperA.getFieldDescriptor("int32"), 1));
    assertEquals(1L, sqlConverter.toSqlValue(helperA.getFieldDescriptor("int64"), 1));
    assertEquals(1.1f, sqlConverter.toSqlValue(helperA.getFieldDescriptor("float"), 1.1));
    assertEquals(1.1, sqlConverter.toSqlValue(helperA.getFieldDescriptor("double"), 1.1));
    assertEquals(1, sqlConverter.toSqlValue(helperA.getFieldDescriptor("bool"), true));
    assertEquals(0, sqlConverter.toSqlValue(helperA.getFieldDescriptor("bool"), false));
    assertEquals("1", sqlConverter.toSqlValue(helperA.getFieldDescriptor("string"), 1));
    assertEquals("str", sqlConverter.toSqlValue(helperA.getFieldDescriptor("string"), "str"));
    assertEquals(0,
        sqlConverter.toSqlValue(helperA.getFieldDescriptor("enuma"), TestModel.EnumA.EA0));
    assertEquals(2,
        sqlConverter.toSqlValue(helperA.getFieldDescriptor("enuma"), TestModel.EnumA.EA2));
    assertEquals(4,
        sqlConverter.toSqlValue(helperA.getFieldDescriptor("enuma"), TestModel.EnumA.EA4));

    // 兼容
    assertEquals(1L, sqlConverter.toSqlValue(helperA.getFieldDescriptor("int64"), 1));
    assertEquals(1f, sqlConverter.toSqlValue(helperA.getFieldDescriptor("float"), 1));
    assertEquals(1, sqlConverter.toSqlValue(helperA.getFieldDescriptor("bool"), 1));
    assertEquals(2, sqlConverter.toSqlValue(helperA.getFieldDescriptor("enuma"), 2));
  }

  @Test
  public void testToSqlValueTypeMismatch() {
    try {
      sqlConverter.toSqlValue(helperA.getFieldDescriptor("int32"), 1.1);
      fail();
    } catch (TypeMismatchDataAccessException e) {
      System.out.println(e.getMessage());
    }
    try {
      sqlConverter.toSqlValue(helperA.getFieldDescriptor("int64"), "");
      fail();
    } catch (TypeMismatchDataAccessException e) {
      System.out.println(e.getMessage());
    }
    try {
      sqlConverter.toSqlValue(helperA.getFieldDescriptor("float"), "");
      fail();
    } catch (TypeMismatchDataAccessException e) {
      System.out.println(e.getMessage());
    }
    try {
      sqlConverter.toSqlValue(helperA.getFieldDescriptor("double"), "");
      fail();
    } catch (TypeMismatchDataAccessException e) {
      System.out.println(e.getMessage());
    }
    try {
      sqlConverter.toSqlValue(helperA.getFieldDescriptor("bool"), "");
      fail();
    } catch (TypeMismatchDataAccessException e) {
      System.out.println(e.getMessage());
    }
    try {
      sqlConverter.toSqlValue(helperA.getFieldDescriptor("enuma"), "");
      fail();
    } catch (TypeMismatchDataAccessException e) {
      System.out.println(e.getMessage());
    }
  }

  @Test
  public void testToSqlValueRepeated() {
    assertEquals("",
        sqlConverter.toSqlValue(helperA.getFieldDescriptor("int32_arr"), Lists.newArrayList()));
    assertEquals("",
        sqlConverter.toSqlValue(helperA.getFieldDescriptor("int32_arr"), Sets.newHashSet()));
    assertEquals("1,2,",
        sqlConverter.toSqlValue(helperA.getFieldDescriptor("int32_arr"), Lists.newArrayList(1, 2)));
    assertEquals("1,2,", sqlConverter
        .toSqlValue(helperA.getFieldDescriptor("int64_arr"), Lists.newArrayList(1L, 2L)));
    assertEquals("1.1,2.2,", sqlConverter
        .toSqlValue(helperA.getFieldDescriptor("float_arr"), Lists.newArrayList(1.1f, 2.2f)));
    assertEquals("1.1,2.2,", sqlConverter
        .toSqlValue(helperA.getFieldDescriptor("double_arr"), Lists.newArrayList(1.1, 2.2)));
    assertEquals("1,0,", sqlConverter
        .toSqlValue(helperA.getFieldDescriptor("bool_arr"), Lists.newArrayList(true, false)));
    assertEquals(",a,", sqlConverter
        .toSqlValue(helperA.getFieldDescriptor("string_arr"), Lists.newArrayList("", "a")));
    assertEquals(",",
        sqlConverter.toSqlValue(helperA.getFieldDescriptor("string_arr"), Lists.newArrayList("")));
    assertEquals("%2c,",
        sqlConverter.toSqlValue(helperA.getFieldDescriptor("string_arr"), Lists.newArrayList(",")));
    assertEquals("%2c%25,", sqlConverter
        .toSqlValue(helperA.getFieldDescriptor("string_arr"), Lists.newArrayList(",%")));
    assertEquals("2,0,", sqlConverter.toSqlValue(helperA.getFieldDescriptor("enuma_arr"),
        Lists.newArrayList(TestModel.EnumA.EA2, TestModel.EnumA.EA0)));

    // 兼容
    assertEquals("1,2,",
        sqlConverter.toSqlValue(helperA.getFieldDescriptor("int64_arr"), Lists.newArrayList(1, 2)));
    assertEquals("1.0,2.0,", sqlConverter
        .toSqlValue(helperA.getFieldDescriptor("double_arr"), Lists.newArrayList(1, 2)));
    assertEquals("0,1,",
        sqlConverter.toSqlValue(helperA.getFieldDescriptor("bool_arr"), Lists.newArrayList(0, 1)));
    assertEquals("2,",
        sqlConverter.toSqlValue(helperA.getFieldDescriptor("enuma_arr"), Lists.newArrayList(2)));

    try {
      sqlConverter.toSqlValue(helperA.getFieldDescriptor("int32_arr"), 1);
      fail();
    } catch (TypeMismatchDataAccessException e) {
      System.out.println(e.getMessage());
    }
    try {
      sqlConverter.toSqlValue(helperA.getFieldDescriptor("bytes_arr"), Lists.newArrayList());
      fail();
    } catch (TypeMismatchDataAccessException e) {
      System.out.println(e.getMessage());
    }
    try {
      sqlConverter.toSqlValue(helperA.getFieldDescriptor("msgb_arr"), Lists.newArrayList());
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
    assertEquals("", sqlConverter.toSqlValue(helperA.getFieldDescriptor("int32_map"), map()));
    assertEquals("a:1;b:2;",
        sqlConverter.toSqlValue(helperA.getFieldDescriptor("int32_map"), map("a", 1, "b", 2)));
    assertEquals("1:1;",
        sqlConverter.toSqlValue(helperA.getFieldDescriptor("bool_map"), map(1, true)));
    assertEquals(":a;",
        sqlConverter.toSqlValue(helperA.getFieldDescriptor("string_map"), map("", "a")));
    assertEquals("%3a:%3b;",
        sqlConverter.toSqlValue(helperA.getFieldDescriptor("string_map"), map(":", ";")));
    assertEquals("%3a:%25;",
        sqlConverter.toSqlValue(helperA.getFieldDescriptor("string_map"), map(":", "%")));
    assertEquals("a:0;", sqlConverter
        .toSqlValue(helperA.getFieldDescriptor("enuma_map"), map("a", TestModel.EnumA.EA0)));

    // list
    TestModel.MsgA msga =
        TestModel.MsgA.newBuilder().putInt64Map("a", 1).putInt64Map("b", 2).build();
    assertEquals("a:1;b:2;", sqlConverter
        .toSqlValue(TestModel.MsgA.class, "int64_map", helperA.getFieldValue(msga, "int64_map")));

    // 兼容
    assertEquals(":2;",
        sqlConverter.toSqlValue(helperA.getFieldDescriptor("int64_map"), map("", 2)));
    assertEquals("a:2.0;",
        sqlConverter.toSqlValue(helperA.getFieldDescriptor("double_map"), map("a", 2)));
    assertEquals("0:1;",
        sqlConverter.toSqlValue(helperA.getFieldDescriptor("bool_map"), map(0, 1)));
    assertEquals("a:2;",
        sqlConverter.toSqlValue(helperA.getFieldDescriptor("enuma_map"), map("a", 2)));

    try {
      sqlConverter.toSqlValue(helperA.getFieldDescriptor("int32_map"), 1);
      fail();
    } catch (TypeMismatchDataAccessException e) {
      System.out.println(e.getMessage());
    }
    try {
      sqlConverter.toSqlValue(helperA.getFieldDescriptor("bytes_map"), map());
      fail();
    } catch (TypeMismatchDataAccessException e) {
      System.out.println(e.getMessage());
    }
    try {
      sqlConverter.toSqlValue(helperA.getFieldDescriptor("msgb_map"), map());
      fail();
    } catch (TypeMismatchDataAccessException e) {
      System.out.println(e.getMessage());
    }
  }

  @Test
  @SuppressWarnings("rawtype")
  public void testFromSqlValue() {
    assertEquals(1, sqlConverter.fromSqlValue(TestModel.MsgA.class, "int32", 1));
    assertEquals(1, sqlConverter.fromSqlValue(TestModel.MsgA.class, "int64", 1));
    assertEquals(1L, sqlConverter.fromSqlValue(TestModel.MsgA.class, "int64", 1L));
    assertEquals(1, sqlConverter.fromSqlValue(TestModel.MsgA.class, "float", 1));
    assertEquals(1f, sqlConverter.fromSqlValue(TestModel.MsgA.class, "float", 1f));
    assertEquals(1, sqlConverter.fromSqlValue(TestModel.MsgA.class, "double", 1));
    assertEquals(1.0, sqlConverter.fromSqlValue(TestModel.MsgA.class, "double", 1.0));
    assertEquals(true, sqlConverter.fromSqlValue(TestModel.MsgA.class, "bool", 1));
    assertEquals(false, sqlConverter.fromSqlValue(TestModel.MsgA.class, "bool", 0));
    assertEquals(TestModel.EnumA.EA0.getValueDescriptor(),
        sqlConverter.fromSqlValue(TestModel.MsgA.class, "enuma", 0));

    try {
      sqlConverter.fromSqlValue(TestModel.MsgA.class, "bool", 1.0);
      fail();
    } catch (TypeMismatchDataAccessException e) {
      System.out.println(e.getMessage());
    }
    try {
      sqlConverter.fromSqlValue(TestModel.MsgA.class, "bytes", null);
      fail();
    } catch (TypeMismatchDataAccessException e) {
      System.out.println(e.getMessage());
    }
    try {
      sqlConverter.fromSqlValue(TestModel.MsgA.class, "msgb", null);
      fail();
    } catch (TypeMismatchDataAccessException e) {
      System.out.println(e.getMessage());
    }
  }

  @Test
  @SuppressWarnings("rawtype")
  public void testFromSqlValueRepeated() {
    List enumas =
        (List) sqlConverter.fromSqlValue(helperA, helperA.getFieldDescriptor("enuma_arr"), "0,2");
    assertEquals(2, enumas.size());
    assertTrue(enumas.get(0) instanceof Descriptors.EnumValueDescriptor);
    assertEquals(TestModel.EnumA.EA0.name(),
        ((Descriptors.EnumValueDescriptor) enumas.get(0)).getName());

    assertEquals(Lists.newArrayList(1),
        sqlConverter.fromSqlValue(helperA, helperA.getFieldDescriptor("int32_arr"), "1,"));
    assertEquals(Lists.newArrayList(1L),
        sqlConverter.fromSqlValue(helperA, helperA.getFieldDescriptor("int64_arr"), "1"));
    assertEquals(Lists.newArrayList(1f),
        sqlConverter.fromSqlValue(helperA, helperA.getFieldDescriptor("float_arr"), "1"));
    assertEquals(Lists.newArrayList(1.0),
        sqlConverter.fromSqlValue(helperA, helperA.getFieldDescriptor("double_arr"), "1"));
    assertEquals(Lists.newArrayList(true),
        sqlConverter.fromSqlValue(helperA, helperA.getFieldDescriptor("bool_arr"), "1"));
    assertEquals(Lists.newArrayList(false),
        sqlConverter.fromSqlValue(helperA, helperA.getFieldDescriptor("bool_arr"), "0"));

    assertEquals(Lists.newArrayList(),
        sqlConverter.fromSqlValue(helperA, helperA.getFieldDescriptor("string_arr"), null));
    assertEquals(Lists.newArrayList(),
        sqlConverter.fromSqlValue(helperA, helperA.getFieldDescriptor("string_arr"), ""));
    assertEquals(Lists.newArrayList(""),
        sqlConverter.fromSqlValue(helperA, helperA.getFieldDescriptor("string_arr"), ","));
    assertEquals(Lists.newArrayList("a"),
        sqlConverter.fromSqlValue(helperA, helperA.getFieldDescriptor("string_arr"), "a"));
    assertEquals(Lists.newArrayList(","),
        sqlConverter.fromSqlValue(helperA, helperA.getFieldDescriptor("string_arr"), "%2c,"));
    assertEquals(Lists.newArrayList(","),
        sqlConverter.fromSqlValue(helperA, helperA.getFieldDescriptor("string_arr"), "%2c"));
    assertEquals(Lists.newArrayList("%"),
        sqlConverter.fromSqlValue(helperA, helperA.getFieldDescriptor("string_arr"), "%25,"));

    try {
      sqlConverter.fromSqlValue(TestModel.MsgA.class, "int32_arr", 1);
      fail();
    } catch (TypeMismatchDataAccessException e) {
      System.out.println(e.getMessage());
    }
    try {
      sqlConverter.fromSqlValue(TestModel.MsgA.class, "bytes_arr", "a");
      fail();
    } catch (TypeMismatchDataAccessException e) {
      System.out.println(e.getMessage());
    }
    try {
      sqlConverter.fromSqlValue(TestModel.MsgA.class, "msgb_arr", "a");
      fail();
    } catch (TypeMismatchDataAccessException e) {
      System.out.println(e.getMessage());
    }
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testFromSqlValueMap() {
    assertEquals(Collections.emptyList(),
        sqlConverter.fromSqlValue(TestModel.MsgA.class, "int32_map", null));

    List<MapEntry> mapEntries;

    mapEntries = (List<MapEntry>) sqlConverter
        .fromSqlValue(helperA, helperA.getFieldDescriptor("int64_map"), "");
    assertEquals(0, mapEntries.size());

    mapEntries = (List<MapEntry>) sqlConverter
        .fromSqlValue(helperA, helperA.getFieldDescriptor("int64_map"), "a:1");
    assertEquals(1, mapEntries.size());
    assertEquals("a", mapEntries.get(0).getKey());
    assertEquals(1L, mapEntries.get(0).getValue());

    mapEntries = (List<MapEntry>) sqlConverter
        .fromSqlValue(helperA, helperA.getFieldDescriptor("int64_map"), "a:1;");
    assertEquals(1, mapEntries.size());
    assertEquals("a", mapEntries.get(0).getKey());
    assertEquals(1L, mapEntries.get(0).getValue());

    try {
      sqlConverter.fromSqlValue(TestModel.MsgA.class, "int32_map", 1);
      fail();
    } catch (TypeMismatchDataAccessException e) {
      System.out.println(e.getMessage());
    }
    try {
      sqlConverter.fromSqlValue(TestModel.MsgA.class, "bytes_map", "a:1");
      fail();
    } catch (TypeMismatchDataAccessException e) {
      System.out.println(e.getMessage());
    }
    try {
      sqlConverter.fromSqlValue(TestModel.MsgA.class, "msgb_map", "a:1");
      fail();
    } catch (TypeMismatchDataAccessException e) {
      System.out.println(e.getMessage());
    }
  }

}
