package protobuf4j.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import protobuf4j.core.ProtobufObjectMapper;

/**
 * Author: yuanwq
 * Date: 2019/5/8
 */
@Configuration
public class ExampleConfiguration {
  @Bean
  public ProtobufObjectMapper protobufObjectMapper() {
    return ProtobufObjectMapper.DEFAULT;
  }

}
