package protobuf4j.orm.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * author: yuanwq
 * date: 2018/7/14
 */
public class DaoSqlLogger {
  private final String name;
  private final Logger raw;
  private final Logger insert;
  private final Logger delete;
  private final Logger select;
  private final Logger update;

  public DaoSqlLogger(String name) {
    this.name = name;
    String prefix = "dao.sql." + name + ".";
    this.raw = LoggerFactory.getLogger(prefix + "raw");
    this.insert = LoggerFactory.getLogger(prefix + "insert");
    this.delete = LoggerFactory.getLogger(prefix + "delete");
    this.select = LoggerFactory.getLogger(prefix + "select");
    this.update = LoggerFactory.getLogger(prefix + "update");
  }

  public String getName() {
    return name;
  }

  public Logger insert() {
    return insert;
  }

  public Logger delete() {
    return delete;
  }

  public Logger select() {
    return select;
  }

  public Logger update() {
    return update;
  }

  public Logger raw() {
    return raw;
  }

}
