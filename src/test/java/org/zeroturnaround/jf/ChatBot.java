package org.zeroturnaround.jf;

import java.io.BufferedOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class ChatBot implements Runnable {

  private final Process process;
  private final Dialogue dialogue;

  public ChatBot(Process process, Dialogue.Line... dialogueLines) {
    this.process = process;
    this.dialogue = new Dialogue(dialogueLines);
  }

  public Process getProcess() {
    return process;
  }

  public Dialogue getDialogue() {
    return dialogue;
  }

  @Override
  public void run() {
    Scanner in = new Scanner(process.getInputStream());
    PrintStream out = new PrintStream(new BufferedOutputStream(process.getOutputStream()), true);

    while (in.hasNextLine()) {
      dialogue.replyTo(in.nextLine()).forEach(out::println);
    }
  }
}
