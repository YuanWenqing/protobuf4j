package org.protoframework.dao;

import com.google.protobuf.Descriptors;
import org.junit.Test;
import org.protoframework.core.ProtoMessageHelper;
import org.protoframework.core.proto.data.TestModel;
import org.springframework.dao.TypeMismatchDataAccessException;

import java.sql.Timestamp;

import static org.junit.Assert.*;

/**
 * @author: yuanwq
 * @date: 2018/7/23
 */
public class TestProtoSqlConverter {
  private IProtoSqlConverter sqlConverter = ProtoSqlConverter.getInstance();
  private ProtoMessageHelper<TestModel.MsgA> helperA =
      ProtoMessageHelper.getHelper(TestModel.MsgA.class);
  private ProtoMessageHelper<TestModel.MsgB> helperB =
      ProtoMessageHelper.getHelper(TestModel.MsgB.class);

  @Test
  public void test() {
    assertEquals("msg_a", sqlConverter.tableName(TestModel.MsgA.class));
    assertFalse(sqlConverter.isTimestampField(helperA.getFieldDescriptor("int64")));
  }

  @Test
  public void testResolveSqlValueType() {
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
  }

  @Test
  public void testTimestamp() {
    Descriptors.FieldDescriptor timeFd = helperB.getFieldDescriptor("create_time");
    assertTrue(sqlConverter.isTimestampField(timeFd));
    assertEquals(Timestamp.class, sqlConverter.resolveSqlValueType(timeFd));
  }

}
