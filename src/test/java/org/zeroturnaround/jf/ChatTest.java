package org.zeroturnaround.jf;

import static org.assertj.core.api.Assertions.assertThat;
import static org.zeroturnaround.jf.Dialogue.line;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.Test;
import org.zeroturnaround.process.Processes;

/**
 * NB! Test expects the latest version of your compiled classes to be in target/classes
 */
public class ChatTest {

  private static List<Process> processes = new ArrayList<>();

  static {
    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        for (Process process : processes) {
          try {
            Processes.newPidProcess(process).destroyForcefully();
          }
          catch (Exception e) {
            // ignore
          }
        }
      }
    });
  }

  @Test
  public void verifyProtocol() throws IOException, InterruptedException {
    ExecutorService executor = Executors.newCachedThreadPool();

    Process server = startServer();

    Thread.sleep(1000);

    ChatBot john = new ChatBot(startClient(),
        line("Enter name:", "john"),
        line("Connecting..."),
        line("Connected, you can type your messages now", "i am first!"),
        line("jim has joined"),
        line("jim: i am second!"),
        line("sally has joined"),
        line("sally: i am last!", "hi sally!"),
        line("sally: hi there!"),
        line("sally: let's get outta here!", "/q")
    );

    ChatBot jim = new ChatBot(startClient(),
        line("Enter name:", "jim"),
        line("Connecting..."),
        line("Connected, you can type your messages now", "i am second!"),
        line("sally has joined"),
        line("sally: i am last!"),
        line("john: hi sally!"),
        line("sally: hi there!"),
        line("sally: let's get outta here!"),
        line("john has left"),
        line("sally has left", "/q")
    );

    ChatBot sally = new ChatBot(startClient(),
        line("Enter name:", "sally"),
        line("Connecting..."),
        line("Connected, you can type your messages now", "i am last!"),
        line("john: hi sally!", "hi there!", "let's get outta here!"),
        line("john has left", "/q")
    );

    Future<?> johnFuture = executor.submit(john);

    Thread.sleep(500);

    Future<?> jimFuture = executor.submit(jim);

    Thread.sleep(500);

    Future<?> sallyFuture = executor.submit(sally);

    CountDownLatch latch = new CountDownLatch(3);

    executor.execute(new FutureAwait(latch, johnFuture));
    executor.execute(new FutureAwait(latch, jimFuture));
    executor.execute(new FutureAwait(latch, sallyFuture));

    latch.await();

    assertThat(john.getDialogue().hasCompletedAsExpected())
        .overridingErrorMessage("john's dialogue did not complete as expected!")
        .isTrue();
    assertThat(jim.getDialogue().hasCompletedAsExpected())
        .overridingErrorMessage("jim's dialogue did not complete as expected!")
        .isTrue();
    assertThat(sally.getDialogue().hasCompletedAsExpected())
        .overridingErrorMessage("sally's dialogue did not complete as expected!")
        .isTrue();

    Processes.newPidProcess(server).destroyGracefully();
  }

  @Test
  public void verifyReturnsClientList() throws InterruptedException, IOException {
    ExecutorService executor = Executors.newCachedThreadPool();

    Process server = startServer();

    Thread.sleep(1000);

    ChatBot john = new ChatBot(startClient(),
        line("Enter name:", "john"),
        line("Connecting..."),
        line("Connected, you can type your messages now"),
        line("jim has joined"),
        line("sally has joined")
    );

    ChatBot jim = new ChatBot(startClient(),
        line("Enter name:", "jim"),
        line("Connecting..."),
        line("Connected, you can type your messages now"),
        line("sally has joined")
    );

    ChatBot sally = new ChatBot(startClient(),
        line("Enter name:", "sally"),
        line("Connecting..."),
        line("Connected, you can type your messages now")
    );

    Future<?> johnFuture = executor.submit(john);

    Thread.sleep(500);

    Future<?> jimFuture = executor.submit(jim);

    Thread.sleep(500);

    Future<?> sallyFuture = executor.submit(sally);

    Thread.sleep(500);

    assertThat(getClientList()).containsOnly("john", "jim", "sally");

    Processes.newPidProcess(john.getProcess()).destroyForcefully();
    Processes.newPidProcess(jim.getProcess()).destroyForcefully();
    Processes.newPidProcess(sally.getProcess()).destroyForcefully();

    Thread.sleep(500);

    assertThat(getClientList()).containsOnly("");
  }

  private List<String> getClientList() throws IOException {
    List<String> list = new ArrayList<>();

    try (SocketChannel socket = SocketChannel.open(new InetSocketAddress(8080));
         BufferedReader reader = new BufferedReader(Channels.newReader(socket, "UTF-8"))) {
      String line;

      while ((line = reader.readLine()) != null) {
        list.add(line);
      }
    }

    return list;
  }

  private Process startClient() throws IOException {
    return startedProcess("c");
  }

  private Process startServer() throws IOException {
    return startedProcess("s");
  }

  private Process startedProcess(String kind) throws IOException {
    Process process = new ProcessBuilder()
        .command(
            System.getProperty("java.home") + File.separator + "bin" + File.separator + "java",
            "-cp",
            "target/classes",
            "org.zeroturnaround.jf.Main",
            kind
        )
        .start();

    processes.add(process);

    return process;
  }
}
