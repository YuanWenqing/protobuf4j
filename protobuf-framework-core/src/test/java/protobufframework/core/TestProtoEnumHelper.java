package protobufframework.core;

import org.junit.Before;
import org.junit.Test;
import protobufframework.test.proto.TestModel;

import static org.junit.Assert.*;

/**
 * @author: yuanwq
 * @date: 2018/7/9
 */
public class TestProtoEnumHelper {
  private ProtoEnumHelper<TestModel.EnumA> helper;

  @Before
  public void setup() {
    helper = ProtoEnumHelper.getHelper(TestModel.EnumA.class);
  }

  @Test
  public void testHelper() {
    helper = ProtoEnumHelper.getHelper(TestModel.EnumA.class);
    System.out.println(helper.getEnumValues());
    System.out.println(helper.getEnumValueNames());
    System.out.println(helper.getEnumValueNumbers());
    assertSame(helper, ProtoEnumHelper.getHelper(TestModel.EnumA.EA0));
    assertEquals(TestModel.EnumA.class, helper.getType());
    assertEquals(TestModel.EnumA.EA0, helper.defaultValue());

    assertEquals(TestModel.EnumA.getDescriptor(), helper.getDescriptor());
    assertEquals(TestModel.EnumA.UNRECOGNIZED, helper.getUnrecognizedValue());

    assertEquals("test.EnumA{null}", helper.toString(null));
    assertEquals("test.EnumA{EA0[0]}", helper.toString(TestModel.EnumA.EA0));
    assertEquals("test.EnumA{EA2[2]}", helper.toString(TestModel.EnumA.EA2));
    assertEquals("test.EnumA{UNRECOGNIZED[-1]}", helper.toString(TestModel.EnumA.UNRECOGNIZED));
  }

  @Test
  public void testFindValue() {
    assertNotNull(helper.of("EA0"));
    assertNull(helper.of("EA10"));

    for (TestModel.EnumA enumA : TestModel.EnumA.values()) {
      if (enumA == TestModel.EnumA.UNRECOGNIZED) {
        continue;
      }
      assertEquals(enumA, helper.forNumber(enumA.getNumber()));
      assertEquals(enumA, helper.of(enumA.name()));
    }
    assertEquals(null, helper.of(""));
    assertEquals(null, helper.of(null));
    assertEquals(null, helper.forNumber(-1));
    assertEquals(null, helper.forNumber(10000));
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
