package it.polimi.ingsw.client;

import it.polimi.ingsw.model.Board;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Client {

  private String ip;
  private int port;

  public Client(String ip, int port) {
    this.ip = ip;
    this.port = port;
  }

  /* public void startClient() throws IOException {

    Socket socket = new Socket(ip, port);
    System.out.println("Connection established");
    Scanner socketIn = new Scanner(socket.getInputStream());
    PrintWriter socketOut = new PrintWriter(socket.getOutputStream());
    Scanner stdin = new Scanner(System.in);
    try{
      while (true){
        String inputLine = stdin.nextLine();
        socketOut.println(inputLine);
        socketOut.flush();
        String socketLine = socketIn.nextLine();
        System.out.println(socketLine);
      }

    } catch(NoSuchElementException e){
      System.out.println("Connection closed");

    } finally {
      stdin.close();
      socketIn.close();
      socketOut.close();
      socket.close();
    }
  }
*/
}
