package protobufframework.orm.util;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;
import static org.junit.Assert.*;

/**
 * author: yuanwq
 * date: 2018/7/23
 */
public class TestThreadLocalTimer {
  @Test
  public void test() throws InterruptedException {
    ThreadLocalTimer timer = new ThreadLocalTimer();
    timer.restart();
    assertTrue(timer.get().isRunning());
    sleep(100);
    assertTrue(timer.get().isRunning());
    assertTrue(timer.elapsed(TimeUnit.MILLISECONDS) >= 100);
    sleep(100);
    assertTrue(timer.stop(TimeUnit.MILLISECONDS) >= 200);
    assertFalse(timer.get().isRunning());
    timer.restart();
    assertTrue(timer.get().isRunning());
    assertTrue(timer.elapsed(TimeUnit.MILLISECONDS) < 100);
  }
}
