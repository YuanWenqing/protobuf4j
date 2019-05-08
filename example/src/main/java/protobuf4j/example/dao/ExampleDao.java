package protobuf4j.example.dao;

import protobuf4j.example.proto.Example;
import protobuf4j.orm.dao.IPrimaryKeyMessageDao;

/**
 * Author: yuanwq
 * Date: 2019/5/8
 */
public interface ExampleDao extends IPrimaryKeyMessageDao<Long, Example> {
}
