/**
 * author yuanwq, date: 2016年2月17日
 */
package protobuf4j.orm.dao;

import com.google.protobuf.Descriptors.FieldDescriptor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;
import protobuf4j.orm.converter.IFieldResolver;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 将数据库值映射处理为Protobuf Message的字段值
 * <p>
 * <p>
 * author yuanwq
 */
public class ProtoFieldRowMapper<F> implements RowMapper<F> {

  private final IFieldResolver fieldResolver;
  private final FieldDescriptor fd;

  public ProtoFieldRowMapper(IFieldResolver fieldResolver, FieldDescriptor fd) {
    if (fd.isRepeated()) {
      throw new UnsupportedOperationException(
          "not supported for repeated field, field=" + fd.getFullName());
    }
    if (fd.getJavaType().equals(FieldDescriptor.JavaType.ENUM)) {
      throw new UnsupportedOperationException(
          "not supported for enum field, field=" + fd.getFullName() + ", enumType=" +
              fd.getEnumType().getFullName());
    }
    this.fieldResolver = fieldResolver;
    this.fd = fd;
  }

  @SuppressWarnings("unchecked")
  @Override
  public F mapRow(ResultSet rs, int rowNum) throws SQLException {
    Object value = JdbcUtils.getResultSetValue(rs, 1, fieldResolver.resolveSqlValueType(fd));
    if (value == null) return null;
    return (F) fieldResolver.fromSqlValue(fd, value);
  }

}
