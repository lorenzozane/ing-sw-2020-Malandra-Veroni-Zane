package it.polimi.ingsw;

import it.polimi.ingsw.network.Client;

import java.io.IOException;
import java.util.Scanner;

public class ClientApp {
    public static void main(String[] args) throws IOException {
        Client client = new Client("127.0.0.1", 12345);
        Scanner in = new Scanner(System.in);

        System.out.println("Choose CLI or GUI");
        String input = in.nextLine();
        while(!(checkInterface(input))){
            System.out.println("Not supported\nChoose CLI or GUI");
            input = in.nextLine();
        }

        if(input.equalsIgnoreCase("GUI")){
//            client.setGui(true);
            //Gui.main();
        }

        try{
            client.run();
        }catch (IOException e){
            System.err.println(e.getMessage());
        }
    }



    private static boolean checkInterface(String input){
        return input.equalsIgnoreCase("GUI") || input.equalsIgnoreCase("CLI");
    }
}
