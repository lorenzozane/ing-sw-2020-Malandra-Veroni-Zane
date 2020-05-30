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
        Client client = new Client("127.0.0.1", 12345);
        Scanner in = new Scanner(System.in);

        System.out.println(Message.santorini);
        System.out.println("Choose CLI or GUI");
        String input = in.nextLine();
        while(!(checkInterface(input))){
            System.out.println("Not supported\nChoose CLI or GUI");
            input = in.nextLine();
        }

        if (input.equalsIgnoreCase("CLI")) {
            View view = new View(new Cli());
            client.setChosenUserInterface(view, UserInterface.CLI);

            client.addUpdateTurnMessageObserver(view.getUpdateTurnMessageReceiver());
            client.addStringObserver(view.getStringReceiver());

            view.addPlayerMoveObserver(client.getPlayerMoveReceiver());
            view.addPlayerMoveStartupObserver(client.getPlayerMoveStartupReceiver());
            view.addStringObserver(client.getStringReceiver());
        } else if (input.equalsIgnoreCase("GUI")){
            View view = new View(new Gui());
            client.setChosenUserInterface(view, UserInterface.GUI);
            //Gui.main();
        }

        client.run();
    }



    private static boolean checkInterface(String input){
        return input.equalsIgnoreCase("GUI") || input.equalsIgnoreCase("CLI");
    }
}
