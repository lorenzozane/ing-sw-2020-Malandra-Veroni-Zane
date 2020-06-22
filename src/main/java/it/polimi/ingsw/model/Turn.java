package it.polimi.ingsw.model;

import it.polimi.ingsw.model.TurnEvents.*;
import it.polimi.ingsw.observer.MessageForwarder;
import it.polimi.ingsw.observer.Observer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.AbstractMap.SimpleEntry;
import java.util.*;

/**
 * Structure useful to contain the elements necessary to describe the course of the game turns
 */
public class Turn extends MessageForwarder {

    protected Game gameInstance;
    protected Player currentPlayer = null;
    protected Worker currentWorker = null;
    protected ArrayList<Player> playerOrder = new ArrayList<>();
    protected HashMap<Player, TurnSequence> turnSequenceMap = new HashMap<>();
    protected LinkedList<SimpleEntry<Player, StartupActions>> startupTurnSequence = new LinkedList<>();
    protected LinkedList<PlayerMove> movesPerformed = new LinkedList<>();
    protected int currentMoveIndex = 0;
    protected boolean startupPhase = true;
    protected String lastMovePerformedBy = null;
    protected boolean gameIsFinish = false;
    private final UpdateTurnMessageSender updateTurnMessageSender = new UpdateTurnMessageSender();

    protected Turn(Game gameInstance) {
        this.gameInstance = gameInstance;
    }

