package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;


public class Deck {

    //Verificare Warning "Raw use of parameterized class 'ArrayList'"
    private ArrayList<GodsCard> cardList = new ArrayList<>();
    private ArrayList<GodsCard> chosenCards = new ArrayList<>();

    private void buildDeck(){
        try {
            File xmlGodsDescription = new File("GodsDescription.xml");
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(xmlGodsDescription);
            document.getDocumentElement().normalize();

            NodeList rootNode = document.getElementsByTagName("santorini");
            //TODO;

        } catch (Exception ex){
            System.out.println(ex);
        }
    }

    public GodsCard pickUpCard(){

        return null;
    }

    public void chooseCards(){

    }
}
