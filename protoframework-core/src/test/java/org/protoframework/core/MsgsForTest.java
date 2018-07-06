package org.protoframework.core;

import com.google.protobuf.ByteString;
import org.protoframework.core.proto.data.TestModel;

/**
 * author: yuanwq
 * date: 2018/7/6
 */
public class MsgsForTest {

  public static TestModel.MsgA allSetMsgA =
      TestModel.MsgA.newBuilder().setInt32(1).setInt64(1).setFloat(1).setDouble(1).setBool(true)
          .setString("1").setBytes(ByteString.copyFromUtf8("a")).setEnuma(TestModel.EnumA.EA1)
          .setMsgb(TestModel.MsgB.newBuilder().setId("").build()).addInt32Arr(1).addInt64Arr(1)
          .addFloatArr(1).addDoubleArr(1).addBoolArr(false).addStringArr("a")
          .addBytesArr(ByteString.EMPTY).addEnumaArr(TestModel.EnumA.EA0)
          .addMsgbArr(TestModel.MsgB.getDefaultInstance()).putInt32Map("", 1).putInt64Map("", 1)
          .putFloatMap("", 1).putDoubleMap("", 1).putBoolMap(1, false).putStringMap("", "a")
          .putBytesMap("", ByteString.EMPTY).putEnumaMap("", TestModel.EnumA.EA0)
          .putMsgbMap("", TestModel.MsgB.getDefaultInstance()).build();
}
