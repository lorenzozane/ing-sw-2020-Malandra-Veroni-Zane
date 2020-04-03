package it.polimi.ingsw.model;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.graalvm.compiler.core.common.type.ArithmeticOpTable;
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
            NodeList godsNameNodes = rootElement.getChildNodes();

            //Crea un nuovo nodo singolo per ogni divinità e istanzia una carta aggiungendola a cardList
            for (int i = 0; i < godsNameNodes.getLength(); i++){
                Node godNode = godsNameNodes.item(i);
                if (godNode.getNodeType() == Node.ELEMENT_NODE){
                    GodsCard card = new GodsCard(godNode.getNodeName());

                    //TODO: Riuscire a prendere il nodo description ignorando gli \n
                    Element godElement = (Element)godNode;
                    Node descriptionNode = godElement.getElementsByTagName("description").item(0);
                    String godDescription = descriptionNode.getNodeValue();
                    card.setCardDescription(godDescription);
                    cardList.add(card);
                }
            }
        } catch (Exception ex){
            System.out.println(ex);
        }
    }

    public void chooseCards(String ... godsCardName){
        try {
            for (int i = 0; i < godsCardName.length; i++){
                chosenCards.add(pickUpCard(godsCardName[i]));
            }
        } catch (Exception ex){
            System.out.println(ex);
        }
    }

    public GodsCard pickUpCard(String godCardName){
        try {
            if (cardList.size() != 0){

            }
            return null;
        } catch (Exception ex){
            System.out.println(ex);
            return null;
        }
    }

    public ArrayList<GodsCard> getCardList(){
        return cardList;
    }

    public ArrayList<GodsCard> getChosenCards(){
        return chosenCards;
    }
}
