/**
 * @author yuanwq, date: 2016年2月17日
 */
package protobufframework.orm.dao;

import com.google.protobuf.Descriptors.FieldDescriptor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;
import protobufframework.core.ProtoMessageHelper;

import javax.annotation.Nonnull;
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
  private final IProtoSqlConverter sqlConverter;
  private final FieldDescriptor fd;

  public ProtoFieldRowMapper(@Nonnull ProtoMessageHelper<?> messageHelper,
      @Nonnull IProtoSqlConverter sqlConverter, @Nonnull FieldDescriptor fd) {
    if (fd.isRepeated()) {
      throw new UnsupportedOperationException(
          "not supported for repeated field, field=" + fd.getFullName());
    }
    if (fd.getJavaType().equals(FieldDescriptor.JavaType.ENUM)) {
      throw new UnsupportedOperationException(
          "not supported for enum field, field=" + fd.getFullName() + ", enumType=" +
              fd.getEnumType().getFullName());
    }
    this.messageHelper = messageHelper;
    this.sqlConverter = sqlConverter;
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
