package it.polimi.ingsw.model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.*;


public class Turn {

    protected static Player currentPlayer = null;
    protected static ArrayList<Player> playerOrder = new ArrayList<>();
    public static HashMap<Player, ArrayList<TurnEvents>> moveSequence = new HashMap<>();

    public void setPlayerOrder(Player... players) throws IllegalArgumentException {
        if (playerOrder.isEmpty()) {
            if (players.length != Game.getInstance().getPlayerNumber())
                throw new IllegalArgumentException();

            playerOrder.addAll(Arrays.asList(players));

            playerOrder.sort(Comparator.comparing(Player::getBirthday));
        }
    }

    public void updateTurn() {
        if (currentPlayer == null)
            currentPlayer = playerOrder.get(0);
        else
            currentPlayer = getNextPlayer();

//        updateMoveSequence();
    }

    //TODO: Test
    protected Player getNextPlayer() {
        int index = playerOrder.indexOf(currentPlayer);

        return playerOrder.get((index + 1) % playerOrder.size());
    }

    public void setUpMoveSequence() {
        try {
            File xmlChosenCards = new File("src/GodsParameters.xml");
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setIgnoringElementContentWhitespace(true);
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(xmlChosenCards);
            document.normalizeDocument();

            NodeList list = null;

            for (Player player : playerOrder) {
                list = null;
                list = document.getElementsByTagName(player.getPlayerCard().getCardName());
                Element godElement = (Element) list.item(0);
                list = godElement.getElementsByTagName("moveSequence");
                Element godElementMove = (Element) list.item(0);
                list = godElementMove.getChildNodes();

                HashMap<TurnEvents, Integer> moveSequenceTemp = new HashMap<>();
                for (int i = 0; i < list.getLength(); i++) {
                    Node moveNode = list.item(i);
                    if (moveNode.getNodeType() != Node.ELEMENT_NODE && moveNode.getNextSibling() != null) {
                        Node sibling = moveNode.getNextSibling();
                        Integer priority = Integer.parseInt(sibling.getAttributes().getNamedItem("priority").getTextContent());
                        moveSequenceTemp.put(TurnEvents.valueOf(sibling.getTextContent()), priority);
                    }
                }
                List<Map.Entry<TurnEvents, Integer>> listToSort = new LinkedList<>(moveSequenceTemp.entrySet());
                listToSort.sort(Map.Entry.comparingByValue());
                moveSequenceTemp.clear();
                for (Map.Entry<TurnEvents, Integer> entry : listToSort)
                    moveSequenceTemp.put(entry.getKey(), entry.getValue());

                moveSequence.put(player, new ArrayList<>(moveSequenceTemp.keySet()));
            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    //Rivedere. Ad ora non utile
//    protected void updateMoveSequence() {
//        try {
//            File xmlChosenCards = new File("src/ChosenCards.xml");
//            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
//            documentBuilderFactory.setIgnoringElementContentWhitespace(true);
//            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
//            Document document = documentBuilder.parse(xmlChosenCards);
//            document.normalizeDocument();
//
//            NodeList list = document.getElementsByTagName(currentPlayer.getPlayerCard().getCardName());
//            Element godElement = (Element) list.item(0);
//            NodeList godParameterNodes = godElement.getChildNodes();
//
//        } catch (Exception ex) {
//            System.out.println(ex.getMessage());
//        }
//
//    }
}
