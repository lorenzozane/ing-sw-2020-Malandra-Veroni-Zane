package it.polimi.ingsw;

import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.network.Client.UserInterface;
import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.cli.Cli;
import it.polimi.ingsw.view.gui.Gui;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Startup the client application.
 */
public class ClientApp {
    /**
     * Main of client with CLI interface.
     *
     * @param args IP and PORT to create a socket.
     */
    public static void main(String[] args) {
        Client client;
        try {
            if (args.length < 2)
                client = new Client("127.0.0.1", 12345);
            else
                client = new Client(args[0], Integer.parseInt(args[1]));
        } catch (Exception e) {
            while (true) {
                System.out.println("Server not reachable or IP:PORT not valid");
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

        System.out.println(Message.santorini);

        View view = new View(new Cli());
        client.setChosenUserInterface(view, UserInterface.CLI);

        client.addUpdateTurnMessageObserver(view.getUpdateTurnMessageReceiver());
        client.addStringObserver(view.getStringReceiver());

        view.addPlayerMoveObserver(client.getPlayerMoveReceiver());
        view.addPlayerMoveStartupObserver(client.getPlayerMoveStartupReceiver());
        view.addStringObserver(client.getStringReceiver());

        client.run();
    }

    /**
     * Method to check the IPv4 format.
     *
     * @param ip choose from client.
     * @return Returns a boolean that describe if the ip is in a valid format.
     */
    private static boolean checkIp(String ip) {
        Pattern pattern;
        Matcher matcher;
        String IP_ADDRESS_PATTERN
                = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
                + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
                + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
                + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
        pattern = Pattern.compile(IP_ADDRESS_PATTERN);
        matcher = pattern.matcher(ip);
        return matcher.matches();
    }

    /**
     * Method to check the port number.
     *
     * @param port choose from client.
     * @return Returns a boolean that describe if the port is in a valid format.
     */
    private static boolean checkPort(String port) {
        return (Integer.parseInt(port) > 1500 && Integer.parseInt(port) < 65535);
    }
}
