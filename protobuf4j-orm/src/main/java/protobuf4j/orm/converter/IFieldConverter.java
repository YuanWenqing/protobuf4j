package protobuf4j.orm.converter;

/**
 * handle type conversion between value of field and sql-style value
 */
public interface IFieldConverter {

  /**
   * type of converted sql-style value
   *
   * @return
   */
  Class<?> getSqlValueType();

  /**
   * convert {@code fieldValue} to sql-style value
   *
   * @param fieldValue to convert value
   * @return
   */
  Object toSqlValue(Object fieldValue);

  /**
   * parse from sql-style value
   *
   * @param sqlValue sql-style value to parse from
   * @return
   */
  Object fromSqlValue(Object sqlValue);
}
