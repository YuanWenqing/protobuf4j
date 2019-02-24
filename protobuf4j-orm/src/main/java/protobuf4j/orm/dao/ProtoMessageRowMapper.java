/*
 * Copyright 2002-2014 the original author or authors. Licensed under the Apache License, Version
 * 2.0 (the "License"); you may not use this file except in compliance with the License. You may
 * obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by
 * applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */

package protobuf4j.orm.dao;

import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.Message;
import lombok.Getter;
import lombok.Setter;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;
import protobuf4j.core.ProtoMessageHelper;
import protobuf4j.orm.converter.FieldResolver;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * 将数据库一行数据映射为Protobuf Message
 * <p>
 * author yuanwq
 */
@Setter
@Getter
public class ProtoMessageRowMapper<T extends Message> implements RowMapper<T> {
  private final ProtoMessageHelper<T> messageHelper;
  private final FieldResolver<T> fieldResolver;

  public ProtoMessageRowMapper(ProtoMessageHelper<T> messageHelper,
      FieldResolver<T> fieldResolver) {
    this.messageHelper = messageHelper;
    this.fieldResolver = fieldResolver;
  }

  /**
   * map the values for all columns in the current row to a Message
   *
   * @see ResultSetMetaData
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  @Override
  public T mapRow(ResultSet rs, int rowNumber) throws SQLException {
    Message.Builder builder = this.messageHelper.newBuilder();
    ResultSetMetaData rsmd = rs.getMetaData();
    int columnCount = rsmd.getColumnCount();

    for (int index = 1; index <= columnCount; index++) {
      String column = JdbcUtils.lookupColumnName(rsmd, index);
      column = column.toLowerCase(); // TODO: always got upper case, but why?
      FieldDescriptor fd = this.messageHelper.getFieldDescriptor(column);
      if (fd != null) {
        Object value = null;
        try {
          value = getColumnValue(rs, index, fd);
          if (value == null) continue;
          value = fieldResolver.fromSqlValue(fd, value);
          builder.setField(fd, value);
        } catch (Exception ex) {
          throw new DataRetrievalFailureException(
              "Unable to map column " + column + " to " + fd.getFullName() + " of type " +
                  fd.getJavaType() + ", value=" + value, ex);
        }
      }
    }

    return (T) builder.build();
  }

  /**
   * Retrieve a JDBC object value for the specified column.
   * <p>
   * The default implementation calls {@link JdbcUtils#getResultSetValue(ResultSet, int, Class)}.
   * Subclasses may override this to check specific value types upfront, or to post-process values
   * return from {@code getResultSetValue}.
   *
   * @param rs    is the ResultSet holding the data
   * @param index is the column index
   * @param fd    the bean property that each result object is expected to match (or {@code null} if
   *              none specified)
   * @return the Object value
   * @throws SQLException in case of extraction failure
   * @see JdbcUtils#getResultSetValue(ResultSet, int, Class)
   */
  public Object getColumnValue(ResultSet rs, int index, FieldDescriptor fd) throws SQLException {
    return JdbcUtils.getResultSetValue(rs, index, fieldResolver.resolveSqlValueType(fd));
  }

}
