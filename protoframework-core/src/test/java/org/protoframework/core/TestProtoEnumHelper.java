package org.protoframework.core;

import org.junit.Before;
import org.junit.Test;
import org.protoframework.core.proto.data.TestModel;

import static org.junit.Assert.*;

/**
 * author: yuanwq
 * date: 2018/7/9
 */
public class TestProtoEnumHelper {
  private ProtoEnumHelper<TestModel.EnumA> helper;

  @Before
  public void setup() {
    helper = ProtoEnumHelper.getHelper(TestModel.EnumA.class);
  }

  @Test
  public void testHelper() {
    System.out.println(helper.getFieldNames());
    assertSame(helper, ProtoEnumHelper.getHelper(TestModel.EnumA.EA0));
    assertEquals(TestModel.EnumA.class, helper.getType());
    assertEquals(TestModel.EnumA.EA0, helper.defaultValue());

    assertTrue(helper.isEmpty(null));
    assertTrue(helper.isEmpty(TestModel.EnumA.EA0));
    assertFalse(helper.isEmpty(TestModel.EnumA.EA1));
    assertFalse(helper.isEmpty(TestModel.EnumA.UNRECOGNIZED));

    assertEquals(TestModel.EnumA.getDescriptor(), helper.getDescriptor());
    assertEquals(TestModel.EnumA.UNRECOGNIZED, helper.getUnrecognizedValue());

    assertEquals("null", helper.toString(null));
    assertEquals("EA0[0]", helper.toString(TestModel.EnumA.EA0));
    assertEquals("EA1[1]", helper.toString(TestModel.EnumA.EA1));
    assertEquals("UNRECOGNIZED[-1]", helper.toString(TestModel.EnumA.UNRECOGNIZED));
  }

  @Test
  public void testFindValue() {
    assertTrue(helper.hasField("EA0"));
    assertFalse(helper.hasField("EA10"));

    for (TestModel.EnumA enumA : TestModel.EnumA.values()) {
      if (enumA == TestModel.EnumA.UNRECOGNIZED) {
        continue;
      }
      assertEquals(enumA, helper.byNumber(enumA.getNumber()));
      assertEquals(enumA, helper.byName(enumA.name()));
    }
    assertEquals(null, helper.byName(""));
    assertEquals(null, helper.byName(null));
    assertEquals(null, helper.byNumber(-1));
    assertEquals(null, helper.byNumber(10000));
  }

  @Test
  public void testUnsupport() {
    assertUnsupport(() -> helper.getFieldType(""));
    assertUnsupport(() -> helper.getFieldTypeMap());
    assertUnsupport(() -> helper.isFieldSet(null, ""));
    assertUnsupport(() -> helper.getFieldValue(null, ""));
    assertUnsupport(() -> helper.setFieldValue(null, "", null));
  }

  private void assertUnsupport(Runnable func) {
    try {
      func.run();
      fail();
    } catch (UnsupportedOperationException e) {
      System.out.println(e.getMessage());
    }
  }
}
