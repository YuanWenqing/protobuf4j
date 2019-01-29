package protobuf4j.core;

import com.google.common.collect.Lists;
import com.google.protobuf.ByteString;
import com.google.protobuf.MapEntry;
import org.junit.Before;
import org.junit.Test;
import protobuf4j.test.MsgsForTest;
import protobuf4j.test.proto.TestModel;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * author: yuanwq
 * date: 2018/7/4
 */
public class TestProtoMessageHelper {
  private ProtoMessageHelper<TestModel.MsgA> helper;
  private TestModel.MsgA codecsgA;

  @Before
  public void setup() {
    helper = ProtoMessageHelper.getHelper(TestModel.MsgA.class);
  }

  @Test
  public void testMessage() {
    System.out.println("fields: " + helper.getFieldNames());

    assertSame(helper,
        ProtoMessageHelper.<TestModel.MsgA>getHelper(TestModel.MsgA.getDefaultInstance()));

    assertEquals(TestModel.MsgA.class, helper.getType());
    assertEquals(TestModel.MsgA.getDefaultInstance(), helper.defaultValue());
    assertEquals(TestModel.MsgA.Builder.class, helper.newBuilder().getClass());

    assertTrue(helper.isEmpty(null));
    assertTrue(helper.isEmpty(TestModel.MsgA.getDefaultInstance()));
    assertTrue(helper.isEmpty(TestModel.MsgA.newBuilder().build()));
    assertFalse(helper.isEmpty(TestModel.MsgA.newBuilder().setInt32(1).build()));

    TestModel.MsgA msgaWithMsgb =
        TestModel.MsgA.newBuilder().setMsgb(TestModel.MsgB.getDefaultInstance()).build();
    assertFalse(helper.isEmpty(msgaWithMsgb));
    assertTrue(helper.isFieldSet(msgaWithMsgb, "msgb"));

    try {
      helper.newBuilderForField("aaaaaaa");
      fail();
    } catch (RuntimeException e) {
      System.out.println(e.getMessage());
    }

  }

  @Test
  public void testFieldSize() {
    assertEquals(helper.getFieldDescriptorList().size(), helper.getFieldDescriptorMap().size());
    assertEquals(helper.getFieldDescriptorList().size(), helper.getFieldNames().size());
    assertEquals(helper.getFieldDescriptorList().size(), helper.getFieldTypeMap().size());
  }

  @Test
  public void testFieldType() {
    assertTrue(helper.hasField("int32"));
    assertEquals(Integer.class, helper.getFieldType("int32"));
    assertTrue(helper.hasField("int64"));
    assertEquals(Long.class, helper.getFieldType("int64"));
    assertTrue(helper.hasField("float"));
    assertEquals(Float.class, helper.getFieldType("float"));
    assertTrue(helper.hasField("double"));
    assertEquals(Double.class, helper.getFieldType("double"));
    assertTrue(helper.hasField("bool"));
    assertEquals(Boolean.class, helper.getFieldType("bool"));
    assertTrue(helper.hasField("string"));
    assertEquals(String.class, helper.getFieldType("string"));
    assertTrue(helper.hasField("bytes"));
    assertEquals(ByteString.class, helper.getFieldType("bytes"));
    assertTrue(helper.hasField("enuma"));
    assertEquals(TestModel.EnumA.class, helper.getFieldType("enuma"));
    assertTrue(helper.hasField("msgb"));
    assertEquals(TestModel.MsgB.class, helper.getFieldType("msgb"));

    assertTrue(helper.hasField("int32_arr"));
    assertEquals(List.class, helper.getFieldType("int32_arr"));
    assertTrue(helper.hasField("int64_arr"));
    assertEquals(List.class, helper.getFieldType("int64_arr"));
    assertTrue(helper.hasField("float_arr"));
    assertEquals(List.class, helper.getFieldType("float_arr"));
    assertTrue(helper.hasField("double_arr"));
    assertEquals(List.class, helper.getFieldType("double_arr"));
    assertTrue(helper.hasField("bool_arr"));
    assertEquals(List.class, helper.getFieldType("bool_arr"));
    assertTrue(helper.hasField("string_arr"));
    assertEquals(List.class, helper.getFieldType("string_arr"));
    assertTrue(helper.hasField("bytes_arr"));
    assertEquals(List.class, helper.getFieldType("bytes_arr"));
    assertTrue(helper.hasField("enuma_arr"));
    assertEquals(List.class, helper.getFieldType("enuma_arr"));
    assertTrue(helper.hasField("msgb_arr"));
    assertEquals(List.class, helper.getFieldType("msgb_arr"));

    assertTrue(helper.hasField("int32_map"));
    assertEquals(Map.class, helper.getFieldType("int32_map"));
    assertTrue(helper.hasField("int64_map"));
    assertEquals(Map.class, helper.getFieldType("int64_map"));
    assertTrue(helper.hasField("float_map"));
    assertEquals(Map.class, helper.getFieldType("float_map"));
    assertTrue(helper.hasField("double_map"));
    assertEquals(Map.class, helper.getFieldType("double_map"));
    assertTrue(helper.hasField("bool_map"));
    assertEquals(Map.class, helper.getFieldType("bool_map"));
    assertTrue(helper.hasField("string_map"));
    assertEquals(Map.class, helper.getFieldType("string_map"));
    assertTrue(helper.hasField("bytes_map"));
    assertEquals(Map.class, helper.getFieldType("bytes_map"));
    assertTrue(helper.hasField("enuma_map"));
    assertEquals(Map.class, helper.getFieldType("enuma_map"));
    assertTrue(helper.hasField("msgb_map"));
    assertEquals(Map.class, helper.getFieldType("msgb_map"));
  }

