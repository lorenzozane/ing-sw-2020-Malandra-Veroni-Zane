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

    private ArrayList<GodsCard> cardList = new ArrayList<>();
    private ArrayList<GodsCard> chosenCards = new ArrayList<>();

    public Deck(){
        buildDeck();
    }

    private void buildDeck(){
        try {
            //Apertura file xml GodsDescription.xml, ed inizializzazione documento
            File xmlGodsDescription = new File("src/GodsDescription.xml");
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(xmlGodsDescription);
            //Normalizza gli elementi del file nel caso ci fossero "problemi" di formattazione
            document.getDocumentElement().normalize();

            //Salva il nodo root e cerca tutti i nodi figli (le divinità)
            Element rootElement = document.getDocumentElement();
            NodeList godsNameNode = rootElement.getChildNodes();

            //Crea un nuovo nodo singolo per ogni divinità e istanzia una carta aggiungendola a cardList
            for (int i = 0; i < godsNameNode.getLength(); i++){
                Node godNode = godsNameNode.item(i);
                if (godNode.getNodeType() == Node.ELEMENT_NODE){
                    GodsCard card = new GodsCard(godNode.getNodeName());
                    cardList.add(card);
                }
            }

        } catch (Exception ex){
            System.out.println(ex);
        }
    }

    public GodsCard pickUpCard(){

        return null;
    }

    public void chooseCards(){

    }

    public ArrayList<GodsCard> getCardList(){
        return cardList;
    }
}
