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

}