  @Test
  public void testRepeatedFieldValueType() {
    assertEquals(Integer.class,
        helper.resolveRepeatedFieldValueType(helper.getFieldDescriptor("int32_arr")));
    assertEquals(Long.class,
        helper.resolveRepeatedFieldValueType(helper.getFieldDescriptor("int64_arr")));
    assertEquals(Float.class,
        helper.resolveRepeatedFieldValueType(helper.getFieldDescriptor("float_arr")));
    assertEquals(Double.class,
        helper.resolveRepeatedFieldValueType(helper.getFieldDescriptor("double_arr")));
    assertEquals(Boolean.class,
        helper.resolveRepeatedFieldValueType(helper.getFieldDescriptor("bool_arr")));
    assertEquals(String.class,
        helper.resolveRepeatedFieldValueType(helper.getFieldDescriptor("string_arr")));
    assertEquals(ByteString.class,
        helper.resolveRepeatedFieldValueType(helper.getFieldDescriptor("bytes_arr")));
    assertEquals(TestModel.EnumA.class,
        helper.resolveRepeatedFieldValueType(helper.getFieldDescriptor("enuma_arr")));
    assertEquals(TestModel.MsgB.class,
        helper.resolveRepeatedFieldValueType(helper.getFieldDescriptor("msgb_arr")));

    assertEquals(MapEntry.class,
        helper.resolveRepeatedFieldValueType(helper.getFieldDescriptor("int32_map")));
    assertEquals(MapEntry.class,
        helper.resolveRepeatedFieldValueType(helper.getFieldDescriptor("int64_map")));
    assertEquals(MapEntry.class,
        helper.resolveRepeatedFieldValueType(helper.getFieldDescriptor("float_map")));
    assertEquals(MapEntry.class,
        helper.resolveRepeatedFieldValueType(helper.getFieldDescriptor("double_map")));
    assertEquals(MapEntry.class,
        helper.resolveRepeatedFieldValueType(helper.getFieldDescriptor("bool_map")));
    assertEquals(MapEntry.class,
        helper.resolveRepeatedFieldValueType(helper.getFieldDescriptor("string_map")));
    assertEquals(MapEntry.class,
        helper.resolveRepeatedFieldValueType(helper.getFieldDescriptor("bytes_map")));
    assertEquals(MapEntry.class,
        helper.resolveRepeatedFieldValueType(helper.getFieldDescriptor("enuma_map")));
    assertEquals(MapEntry.class,
        helper.resolveRepeatedFieldValueType(helper.getFieldDescriptor("msgb_map")));
  }

