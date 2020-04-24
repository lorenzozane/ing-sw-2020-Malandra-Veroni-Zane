package it.polimi.ingsw.model;

import it.polimi.ingsw.model.TurnEvents.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.*;

/**
 * Structure useful to contain the elements necessary to describe the course of the game turns
 */
public class Turn {

    protected static Player currentPlayer = null;
    protected static Worker currentWorker = null;
    protected static ArrayList<Player> playerOrder = new ArrayList<>();
    protected static HashMap<Player, TurnSequence> turnSequenceMap = new HashMap<>();
    protected static LinkedList<PlayerMove> movesPerformed = new LinkedList<>();
    protected static int currentMoveIndex = 0;
    //protected static LinkedHashMap<TurnEvents.Actions, LinkedList<Slot>> movesPerformed = new LinkedHashMap<>();

    //TODO: Implementare currentWorker (una volta scelto il worker il player deve usare quello per tutto il turno)

    protected Turn() {

    }

    public boolean isPlayerTurn(Player player) {
        return player.equals(currentPlayer);
    }

    public ArrayList<Worker> getCurrentPlayerWorkers() {
        return currentPlayer.getWorkers();
    }

    public void setCurrentWorker(Worker worker) {
        currentWorker = worker;
    }

    public Worker getCurrentWorker() {
        if (currentWorker != null)
            return currentWorker;
        return null;
    }

    public void resetCurrentWorker() {
        currentWorker = null;
    }

    public TurnSequence getCurrentPlayerTurnSequence() {
        return turnSequenceMap.get(currentPlayer);
    }

    /**
     * Defines the player order based on age to create the ArrayList playerOrder
     *
     * @param players List of players
     * @throws IllegalArgumentException Is thrown if the number of players to compare does not match the numbers of players in game
     */
    protected static void setPlayerOrder(Player... players) throws IllegalArgumentException {
        if (playerOrder.isEmpty()) {
            if (players.length != Game.getInstance().getPlayerNumber())
                throw new IllegalArgumentException();

            playerOrder.addAll(Arrays.asList(players));
            playerOrder.sort(Comparator.comparing(Player::getBirthday).reversed());
        }
    }

    /**
     * Update the currentPlayer to move to the next player's turn
     */
    protected void updateTurn() {
        TurnSequence currentTurnSequence = turnSequenceMap.get(currentPlayer);
        if (currentMoveIndex < currentTurnSequence.getMoveSequence().size()) {
            currentTurnSequence.getMoveSequence().get(currentMoveIndex);
            currentMoveIndex++;
            //TODO: Notificare la nuova mossa alla view
        } else
            updateToNextPlayerTurn();

    }

    protected void updateToNextPlayerTurn() {
        if (currentPlayer == null)
            currentPlayer = playerOrder.get(0);
        else
            currentPlayer = getNextPlayer();

        currentMoveIndex = 0;
        movesPerformed.clear();
    }

    //TODO: Implementazione movesPerformed
    public void addLastMovePerformed(PlayerMove lastMove) {
        movesPerformed.add(lastMove);
    }

    public void restoreToLastMovePerformed() {
        if (!movesPerformed.isEmpty()) {
            PlayerMove moveToRestore = movesPerformed.getLast();
            if (moveToRestore.getMove().getActionType() == Actions.ActionType.MOVEMENT) {
                //Invert the sequence of starting and target slot to make the reverse move
                Slot targetSlot = moveToRestore.getStartingSlot();
                moveToRestore.getMovedWorker().move(targetSlot);
            } else if (moveToRestore.getMove().getActionType() == Actions.ActionType.BUILDING) {
                moveToRestore.getTargetSlot().destroyTopBuilding();
            }
        } else
            return; //TODO: Gestire
    }

    protected LinkedHashMap<TurnEvents.Actions, LinkedList<Slot>> getMovesPerformed() {


        return null;
    }

    //TODO: Test

