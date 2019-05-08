# Protobuf4j

## Introduction

A Facility framework to develop with Google Protobuf, including:

* Utils to handle Protobuf messages and enums
* ORM-based DAO for storing Protobuf Message in MySQL
* Some supports with Spring and SpringBoot

## Modules

* `protobuf4j-core`: helper class for Protobuf Message and Enum, codec class for Message.
* `protobuf4j-sql`: abstraction for SQL fragment and statement.
* `protobuf4j-orm`: basic ORM-like DAO framework utilizing `protobuf4j-sql`
* `protobuf4j-orm-starter`: springboot starter for `protobuf4j-orm`, including autoconfiguration and jdbc injection
* `protobuf4j-spring`: some extensions for Protobuf to integrate with spring

## ORM Usage

### Gradle

~~~groovy
implementation("xyz.codemeans.protobuf4j:protobuf4j-orm-starter:$protobuf4jVersion")
~~~

The latest version is `protobuf4jVersion=0.9.3.alpha`

### Properties

~~~properties
protobuf4j.datasource.auto-enable=<if-auto-configuration-enalbed:default true>
~~~

### Java

interface

~~~java
public interface ExampleDao extends IPrimaryKeyMessageDao<Long, Example> {
}
~~~

impl

~~~java
@Repository
public class ExampleDaoImpl extends PrimaryKeyProtoMessageDao<Long, Example> implements ExampleDao {
  public ExampleDaoImpl() {
    super(Example.class, ExampleNaming.ID);
  }
}
~~~

### CRUD

~~~java
@Autowired
ExampleDao exampleDao;

public Example createExample(Example example) {
  long id = exampleDao.insertReturnKey(example).longValue();
  return exampleDao.selectOneByPrimaryKey(id);
}

public Example getExample(long id) {
  return exampleDao.selectOneByPrimaryKey(id);
  // or
  //return exampleDao.selectOneByCond(FieldAndValue.eq(ExampleNaming.ID, id));
}

public Example updateExample(Example newValue, Example oldValue) {
  exampleDao.updateMessageByPrimaryKey(newValue, oldValue);
  return exampleDao.selectOneByPrimaryKey(newValue.getId());
}

public void deleteExample(long id) {
  exampleDao.deleteByPrimaryKey(id);
}

// complex conditions and pagination
public List<Example> listExamples(WhereClause where) {
  return exampleDao.selectByWhere(where);
}

public Map<Long, Example> getExamples(Collection<Long> ids) {
  return exampleDao.selectMultiByPrimaryKey(ids);
}
~~~

## SQL Usage

~~~java
# condition


# pagination


# select specified columns


# update whole message with different fields


# update specified columns


# complex insert/select/update/delete


~~~



## Reference

* `naming`: a Protobuf codegen plugin to generate *Naming class containing field naming constatns, see <https://github.com/YuanWenqing/protoplugin>

