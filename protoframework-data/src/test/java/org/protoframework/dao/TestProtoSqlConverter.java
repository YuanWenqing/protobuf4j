package org.protoframework.dao;

import com.google.common.collect.Lists;
import com.google.protobuf.Descriptors;
import org.junit.Test;
import org.protoframework.core.ProtoMessageHelper;
import org.protoframework.core.proto.data.TestModel;
import org.springframework.dao.TypeMismatchDataAccessException;

import java.sql.Date;
import java.sql.Timestamp;

import static org.junit.Assert.*;

/**
 * @author: yuanwq
 * @date: 2018/7/23
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
      sqlConverter.toSqlValue(helperA.getFieldDescriptor("int32"), "");
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
  }

}
