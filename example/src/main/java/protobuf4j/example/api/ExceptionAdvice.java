package protobuf4j.example.api;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Author: yuanwq
 * Date: 2019/5/8
 */
@ControllerAdvice
@Slf4j
public class ExceptionAdvice {
  @ResponseBody
  @ExceptionHandler(DataNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public Map<String, Object> handleNotFoundException(DataNotFoundException ex,
      HttpServletRequest request) {
    return handle(ex, request);
  }

  private Map<String, Object> handle(Throwable ex, HttpServletRequest request) {
    Map<String, Object> map = Maps.newLinkedHashMap();
    map.put("error", ex.getMessage());
    String url = request.getMethod() + " " + request.getRequestURL();
    log.warn(url, ex);
    return map;
  }

  @ResponseBody
  @ExceptionHandler(Throwable.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public Map<String, Object> handleOtherException(Throwable ex, HttpServletRequest request) {
    return handle(ex, request);
  }

}
