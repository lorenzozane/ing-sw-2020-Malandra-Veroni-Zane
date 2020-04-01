package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import com.sun.org.apache.xerces.internal.dom.DocumentImpl;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;


public class Deck {

    //Verificare Warning "Raw use of parameterized class 'ArrayList'"
    private ArrayList<GodsCard> cardList = new ArrayList<>();
    private ArrayList<GodsCard> chosenCards = new ArrayList<>();

    public Deck(){
        buildDeck();
    }

    private void buildDeck(){
        try {
            File xmlGodsDescription = new File("src/GodsDescription.xml");
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(xmlGodsDescription);
            document.getDocumentElement().normalize();

            Element rootElement = document.getDocumentElement();
            NodeList godsNameNode = rootElement.getChildNodes();

            for (int i = 0; i < godsNameNode.getLength(); i++){
                Node godNode = godsNameNode.item(i);
                if (godNode.getNodeType() == Node.ELEMENT_NODE){
                    GodsCard card = new GodsCard(godNode.getNodeName());
                    //TODO;
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
}
