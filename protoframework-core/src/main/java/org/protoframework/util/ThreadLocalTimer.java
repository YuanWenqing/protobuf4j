/**
 * @author yuanwq, date: 2017年2月17日
 */
package org.protoframework.util;

import com.google.common.base.Stopwatch;

import java.util.concurrent.TimeUnit;

/**
 * 线程安全的计时器
 *
 * @author yuanwq
 * @see Stopwatch
 */
public class ThreadLocalTimer extends ThreadLocal<Stopwatch> {
  @Override
  protected Stopwatch initialValue() {
    return Stopwatch.createUnstarted();
  }

  public void restart() {
    get().reset().start();
  }

  /**
   * stop and return elapsed time in desired unit
   */
  public long stop(TimeUnit desiredUnit) {
    return get().stop().elapsed(desiredUnit);
  }

  public long elapsed(TimeUnit desiredUnit) {
    return get().elapsed(desiredUnit);
  }

}
