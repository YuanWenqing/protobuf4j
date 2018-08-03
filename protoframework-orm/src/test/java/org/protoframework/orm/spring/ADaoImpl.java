package org.protoframework.orm.spring;

import org.protoframework.core.proto.data.TestModel;
import org.protoframework.orm.dao.PriKeyProtoMessageDao;
import org.springframework.stereotype.Repository;

/**
 * @author: yuanwq
 * @date: 2018/8/3
 */
@Repository
@DataSourceRouting("a")
public class ADaoImpl extends PriKeyProtoMessageDao<Long, TestModel.DbMsg> implements ADao {
  public ADaoImpl() {
    super(TestModel.DbMsg.class, "id");
    daoLogger.info(getClass().getName());
  }
}