    public void addUpdateTurnMessageObserver(Observer<UpdateTurnMessage> observer) {
        updateTurnMessageSender.addObserver(observer);
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean isPlayerTurn(Player player) {
        return player.equals(currentPlayer);
    }

    public int getCurrentMoveIndex() {
        return currentMoveIndex;
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

    protected void resetCurrentWorker() {
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

            setUpStartupPhase();
        }
    }

    /**
     * Populate the startup turn sequence of move to be performed by the player in order to start the game
     */
    protected void setUpStartupPhase() {
        if (startupPhase && playerOrder.size() == gameInstance.getPlayerNumber()) {
            for (Player player : playerOrder.subList(0, 2))
                startupTurnSequence.add(new SimpleEntry<>(player, StartupActions.COLOR_REQUEST));
            if (playerOrder.size() > 2)
                startupTurnSequence.add(new SimpleEntry<>(playerOrder.get(playerOrder.size() - 1), StartupActions.PICK_LAST_COLOR));
            for (int i = 0; i < playerOrder.size(); i++)
                startupTurnSequence.add(new SimpleEntry<>(playerOrder.get(0), StartupActions.CHOOSE_CARD_REQUEST));
            for (Player player : playerOrder.subList(1, playerOrder.size()))
                startupTurnSequence.add(new SimpleEntry<>(player, StartupActions.PICK_UP_CARD_REQUEST));
            startupTurnSequence.add(new SimpleEntry<>(playerOrder.get(0), StartupActions.PICK_LAST_CARD));
            for (Player player : playerOrder) {
                startupTurnSequence.add(new SimpleEntry<>(player, StartupActions.PLACE_WORKER_1));
                startupTurnSequence.add(new SimpleEntry<>(player, StartupActions.PLACE_WORKER_2));
            }

            updateTurn();
        }
    }

    /**
     * Set up the game turn after the game initialization
     */
    public void setUpGameTurn() {
        if (startupPhase) {
            setUpTurnSequence();
            currentPlayer = null;
            currentMoveIndex = 0;
            startupPhase = false;
            updateTurn();
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

    //TODO: Possibile rendere protected?

    /**
     * Throughout the game allow to update the turn to the next move to be performed, or to the next player
     */
    public void updateTurn() {
        if (startupPhase)
            updateTurnStartup();
        else
            updateTurnInGame();
    }

    /**
     * Update the turn to the next move to be performed in the startup phase of the game
     */
    protected void updateTurnStartup() {
        if (startupPhase) {
            if (currentMoveIndex < startupTurnSequence.size()) {
                StartupActions nextStartupMove = startupTurnSequence.get(currentMoveIndex).getValue();
                currentPlayer = startupTurnSequence.get(currentMoveIndex).getKey();
                currentMoveIndex++;
                UpdateTurnMessage updateTurnMessage = new UpdateTurnMessage(nextStartupMove, currentPlayer, gameInstance.getColorList());
                if (nextStartupMove == StartupActions.CHOOSE_CARD_REQUEST) {
                    updateTurnMessage.setAvailableCards(gameInstance.getDeck().getAvailableCardsToChoseCopy());
                } else if (nextStartupMove == StartupActions.PICK_UP_CARD_REQUEST || nextStartupMove == StartupActions.PICK_LAST_CARD) {
                    updateTurnMessage.setAvailableCards(gameInstance.getDeck().getChosenCardsCopy());
                } else if (nextStartupMove == StartupActions.PLACE_WORKER_1 || nextStartupMove == StartupActions.PLACE_WORKER_2) {
                    updateTurnMessage.setBoardCopy(gameInstance.getBoard());
                }


                updateTurnMessageSender.notifyAll(updateTurnMessage);
            } else
                setUpGameTurn();
        }
    }

    /**
     * Update the turn to the next move to be performed, or to the next player
     */
    protected void updateTurnInGame() {
        if (!startupPhase && !gameIsFinish) {
            boolean setStuck = false;
            TurnSequence currentTurnSequence = getCurrentPlayerTurnSequence();
            if (currentPlayer == null || !(currentMoveIndex < currentTurnSequence.getMoveSequence().size()))
                updateToNextPlayerTurn();
            else
                lastMovePerformedBy = currentPlayer.getNickname();

            currentTurnSequence = getCurrentPlayerTurnSequence();
            if (currentMoveIndex < currentTurnSequence.getMoveSequence().size()) {
                Actions nextMove = currentTurnSequence.getMoveSequence().get(currentMoveIndex);

                if (nextMove == Actions.CHOSE_WORKER) {
                    int movableWorker = initialCheckMovableWorker();
                    if (movableWorker < 1) {
                        lose(currentPlayer);
                        return;
                    }
                } else if (nextMove.getActionType() == Actions.ActionType.MOVEMENT &&
                        currentWorker != null) {
                    if (!checkIfNotStuck(currentWorker))
                        setStuck = true;
                } else if (nextMove.getActionType() == Actions.ActionType.BUILDING) {
                    if (!checkIfCanBuild(currentWorker))
                        setStuck = true;
                }

                currentMoveIndex++;
                UpdateTurnMessage updateTurnMessage = new UpdateTurnMessage(gameInstance.getBoard(), lastMovePerformedBy, nextMove, currentPlayer, currentWorker);
                if (setStuck)
                    updateTurnMessage.setStuck(true);
                updateTurnMessageSender.notifyAll(updateTurnMessage);
            }
        }
    }

    /**
     * Update the currentPlayer to move to the next player's turn
     */
    protected void updateToNextPlayerTurn() {
        if (!startupPhase) {
            if (currentPlayer == null) {
                lastMovePerformedBy = null;
                currentPlayer = playerOrder.get(0);
            } else {
                lastMovePerformedBy = currentPlayer.getNickname();
                currentPlayer = getNextPlayer();
            }
        }

        resetCurrentWorker();
        movesPerformed.clear();
        currentMoveIndex = 0;
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
            do {
                PlayerMove moveToRestore = movesPerformed.getLast();
                if (moveToRestore.getMove().getActionType() == Actions.ActionType.MOVEMENT) {
                    //Invert the sequence of starting and target slot to make the reverse move
                    Slot targetSlot = moveToRestore.getStartingSlot();
                    moveToRestore.getMovedWorker().move(targetSlot);
                } else if (moveToRestore.getMove().getActionType() == Actions.ActionType.BUILDING) {
                    moveToRestore.getTargetSlot().destroyTopBuilding();
                }

                if (moveToRestore.getMove() == Actions.BUILD_BEFORE) {
                    turnSequenceMap.get(moveToRestore.getPlayerOwner()).setCanMoveUp(true);
                } else if (moveToRestore.getMove() == Actions.MOVE_DISABLE_OPPONENT_UP) {
                    setOtherPlayerCanMoveUpTo(true);
                }
                movesPerformed.removeLast();
            }
            while (!movesPerformed.isEmpty() && movesPerformed.getLast().getForcedMove());

            if (currentMoveIndex > 0)
                currentMoveIndex = currentMoveIndex - 2;

            updateTurn();
        }
    }

    /**
     * Allows to know if there are moves to undo or not (useful for gui purpose)
     *
     * @return A boolean describing if there are moves to undo or not
     */
    public boolean areThereMovesToUndo() {
        return !movesPerformed.isEmpty();
    }

    /**
     * Check if, at the beginning of a turn, the player's workers have a slot adjacent them available to move on
     *
     * @return Returns an ArrayList of worker who can move in an adjacent slot
     */
    protected int initialCheckMovableWorker() {
        ArrayList<Worker> movableWorkers = new ArrayList<>(2);
        for (Worker worker : getCurrentPlayerWorkers()) {
            boolean canMove = checkIfNotStuck(worker);

            if (canMove)
                movableWorkers.add(worker);
        }

        return movableWorkers.size();
    }

    /**
     * Check if, at the beginning of a turn, the worker has a slot adjacent him available to move on
     *
     * @param worker The worker whose ability to move is to be checked
     * @return Returns a boolean who approves or refuses the possibility to move of the worker
     */
    public boolean checkIfNotStuck(Worker worker) {
        boolean canMove = false;
        Slot workerSlot = worker.getWorkerSlot();
        ArrayList<Slot> slotsToVerify = gameInstance.getBoard().getAdjacentSlots(workerSlot);

        int i = 0;
        while (!canMove && i < slotsToVerify.size()) {
            Slot slotToVerify = slotsToVerify.get(i);
            if (Slot.calculateHeightDifference(workerSlot, slotToVerify) < 2 &&
                    (getCurrentPlayerTurnSequence().isCanMoveUp() || Slot.calculateHeightDifference(workerSlot, slotToVerify) < 1))
                if (!slotToVerify.getBuildingsStatus().contains(Building.BuildingLevel.DOME))
                    if (slotToVerify.getWorkerInSlot() == null ||
                            (!Collections.disjoint(getCurrentPlayerTurnSequence().getMoveSequence(), Arrays.asList(Actions.MOVE_OPPONENT_SLOT_FLIP, Actions.MOVE_OPPONENT_SLOT_PUSH)) &&
                                    !slotToVerify.getWorkerInSlot().getPlayerOwner().getNickname().equals(worker.getPlayerOwner().getNickname())))
                        canMove = true;
            i++;
        }

        return canMove;
    }

    /**
     * Check if, at the beginning of a turn, the worker has a slot adjacent him available to build
     *
     * @param worker The worker whose ability to build is to be checked
     * @return Returns a boolean who approves or refuses the possibility to build of the worker
     */
    public boolean checkIfCanBuild(Worker worker) {
        boolean canBuild = false;
        Slot workerSlot = worker.getWorkerSlot();
        ArrayList<Slot> slotsToVerify = gameInstance.getBoard().getAdjacentSlots(workerSlot);

        int i = 0;
        while (!canBuild && i < slotsToVerify.size()) {
            Slot slotToVerify = slotsToVerify.get(i);
            if (Slot.calculateHeightDifference(workerSlot, slotToVerify) < 2)
                if (slotToVerify.getWorkerInSlot() == null)
                    if (slotToVerify.getConstructionTopLevel() == null ||
                            slotToVerify.getConstructionTopLevel().hasProperty(Building.BuildingProperty.CAN_BUILD_ON_IT))
                        canBuild = true;

            i++;
        }

        return canBuild;
    }

    public void win(Player winner) {
        if (!startupPhase && winner == currentPlayer) {
            gameIsFinish = true;
            UpdateTurnMessage updateTurnMessage = new UpdateTurnMessage(gameInstance.getBoard(), winner.getNickname(), Actions.WIN, currentPlayer, currentWorker);
            updateTurnMessageSender.notifyAll(updateTurnMessage);
        }
    }

    private void lose(Player loser) {
        if (gameInstance.getPlayerNumber() <= 2) {
            gameIsFinish = true;
            UpdateTurnMessage updateTurnMessage = new UpdateTurnMessage(gameInstance.getBoard(), loser.getNickname(), Actions.LOSE, loser, null);
            updateTurnMessage.setGameFinish(true);
            updateTurnMessageSender.notifyAll(updateTurnMessage);
        } else {
            UpdateTurnMessage updateTurnMessage = new UpdateTurnMessage(gameInstance.getBoard(), loser.getNickname(), Actions.LOSE, loser, null);
            updateTurnMessageSender.notifyAll(updateTurnMessage);
            gameInstance.setPlayerNumber(gameInstance.getPlayerNumber() - 1);

            for (Worker workerToDelete : loser.getWorkers()) {
                workerToDelete.move(new Slot(new Position(-1, -1)));
                workerToDelete = null;
            }

            updateToNextPlayerTurn();
            updateTurn();
            playerOrder.removeIf(x -> x.getNickname().equalsIgnoreCase(loser.getNickname()));
        }
    }

    public void quit(Player playerWhoQuit) {
        if (playerWhoQuit.getWorkers().get(0) != null)
            for (Worker workerToDelete : playerWhoQuit.getWorkers()) {
                workerToDelete.move(new Slot(new Position(-1, -1)));
                workerToDelete = null;
            }

        if (!gameIsFinish) {
            //Player quit during the active game
            if (playerOrder.contains(playerWhoQuit)) {
                gameIsFinish = true;
                UpdateTurnMessage updateTurnMessage = new UpdateTurnMessage(gameInstance.getBoard(), playerWhoQuit.getNickname(), Actions.GAME_END, playerWhoQuit, null);
                updateTurnMessage.setGameFinish(true);
                updateTurnMessageSender.notifyAll(updateTurnMessage);
                return;
            } else {
                //Player quit while other player continue to player (he lose)
                UpdateTurnMessage updateTurnMessage = new UpdateTurnMessage(gameInstance.getBoard(), playerWhoQuit.getNickname(), Actions.QUIT, playerWhoQuit, null);
                updateTurnMessageSender.notifyAll(updateTurnMessage);
                if (currentPlayer.getNickname().equalsIgnoreCase(playerWhoQuit.getNickname())) { //Non dovrebbe mai verificarsi
                    updateToNextPlayerTurn();
                    updateTurn();
                }
            }
        } else {
            //Player quit because the game is ended
            UpdateTurnMessage updateTurnMessage = new UpdateTurnMessage(gameInstance.getBoard(), playerWhoQuit.getNickname(), Actions.QUIT, playerWhoQuit, null);
            updateTurnMessage.setGameFinish(true);
            updateTurnMessageSender.notifyAll(updateTurnMessage);
        }

        playerOrder.removeIf(x -> x.getNickname().equalsIgnoreCase(playerWhoQuit.getNickname()));
    }

    public boolean isGameIsFinish() {
        return gameIsFinish;
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
     * Returns the boolean that describes whether the player specified can move up during this turn or not
     *
     * @param player The specified player we want to know if can move up
     * @return A boolean describing the possibilità of move up or not during this turn for the player
     */
    public boolean canPlayerMoveUp(Player player) {
        return turnSequenceMap.get(player).isCanMoveUp();
    }

    /**
     * Set up the Turn Sequence of each player in game during the game set up. Builds up the standard sequence of moves
     * and read the win conditions
     */
    protected void setUpTurnSequence() {
        try {
            File xmlChosenCards = new File("src/main/resources/GodsParameters.xml");
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
            System.out.println(ex.getMessage() + "Exception thrown from Turn.setUpTurnSequence");
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

            LinkedHashMap<Actions, Integer> moveSequence = new LinkedHashMap<>();
            int priority = 0;
            moveSequence.put(Actions.CHOSE_WORKER, 0);
            for (int i = 0; i < list.getLength(); i++) {
                Node moveNode = list.item(i);
                if (moveNode.getNodeType() != Node.ELEMENT_NODE && moveNode.getNextSibling() != null) {
                    Node sibling = moveNode.getNextSibling();
                    priority = Integer.parseInt(sibling.getAttributes().getNamedItem("priority").getTextContent());
                    moveSequence.put(Actions.valueOf(sibling.getTextContent()), priority);
                }
            }
            moveSequence.put(Actions.WAIT_FOR_UNDO, ++priority);
            List<Map.Entry<Actions, Integer>> listToSort = new LinkedList<>(moveSequence.entrySet());
            listToSort.sort(Map.Entry.comparingByValue());
            moveSequence.clear();
            for (Map.Entry<Actions, Integer> entry : listToSort)
                moveSequence.put(entry.getKey(), entry.getValue());

            return new ArrayList<>(moveSequence.keySet());
        } catch (Exception ex) {
            System.out.println(ex.getMessage() + "Exception thrown from Turn.loadMoveSequence");
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
            System.out.println(ex.getMessage() + "Exception thrown from Turn.loadWinCondition");
            return null;
        }
    }
}
