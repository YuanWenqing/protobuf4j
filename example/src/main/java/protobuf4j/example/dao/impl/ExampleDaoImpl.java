package protobuf4j.example.dao.impl;

import org.springframework.stereotype.Repository;
import protobuf4j.example.dao.ExampleDao;
import protobuf4j.example.proto.Example;
import protobuf4j.example.proto.naming.ExampleNaming;
import protobuf4j.orm.dao.PrimaryKeyProtoMessageDao;

/**
 * Author: yuanwq
 * Date: 2019/5/8
 */
@Repository
public class ExampleDaoImpl extends PrimaryKeyProtoMessageDao<Long, Example> implements ExampleDao {
  public ExampleDaoImpl() {
    super(Example.class, ExampleNaming.ID);
  }
}
