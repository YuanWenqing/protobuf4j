/**
 * @author yuanwq, date: 2016年2月17日
 */
package org.protoframework.dao;

import com.google.protobuf.Descriptors.FieldDescriptor;
import org.protoframework.core.ProtoMessageHelper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 将数据库值映射处理为Protobuf Message的字段值
 * <p>
 *
 * @author yuanwq
 */
public class ProtoFieldRowMapper<F> implements RowMapper<F> {

  private final ProtoMessageHelper<?> messageHelper;
  private final ProtoSqlConverter sqlConverter;
  private final FieldDescriptor fd;

  public ProtoFieldRowMapper(ProtoMessageHelper<?> messageHelper, FieldDescriptor fd) {
    if (fd.getJavaType().equals(FieldDescriptor.JavaType.ENUM)) {
      throw new RuntimeException("not supported for enum field, field=" + fd.getFullName());
    }
    this.messageHelper = messageHelper;
    this.sqlConverter =
        (ProtoSqlConverter) SqlConverterRegistry.findSqlConverter(messageHelper.getType());
    this.fd = fd;
  }

  @SuppressWarnings("unchecked")
  @Override
  public F mapRow(ResultSet rs, int rowNum) throws SQLException {
    Object value = JdbcUtils.getResultSetValue(rs, 1, sqlConverter.resolveSqlValueType(fd));
    if (value == null) return null;
    return (F) sqlConverter.fromSqlValue(messageHelper, fd, value);
  }

}
