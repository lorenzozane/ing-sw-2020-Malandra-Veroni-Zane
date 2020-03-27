package it.polimi.ingsw.server;

import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

  private static final int PORT = 12345;
  private ServerSocket serverSocket;
  private ExecutorService executor = Executors.newFixedThreadPool(128);
  //private Map<String, ClientConnection> waitingConnection = new HashMap<>();
  //private Map<ClientConnection, ClientConnection> playingConnection = new HashMap<>();

}
