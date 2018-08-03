package org.protoframework.orm.spring;

import org.protoframework.core.proto.data.TestModel;
import org.protoframework.orm.dao.IPrimaryKeyMessageDao;

/**
 * @author: yuanwq
 * @date: 2018/8/3
 */
public interface ADao extends IPrimaryKeyMessageDao<Long, TestModel.DbMsg> {
}
