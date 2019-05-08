package protobuf4j.example.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import protobuf4j.example.dao.ExampleDao;

/**
 * Author: yuanwq
 * Date: 2019/5/8
 */
@RestController("/api/example")
public class ExampleController {
  @Autowired
  private ExampleDao exampleDao;

}
