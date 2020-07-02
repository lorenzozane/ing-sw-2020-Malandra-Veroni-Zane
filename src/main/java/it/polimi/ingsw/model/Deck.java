package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

/**
 * Game Deck. Contains all the God Cards of the game. Contains also the cards chosen by the challenger
 */
public class Deck {

    Game gameInstance;
    private final ArrayList<GodsCard> cardList = new ArrayList<>();
    private final LinkedHashMap<String, GodsCard> chosenCards = new LinkedHashMap<>();
    private ArrayList<GodsCard> availableCardsToChose = new ArrayList<>();

    /**
     * Constructor of the game deck
     */
    public Deck(Game gameInstance) {
        this.gameInstance = gameInstance;
        buildDeck();
    }

    /**
     * Constructs the game deck by reading the XML file containing the description of the implemented gods
     */
    protected void buildDeck() {
        try {
            //Apertura file xml GodsDescription.xml, ed inizializzazione documento
            File xmlGodsDescription = new File("src/main/resources/GodsDescription.xml");
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setIgnoringElementContentWhitespace(true);
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(xmlGodsDescription);
            //Normalizza gli elementi del file nel caso ci fossero "problemi" di formattazione
            //document.getDocumentElement().normalize();
            document.normalizeDocument();

            //Salva il nodo root e cerca tutti i nodi figli (le divinità)
            Element rootElement = document.getDocumentElement();
            NodeList godsNameNodes = rootElement.getChildNodes();

            //Crea un nuovo nodo singolo per ogni divinità e istanzia una carta aggiungendola a cardList
            for (int i = 0; i < godsNameNodes.getLength(); i++) {
                Node godNode = godsNameNodes.item(i);
                if (godNode.getNodeType() == Node.ELEMENT_NODE) {
                    GodsCard card = new GodsCard(godNode.getNodeName());

                    Element godElement = (Element) godNode;
                    Node descriptionNode = godElement.getElementsByTagName("description").item(0);
                    String godDescription = descriptionNode.getFirstChild().getTextContent();
                    card.setCardDescription(godDescription);
                    cardList.add(card);
                }
            }

            availableCardsToChose = getCardListCopy();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Select and save the cards chosen by the challenger
     *
     * @param godsCardName Names of the gods cards chosen by the challenger
     * @throws IllegalArgumentException Is thrown if the number of cards chosen does not match the number of players in game
     */
    public void chooseCards(String... godsCardName) throws IllegalArgumentException {
        try {
            //If the parameters number is different from the number of players, an IllegalArgumentException is thrown
            if (godsCardName.length != gameInstance.getPlayerNumber())
                throw new IllegalArgumentException();

            for (String cardName : godsCardName)
                addCardToChosen(cardName);

        } catch (IllegalArgumentException ex) {
            System.out.println("Exception thrown from Deck.chooseCards");
            throw ex;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Adds a single card to the ArrayList of chosen cards
     *
     * @param godCardName Name of the god card to add to the ArrayList of chosen cards
     * @throws IllegalArgumentException Is thrown if the card to be added has already been added or if it does not exist
     */
    private void addCardToChosen(String godCardName) throws IllegalArgumentException {
        try {
            if (cardList.size() != 0) {
                if (isAGodName(godCardName)) {
                    if (chosenCards.containsKey(godCardName))
                        throw new IllegalArgumentException();

                    chosenCards.put(godCardName, cardList.stream().filter(x -> x.getCardName().equals(godCardName)).findFirst().orElse(null));
                } else {
                    throw new IllegalArgumentException();
                }
            }
        } catch (IllegalArgumentException ex) {
            System.out.println("Exception thrown from Deck.addCardToChosen");
            throw ex;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Select a card from the chosen cards ArrayList
     *
     * @param godCardName Name of the god card to be selected
     * @return Return the requested card if it exists
     * @throws IllegalArgumentException Is thrown if the required card is not present in the chosen cards ArrayList
     */
    public GodsCard pickUpCard(String godCardName) throws IllegalArgumentException {
        try {
            if (chosenCards.size() != 0) {
                if (chosenCards.containsKey(godCardName)) {
                    GodsCard detectedCard = chosenCards.get(godCardName);
                    chosenCards.remove(godCardName);
                    return detectedCard;
                } else {
                    throw new IllegalArgumentException();
                }
            }
            return null;
        } catch (IllegalArgumentException ex) {
            System.out.println("Exception thrown from Deck.pickUpCard");
            throw ex;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }

    /**
     * Remove the specified god card from the available card list.
     *
     * @param godCardName Name of the god you want to remove from the available ones.
     */
    public void removeAvailableCard(String godCardName) {
        availableCardsToChose.removeIf(x -> x.getCardName().equals(godCardName.toLowerCase()));
    }

    /**
     * Verify if a string is a valid god card name.
     *
     * @param godCardName The string to verify.
     * @return Returns a boolean that describe if the string is a valid god name or not.
     */
    public boolean isAGodName(String godCardName) {
        return cardList.stream().anyMatch(x -> x.getCardName().equals(godCardName));
    }

//    /**
//     * Create a copy of the XML file GodsParameters, containing only the copy of the parameters
//     * of the chosen god cards
//     */
//    private void createChosenCardXML(){
//        try {
//            //Apertura file xml GodsParameters.xml, ed inizializzazione documento
//            File xmlGodsDescription = new File("src/GodsParameters.xml");
//            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
//            documentBuilderFactory.setIgnoringElementContentWhitespace(true);
//            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
//            Document sourceDocument = documentBuilder.parse(xmlGodsDescription);
//            sourceDocument.normalizeDocument();
//
//            Path path = Paths.get("src/ChosenCards.xml");
//            Files.deleteIfExists(path);
//
//            Document destinationDocument = documentBuilder.newDocument();
//
//            Element rootElementNewFile = destinationDocument.createElement("santorini");
//            destinationDocument.appendChild(rootElementNewFile);
//
//            for (Map.Entry<String, GodsCard> choseCard : chosenCards.entrySet()) {
//                NodeList list = sourceDocument.getElementsByTagName(choseCard.getValue().getCardName());
//                Element elementCopied = (Element)list.item(0);
//
//                Node nodeCopied = destinationDocument.importNode(elementCopied, true);
//                destinationDocument.getDocumentElement().appendChild(nodeCopied);
//            }
//
//            destinationDocument.normalizeDocument();
//
//            TransformerFactory transformerFactory = TransformerFactory.newInstance();
//            Transformer transformer = transformerFactory.newTransformer();
////            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
//            Source source = new DOMSource(destinationDocument);
//            Result result = new StreamResult(new File("src/ChosenCards.xml"));
//            transformer.transform(source, result);
//
//        } catch (Exception ex) {
//            System.out.println(ex);
//        }
//    }

    /**
     * Return a copy of the ArrayList containing the game card list.
     *
     * @return ArrayList containing the game card list.
     */
    public ArrayList<GodsCard> getCardListCopy() {
        ArrayList<GodsCard> clonedCardList = new ArrayList<>(cardList.size());
        for (GodsCard card : cardList)
            clonedCardList.add(card.clone());

        return clonedCardList;
    }

    /**
     * eturn a copy of the ArrayList containing the available cards.
     *
     * @return ArrayList containing the available cards.
     */
    public ArrayList<GodsCard> getAvailableCardsToChoseCopy() {
        ArrayList<GodsCard> clonedCardList = new ArrayList<>(availableCardsToChose.size());
        for (GodsCard card : availableCardsToChose)
            clonedCardList.add(card.clone());

        return clonedCardList;
    }

    /**
     * Return a copy of the ArrayList containing the chosen cards
     *
     * @return ArrayList of GodsCard containing the chosen cards
     */
    public ArrayList<GodsCard> getChosenCardsCopy() {
        ArrayList<GodsCard> clonedChosenCards = new ArrayList<>(chosenCards.size());
        for (Map.Entry<String, GodsCard> card : chosenCards.entrySet())
            clonedChosenCards.add(card.getValue().clone());

        return clonedChosenCards;
    }
}
