/**
 * @author yuanwq, date: 2016年2月17日
 */
package org.protoframework.dao;

import com.google.protobuf.Descriptors.FieldDescriptor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 将数据库值映射处理为Protobuf Message的字段值
 * <p>
 *
 * @author yuanwq
 */
public class ProtoFieldRowMapper<F> implements RowMapper<F> {

  private final FieldDescriptor fd;

  public ProtoFieldRowMapper(FieldDescriptor fd) {
    if (fd.getJavaType().equals(FieldDescriptor.JavaType.ENUM)) {
      throw new RuntimeException("not supported for enum field, field=" + fd.getFullName());
    }
    this.fd = fd;
  }

  @SuppressWarnings("unchecked")
  @Override
  public F mapRow(ResultSet rs, int rowNum) throws SQLException {
    Object value = ProtoMessageRowMapper.getColumnValue(rs, 1, fd);
    if (value == null) return null;
    return (F) ProtoSqls.mapValue(fd, value);
  }

}
