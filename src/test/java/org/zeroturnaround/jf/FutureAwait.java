package org.zeroturnaround.jf;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.stream.LongStream;

public class FutureAwait implements Runnable {

  private final CountDownLatch countDownLatch;
  private final Future<?> future;

  public FutureAwait(CountDownLatch countDownLatch, Future<?> future) {
    this.countDownLatch = countDownLatch;
    this.future = future;
  }

  @Override
  public void run() {
    try {
      future.get();
      countDownLatch.countDown();
    }
    catch (Throwable e) {
      LongStream.range(0, countDownLatch.getCount()).forEach(c -> countDownLatch.countDown());
      throw new RuntimeException(e.getCause());
    }
  }
}
