package it.polimi.ingsw;

import it.polimi.ingsw.network.Server;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServerApp {
    /**
     * Main of Server.
     *
     * @param args IP and PORT to create a socket.
     */
    public static void main(String[] args) {
        Server server;
        if (args.length == 2) {
            try {
                server = new Server(InetAddress.getByName(args[0]), Integer.parseInt(args[1]));
                server.run();
            } catch (Exception exception) {
                while (true) {
                    System.out.println("IP or PORT not valid");
                    System.out.println("Please enter a valid IP (xxx.xxx.xxx.xxx format)");
                    Scanner in = new Scanner(System.in);
                    String ip = in.nextLine();
                    System.out.println("Please enter a valid PORT");
                    String port = in.nextLine();
                    if (checkIp(ip) && checkPort(port)) {
                        String[] arrayString = new String[2];
                        arrayString[0] = ip;
                        arrayString[1] = port;
                        main(arrayString);
                    }
                }
            }
        } else {
            try {
                server = new Server();
                server.run();
            } catch (IOException e) {
                while (true) {
                    System.out.println("IP or PORT not valid");
                    System.out.println("Please enter a valid IP (xxx.xxx.xxx.xxx format)");
                    Scanner in = new Scanner(System.in);
                    String ip = in.nextLine();
                    System.out.println("Please enter a valid PORT");
                    String port = in.nextLine();
                    if (checkIp(ip) && checkPort(port)) {
                        String[] arrayString = new String[2];
                        arrayString[0] = ip;
                        arrayString[1] = port;
                        main(arrayString);
                    }
                }
            }
        }
    }

    /**
     * Method to check the IPv4 format.
     *
     * @param ip choose from client.
     * @return Returns a boolean describing if the ip is in a valid format or not.
     */
    private static boolean checkIp(String ip) {
        Pattern pattern;
        Matcher matcher;
        String IPADDRESS_PATTERN
                = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
                + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
                + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
                + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
        pattern = Pattern.compile(IPADDRESS_PATTERN);
        matcher = pattern.matcher(ip);
        return matcher.matches();
    }

    /**
     * Method to check the port number.
     *
     * @param port choose from client.
     * @return Returns a boolean describing if the port number is valid or not.
     */
    private static boolean checkPort(String port) {
        return (Integer.parseInt(port) > 1500 && Integer.parseInt(port) < 65535);
    }

}
