package protobuf4j.example.api.controller;

import com.google.common.collect.Lists;
import com.google.protobuf.util.Timestamps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.web.bind.annotation.*;
import protobuf4j.example.api.DataNotFoundException;
import protobuf4j.example.dao.ExampleDao;
import protobuf4j.example.proto.Example;
import protobuf4j.example.proto.naming.ExampleNaming;
import protobuf4j.orm.sql.*;
import protobuf4j.orm.sql.clause.FromClause;
import protobuf4j.orm.sql.clause.PaginationClause;
import protobuf4j.orm.sql.clause.SelectClause;
import protobuf4j.orm.sql.clause.WhereClause;
import protobuf4j.orm.sql.expr.LogicalExpr;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Author: yuanwq
 * Date: 2019/5/8
 */
@RestController
@RequestMapping("/api/examples")
public class ExampleController {
  @Autowired
  private ExampleDao exampleDao;

  @GetMapping
  public List<Example> list(@RequestParam(value = "prefix", defaultValue = "") String prefix,
      @RequestParam(value = "beg", defaultValue = "0") long beg,
      @RequestParam(value = "end", defaultValue = "0") long end,
      @RequestParam(value = "pn", defaultValue = "1") int pn,
      @RequestParam(value = "limit", defaultValue = "10") int limit) {
    List<IExpression> conds = Lists.newArrayList();
    if (StringUtils.isNotBlank(prefix)) {
      conds.add(FieldAndValue.like(ExampleNaming.NAME, SqlUtil.likePrefix(prefix)));
    }
    if (beg > 0) {
      conds.add(FieldAndValue.gte(ExampleNaming.ID, beg));
    }
    if (end > 0) {
      conds.add(FieldAndValue.lt(ExampleNaming.ID, end));
    }
    WhereClause whereClause = new WhereClause();
    whereClause.setCond(LogicalExpr.and(conds));
    whereClause.orderBy().asc(ExampleNaming.ID);
    whereClause.setPagination(PaginationClause.newBuilder(limit).buildByPageNo(pn));
    return exampleDao.selectByWhere(whereClause);
  }

  @GetMapping("names")
  public List<String> listNames() {
    SelectClause selectClause = new SelectClause();
    selectClause.select(ExampleNaming.NAME);
    FromClause fromClause = QueryCreator.fromType(Example.class);
    SelectSql sql = new SelectSql(selectClause, fromClause);
    return exampleDao.doSelect(sql, new SingleColumnRowMapper<>(String.class));
  }

  @GetMapping("{id}")
  public Example get(@PathVariable("id") long id) {
    return exampleDao.selectOneByPrimaryKey(id);
  }

  @DeleteMapping("{id}")
  public int delete(@PathVariable("id") long id) {
    return exampleDao.deleteByPrimaryKey(id);
  }

  @PostMapping
  public Example create(@RequestParam("name") String name, @RequestParam("tag") List<String> tags,
      HttpServletRequest request) {
    Example example =
        Example.newBuilder().setName(name).addAllTag(tags).putPayload("ip", request.getRemoteAddr())
            .setCreateTime(Timestamps.fromMillis(System.currentTimeMillis()))
            .setUpdateTime(Timestamps.fromMillis(System.currentTimeMillis())).build();
    long id = exampleDao.insertReturnKey(example).longValue();
    IExpression cond = FieldAndValue.eq(ExampleNaming.ID, id);
    example = exampleDao.selectOneByCond(cond);
    return example;
  }

  @PutMapping("{id}")
  public Example update(@PathVariable("id") long id, @RequestParam("name") String name,
      @RequestParam("tag") List<String> tags, HttpServletRequest request) {
    Example example = exampleDao.selectOneByPrimaryKey(id);
    if (example == null) {
      throw new DataNotFoundException("id=" + id);
    }
    Example newValue = example.toBuilder().setName(name).clearTag().addAllTag(tags)
        .putPayload("ip", request.getRemoteAddr())
        .setUpdateTime(Timestamps.fromMillis(System.currentTimeMillis())).build();
    exampleDao.updateMessageByPrimaryKey(newValue, example);
    /* alias to: */
    // IExpression cond = FieldAndValue.eq(ExampleNaming.ID, id);
    // exampleDao.updateMessage(newValue, example, cond);
    return exampleDao.selectOneByPrimaryKey(id);
  }
}