    /**
     * Returns the next player by cycling on the list of players sorted by age
     *
     * @return Return the next player to play
     */
    protected Player getNextPlayer() {
        int index = playerOrder.indexOf(currentPlayer);

        return playerOrder.get((index + 1) % playerOrder.size());
    }

    public static boolean canCurrentPlayerMoveUp() {
        return turnSequenceMap.get(currentPlayer).isCanMoveUp();
    }

//    protected void updateTurnSequence() {
//        try {
//
//        } catch (Exception ex) {
//            System.out.println(ex.getMessage());
//        }
//    }

    //TODO: Cambiare un parametro di Prometheus. Necessario affinchè non salga se costruisce prima di muovere

    /**
     * Set up the Turn Sequence of each player in game during the game set up. Builds up the standard sequence of moves
     * and read the win conditions
     */
    protected void setUpTurnSequence() {
        try {
            File xmlChosenCards = new File("src/GodsParameters.xml");
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setIgnoringElementContentWhitespace(true);
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(xmlChosenCards);
            document.normalizeDocument();

            for (Player player : playerOrder) {
                ArrayList<Actions> moveSequence = loadMoveSequence(player, document);
                ArrayList<WinConditions> winConditions = loadWinCondition(player, document);

                TurnSequence turnSequence = new TurnSequence(player, moveSequence, winConditions);
                turnSequenceMap.put(player, turnSequence);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Builds up the standard sequence of moves for each player
     *
     * @param player   Player to create the move sequence for
     * @param document Document to read to extract all the player's turn parameters
     * @return Return the ArrayList of player's move sequence
     */
    private ArrayList<Actions> loadMoveSequence(Player player, Document document) {
        try {
            NodeList list = document.getElementsByTagName(player.getPlayerCard().getCardName());
            Element godElement = (Element) list.item(0);
            list = godElement.getElementsByTagName("moveSequence");
            Element godElementMove = (Element) list.item(0);
            list = godElementMove.getChildNodes();

            HashMap<Actions, Integer> moveSequence = new HashMap<>();
            for (int i = 0; i < list.getLength(); i++) {
                Node moveNode = list.item(i);
                if (moveNode.getNodeType() != Node.ELEMENT_NODE && moveNode.getNextSibling() != null) {
                    Node sibling = moveNode.getNextSibling();
                    Integer priority = Integer.parseInt(sibling.getAttributes().getNamedItem("priority").getTextContent());
                    moveSequence.put(Actions.valueOf(sibling.getTextContent()), priority);
                }
            }
            List<Map.Entry<Actions, Integer>> listToSort = new LinkedList<>(moveSequence.entrySet());
            listToSort.sort(Map.Entry.comparingByValue());
            moveSequence.clear();
            for (Map.Entry<Actions, Integer> entry : listToSort)
                moveSequence.put(entry.getKey(), entry.getValue());

            return new ArrayList<>(moveSequence.keySet());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }

    /**
     * Load the win condition for each player
     *
     * @param player   Player to read the win conditions for
     * @param document Document to read to extract all the player's win conditions
     * @return Return the ArrayList of player's win conditions
     */
    private ArrayList<WinConditions> loadWinCondition(Player player, Document document) {
        try {
            NodeList list = document.getElementsByTagName(player.getPlayerCard().getCardName());
            Element godElement = (Element) list.item(0);
            list = godElement.getElementsByTagName("winConditions");
            Element godElementWin = (Element) list.item(0);
            list = godElementWin.getChildNodes();

            ArrayList<WinConditions> winConditions = new ArrayList<>();
            for (int i = 0; i < list.getLength(); i++) {
                Node winNode = list.item(i);
                if (winNode.getNodeType() != Node.ELEMENT_NODE && winNode.getNextSibling() != null) {
                    Node sibling = winNode.getNextSibling();
                    winConditions.add(WinConditions.valueOf(sibling.getTextContent()));
                }
            }

            return winConditions;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }
}