  @Test
  public void testMapFieldKeyValueType() {
    assertEquals(String.class,
        helper.resolveMapFieldKeyType(helper.getFieldDescriptor("int32_map")));
    assertEquals(Integer.class,
        helper.resolveMapFieldKeyType(helper.getFieldDescriptor("bool_map")));

    assertEquals(Integer.class,
        helper.resolveMapFieldValueType(helper.getFieldDescriptor("int32_map")));
    assertEquals(Long.class,
        helper.resolveMapFieldValueType(helper.getFieldDescriptor("int64_map")));
    assertEquals(Float.class,
        helper.resolveMapFieldValueType(helper.getFieldDescriptor("float_map")));
    assertEquals(Double.class,
        helper.resolveMapFieldValueType(helper.getFieldDescriptor("double_map")));
    assertEquals(Boolean.class,
        helper.resolveMapFieldValueType(helper.getFieldDescriptor("bool_map")));
    assertEquals(String.class,
        helper.resolveMapFieldValueType(helper.getFieldDescriptor("string_map")));
    assertEquals(ByteString.class,
        helper.resolveMapFieldValueType(helper.getFieldDescriptor("bytes_map")));
    assertEquals(TestModel.EnumA.class,
        helper.resolveMapFieldValueType(helper.getFieldDescriptor("enuma_map")));
    assertEquals(TestModel.MsgB.class,
        helper.resolveMapFieldValueType(helper.getFieldDescriptor("msgb_map")));
  }

  @Test
  public void testFieldSet() {
    assertMsg(TestModel.MsgA.getDefaultInstance(), false);

    assertTrue(helper.isFieldSet(MsgsForTest.allSetMsgA, "bool"));
    assertMsg(MsgsForTest.allSetMsgA, true);
  }

  private void assertMsg(TestModel.MsgA msga, boolean expect) {
    for (String field : helper.getFieldNames()) {
      try {
        assertEquals(expect, helper.isFieldSet(msga, field));
      } catch (AssertionError e) {
        throw new AssertionError("assert `" + field + "`, " + e.getMessage());
      }
    }
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testGetFieldValue() {
    TestModel.MsgA msga = TestModel.MsgA.getDefaultInstance();
    assertEquals(0, helper.getFieldValue(msga, "int32"));
    assertEquals(Lists.newArrayList(), helper.getFieldValue(msga, "int32_arr"));
    assertEquals(Lists.newArrayList(), helper.getFieldValue(msga, "int32_map"));

    msga = MsgsForTest.allSetMsgA;
    assertEquals(1, helper.getFieldValue(msga, "int32"));
    assertEquals(Lists.newArrayList(1), helper.getFieldValue(msga, "int32_arr"));
    assertTrue(helper.getFieldValue(msga, "int32_map") instanceof List);
    assertEquals(Lists.newArrayList(newEntry("int32_map", "", 1)),
        helper.getFieldValue(msga, "int32_map"));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testSetFieldValue() {
    TestModel.MsgA msgA = TestModel.MsgA.getDefaultInstance();
    msgA = helper.setFieldValue(msgA, "int32", 2);
    msgA = helper.setFieldValue(msgA, "int32_arr", Lists.newArrayList(1, 2));
    msgA = helper.setFieldValue(msgA, "int64_map",
        Lists.newArrayList(newEntry("int64_map", "a", 1L), newEntry("int64_map", "b", 2L)));
    assertEquals(2, msgA.getInt32());
    assertEquals(2, msgA.getInt32ArrCount());
    assertEquals(1, msgA.getInt32Arr(0));
    assertEquals(2, msgA.getInt32Arr(1));
    assertEquals(2, msgA.getInt64MapCount());
    assertEquals(1L, msgA.getInt64MapOrThrow("a"));
    assertEquals(2L, msgA.getInt64MapOrThrow("b"));
  }

  @SuppressWarnings("unchecked")
  private <V> MapEntry<String, V> newEntry(String field, String key, V value) {
    return ((MapEntry.Builder<String, V>) helper.newBuilderForField(field)).setKey(key)
        .setValue(value).build();
  }

  @Test
  public void testToString() {
    System.out.println(ProtoMessageHelper.printToString(null));
    System.out.println(ProtoMessageHelper.printToString(MsgsForTest.allSetMsgA));
    System.out.println("null: " + helper.toString(null));
    System.out.println("empty: " + helper.toString(TestModel.MsgA.getDefaultInstance()));
    System.out.println("MsgsForTest.allSetMsgA: " + helper.toString(MsgsForTest.allSetMsgA));
  }

}
