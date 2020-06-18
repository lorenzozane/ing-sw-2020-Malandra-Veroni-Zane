package it.polimi.ingsw;

import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.network.Client.UserInterface;
import it.polimi.ingsw.network.Message;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.cli.Cli;
import it.polimi.ingsw.view.gui.Gui;

import java.io.IOException;
import java.util.Scanner;

public class ClientApp {
    public static void main(String[] args) throws IOException {
        Client client;
        if(args == null || args.length == 0 )
            client = new Client("127.0.0.1", 12345);
        else
            client = new Client(args[0], Integer.parseInt(args[1]));

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



    private static boolean checkInterface(String input){
        return input.equalsIgnoreCase("GUI") || input.equalsIgnoreCase("CLI");
    }
}
