package ee.ut.jf2016.hw6;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;

public class ChatClient {
  public static void main(String[] args) throws IOException {
    BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

    String name = "";
    while (name.isEmpty()) {
      System.out.print("Enter name: ");
      name = stdin.readLine().trim();
    }

    System.out.println("Connecting...");

    SocketChannel socket = SocketChannel.open(new InetSocketAddress(7070));
    Writer writer = Channels.newWriter(socket, "UTF-8");
    writer.write(name + "\n");
    writer.flush();

    System.out.println("Connected, you can type your messages now");

    Thread stdinReader = new Thread(() -> {
      try {
        while (true) {
          String line = stdin.readLine();
          if (line == null || "/q".equals(line) || "/quit".equals(line) || "/exit".equals(line)) {
            socket.close();
            break;
          }
          line = line.trim();
          if (!line.isEmpty()) {
            writer.write(line + "\n");
            writer.flush();
          }
        }
      }
      catch (IOException e) {
        e.printStackTrace();
      }
    });
    stdinReader.setDaemon(true);
    stdinReader.start();

    BufferedReader reader = new BufferedReader(Channels.newReader(socket, "UTF-8"));
    String line;
    try {
      while ((line = reader.readLine()) != null) {
        System.out.println(line);
      }
    }
    catch (AsynchronousCloseException ignored) {}
  }
}
