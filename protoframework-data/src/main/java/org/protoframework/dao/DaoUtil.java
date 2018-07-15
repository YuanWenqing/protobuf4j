/**
 * @author yuanwq, date: 2017年2月17日
 */
package org.protoframework.dao;

import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * @author yuanwq
 */
public abstract class DaoUtil {

  public static PreparedStatementCreator makeStatementCreator(String sql,
      List<? extends Object> sqlValues) {
    return new PreparedStatementCreator() {
      @Override
      public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
        PreparedStatement ps = con.prepareStatement(sql);
        if (sqlValues.isEmpty()) return ps;
        int i = 1;
        for (Object value : sqlValues) {
          ps.setObject(i++, value);
        }
        return ps;
      }
    };
  }

  public static <V> RowMapper<V> getSingleColumnMapper(Class<V> clazz) {
    return new SingleColumnRowMapper<V>(clazz);
  }

}
