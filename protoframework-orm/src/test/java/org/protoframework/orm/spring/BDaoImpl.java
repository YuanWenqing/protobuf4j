package org.protoframework.orm.spring;

import org.protoframework.core.proto.data.TestModel;
import org.protoframework.orm.dao.ProtoMessageDao;
import org.springframework.stereotype.Repository;

/**
 * @author: yuanwq
 * @date: 2018/8/3
 */
@Repository
@DataSourceRouting("b")
public class BDaoImpl extends ProtoMessageDao<TestModel.DbMsg> implements BDao {
  public BDaoImpl() {
    super(TestModel.DbMsg.class);
    daoLogger.info(getClass().getName());
  }
}
