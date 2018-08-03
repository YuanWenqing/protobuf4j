package org.protoframework.orm.spring;

import org.protoframework.core.proto.data.TestModel;
import org.protoframework.orm.dao.IMessageDao;

/**
 * @author: yuanwq
 * @date: 2018/8/3
 */
public interface BDao extends IMessageDao<TestModel.DbMsg> {
}
