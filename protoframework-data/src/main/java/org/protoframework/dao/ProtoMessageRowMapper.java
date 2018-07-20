/*
 * Copyright 2002-2014 the original author or authors. Licensed under the Apache License, Version
 * 2.0 (the "License"); you may not use this file except in compliance with the License. You may
 * obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by
 * applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */

package org.protoframework.dao;

import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.Message;
import org.protoframework.core.ProtoMessageHelper;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;

import javax.annotation.Nonnull;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

/**
 * 将数据库一行数据映射为Protobuf Message
 *
 * @author yuanwq
 */
public class ProtoMessageRowMapper<T extends Message> implements RowMapper<T> {
  /**
   * The class we are mapping to
   */
  private final Class<T> mappedClass;
  private final ProtoMessageHelper<T> messageHelper;
  private final IProtoSqlConverter sqlConverter;
  /**
   * Whether we're strictly validating
   */
  private boolean checkFullyPopulated = false;

  public ProtoMessageRowMapper(@Nonnull Class<T> mappedClass,
      @Nonnull IProtoSqlConverter sqlConverter) {
    this.mappedClass = mappedClass;
    this.messageHelper = ProtoMessageHelper.getHelper(mappedClass);
    this.sqlConverter = sqlConverter;
  }

  public ProtoMessageRowMapper(@Nonnull Class<T> mappedClass,
      @Nonnull IProtoSqlConverter sqlConverter, boolean checkFullyPopulated) {
    this(mappedClass, sqlConverter);
    this.checkFullyPopulated = checkFullyPopulated;
  }

  /**
   * Get the class that we are mapping to.
   */
  public final Class<T> getMappedClass() {
    return this.mappedClass;
  }

  /**
   * Return whether we're strictly validating that all bean properties have been mapped from
   * corresponding database fields.
   */
  public boolean isCheckFullyPopulated() {
    return this.checkFullyPopulated;
  }

  /**
   * Set whether we're strictly validating that all bean properties have been mapped from
   * corresponding database fields.
   * <p>
   * Default is {@code false}, accepting unpopulated properties in the target bean.
   */
  public void setCheckFullyPopulated(boolean checkFullyPopulated) {
    this.checkFullyPopulated = checkFullyPopulated;
  }

  /**
   * Extract the values for all columns in the current row.
   * <p>
   * Utilizes public setters and result set metadata.
   *
   * @see ResultSetMetaData
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  @Override
  public T mapRow(ResultSet rs, int rowNumber) throws SQLException {
    Message.Builder builder = this.messageHelper.newBuilder();
    ResultSetMetaData rsmd = rs.getMetaData();
    int columnCount = rsmd.getColumnCount();
    Set<String> populatedProperties = new HashSet<>();

    for (int index = 1; index <= columnCount; index++) {
      String column = JdbcUtils.lookupColumnName(rsmd, index);
      FieldDescriptor fd = this.messageHelper.getFieldDescriptor(column);
      if (fd != null) {
        Object value = null;
        try {
          value = getColumnValue(rs, index, fd);
          if (value == null) continue;
          value = sqlConverter.fromSqlValue(messageHelper, fd, value);
          builder.setField(fd, value);
          if (checkFullyPopulated) {
            populatedProperties.add(fd.getName());
          }
        } catch (Exception ex) {
          throw new DataRetrievalFailureException(
              "Unable to map column " + column + " to " + fd.getFullName() + " of type " +
                  fd.getJavaType() + ", value=" + value, ex);
        }
      }
    }

    if (checkFullyPopulated && !populatedProperties.equals(messageHelper.getFieldNames())) {
      throw new InvalidDataAccessApiUsageException("Given ResultSet does not contain all fields " +
          "necessary to populate object of class [" + this.mappedClass + "]: " +
          messageHelper.getFieldNames());
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
    return JdbcUtils.getResultSetValue(rs, index, sqlConverter.resolveSqlValueType(fd));
  }

}
