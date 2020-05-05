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

    protected Game gameInstance;
    protected Player currentPlayer = null;
    protected Worker currentWorker = null;
    protected ArrayList<Player> playerOrder = new ArrayList<>();
    protected HashMap<Player, TurnSequence> turnSequenceMap = new HashMap<>();
    protected LinkedList<PlayerMove> movesPerformed = new LinkedList<>();
    protected int currentMoveIndex = 0;

    //TODO: Implementare currentWorker (una volta scelto il worker il player deve usare quello per tutto il turno)

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    protected Turn(Game gameInstance) {
        this.gameInstance = gameInstance;
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
    protected void setPlayerOrder(Player... players) throws IllegalArgumentException {
        if (playerOrder.isEmpty()) {
            if (players.length != gameInstance.getPlayerNumber())
                throw new IllegalArgumentException();

            playerOrder.addAll(Arrays.asList(players));
            playerOrder.sort(Comparator.comparing(Player::getBirthday).reversed());
        }
    }

    /**
     * Update the canMoveUp property of the other player
     *
     * @param canMoveUpValue New boolean value of the canMoveUp property
     */
    public void setOtherPlayerCanMoveUpTo(Boolean canMoveUpValue) {
        for (Player player : turnSequenceMap.keySet())
            if (player != currentPlayer)
                turnSequenceMap.get(player).setCanMoveUp(canMoveUpValue);
    }

    /**
     * Update the turn to the next move to be performed, or to the next player
     */
    protected void updateTurn() {
        if (currentPlayer == null)
            updateToNextPlayerTurn();
        TurnSequence currentTurnSequence = turnSequenceMap.get(currentPlayer);
        if (currentMoveIndex < currentTurnSequence.getMoveSequence().size()) {
            currentTurnSequence.getMoveSequence().get(currentMoveIndex);
            currentMoveIndex++;
            //TODO: Notificare la nuova mossa alla view
        } else
            updateToNextPlayerTurn();

    }

    /**
     * Update the currentPlayer to move to the next player's turn
     */
    protected void updateToNextPlayerTurn() {
        if (currentPlayer == null)
            currentPlayer = playerOrder.get(0);
        else
            currentPlayer = getNextPlayer();

        currentMoveIndex = 0;
        movesPerformed.clear();
    }

    /**
     * Add to the movesPerformed list (necessary for the management of the UNDO function and
     * some game logic check) the move just performed
     *
     * @param lastMove Last move performed
     */
    public void addLastMovePerformed(PlayerMove lastMove) {
        movesPerformed.add(lastMove);
    }

    /**
     * Returns the game back to the state before the last move performed
     */
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

    /**
     * Returns the list of moves performed so far during the turn
     *
     * @return Returns a LinkedList of PlayerMove containing the moves performed by the user so far in this turn
     */
    public LinkedList<PlayerMove> getMovesPerformed() {
        return movesPerformed;
    }

    /**
     * Returns the next player by cycling on the list of players sorted by age
     *
     * @return Return the next player to play
     */
    protected Player getNextPlayer() {
        int index = playerOrder.indexOf(currentPlayer);

        return playerOrder.get((index + 1) % playerOrder.size());
    }

    /**
     * Returns the boolean that describes whether the player can move up during this turn or not
     *
     * @return A boolean describing the possibility of move up or not during this turn for the player
     */
    public boolean canCurrentPlayerMoveUp() {
        return turnSequenceMap.get(currentPlayer).isCanMoveUp();
    }

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
