package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.SocketConnection;
import it.polimi.ingsw.view.RemoteView;
import org.junit.Test;
import org.mockito.Mockito;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import static org.junit.Assert.*;

public class GameInitializationManagerTest {

    Game gameInstance = new Game();
    GameInitializationManager gameInitializationManager = new GameInitializationManager(gameInstance);


    @Test
    public void buildChosenCard() throws ParseException, IllegalAccessException {
        Turn turn = gameInstance.getTurn();

        Player player1 = new Player("player1");
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = dateFormat.parse("23/5/1998");
        player1.setBirthday(date);
        Player player2 = new Player("player2");
        date = dateFormat.parse("23/5/1996");
        player2.setBirthday(date);
        gameInstance.setPlayerNumber(2);
        gameInstance.addPlayer(player1);
        gameInstance.addPlayer(player2);

        SocketConnection socketConnection = Mockito.mock(SocketConnection.class);
        RemoteView remoteView = new RemoteView(player1.getNickname(), socketConnection);

        turn.addUpdateTurnMessageObserver(remoteView.getUpdateTurnMessageReceiver());

        Deck deck = gameInstance.getDeck();

        PlayerMoveStartup playerMoveStartup = new PlayerMoveStartup(
                TurnEvents.StartupActions.CHOOSE_CARD_REQUEST
        );
        playerMoveStartup.setChosenCard("apollo");
        playerMoveStartup.setPlayerOwnerNickname(turn.getCurrentPlayer().getNickname());
        playerMoveStartup.setRemoteView(remoteView);
        gameInitializationManager.handlePlayerMoveStartup(playerMoveStartup);

        playerMoveStartup = new PlayerMoveStartup(
                TurnEvents.StartupActions.CHOOSE_CARD_REQUEST
        );
        playerMoveStartup.setChosenCard("artemis");
        playerMoveStartup.setPlayerOwnerNickname(turn.getCurrentPlayer().getNickname());
        playerMoveStartup.setRemoteView(remoteView);
        gameInitializationManager.handlePlayerMoveStartup(playerMoveStartup);

        ArrayList<GodsCard> chosenCards = deck.getChosenCardsCopy();
        ArrayList<String> chosenCardsExpected = new ArrayList<>(Arrays.asList("apollo", "artemis"));

        for (GodsCard godCard : chosenCards) {
            assertTrue(chosenCardsExpected.contains(godCard.getCardName()));
            chosenCardsExpected.remove(godCard.getCardName());
        }
    }

    @Test
    public void pickUpCard() throws ParseException, IllegalAccessException {
        Turn turn = gameInstance.getTurn();

        Player player1 = new Player("player1");
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = dateFormat.parse("23/5/1998");
        player1.setBirthday(date);
        Player player2 = new Player("player2");
        date = dateFormat.parse("23/5/1996");
        player2.setBirthday(date);
        gameInstance.setPlayerNumber(2);
        gameInstance.addPlayer(player1);
        gameInstance.addPlayer(player2);

        SocketConnection socketConnection = Mockito.mock(SocketConnection.class);
        RemoteView remoteView = new RemoteView(player1.getNickname(), socketConnection);

        turn.addUpdateTurnMessageObserver(remoteView.getUpdateTurnMessageReceiver());

        Deck deck = gameInstance.getDeck();
        deck.chooseCards("apollo", "artemis");

        PlayerMoveStartup playerMoveStartup = new PlayerMoveStartup(
                TurnEvents.StartupActions.PICK_UP_CARD_REQUEST
        );
        playerMoveStartup.setChosenCard("apollo");
        playerMoveStartup.setPlayerOwnerNickname(turn.getCurrentPlayer().getNickname());
        playerMoveStartup.setRemoteView(remoteView);
        gameInitializationManager.handlePlayerMoveStartup(playerMoveStartup);

        assertEquals("apollo", gameInstance.getPlayerByName(playerMoveStartup.getPlayerOwnerNickname()).getPlayerCard().getCardName());
    }

    @Test
    public void setPlayerColor() throws ParseException, IllegalAccessException {
        Turn turn = gameInstance.getTurn();

        Player player1 = new Player("player1");
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = dateFormat.parse("23/5/1998");
        player1.setBirthday(date);
        Player player2 = new Player("player2");
        date = dateFormat.parse("23/5/1996");
        player2.setBirthday(date);
        gameInstance.setPlayerNumber(2);
        gameInstance.addPlayer(player1);
        gameInstance.addPlayer(player2);

        SocketConnection socketConnection = Mockito.mock(SocketConnection.class);
        RemoteView remoteView = new RemoteView(player1.getNickname(), socketConnection);

        turn.addUpdateTurnMessageObserver(remoteView.getUpdateTurnMessageReceiver());

        PlayerMoveStartup playerMoveStartup = new PlayerMoveStartup(
                TurnEvents.StartupActions.COLOR_REQUEST
        );
        playerMoveStartup.setChosenColor(Color.PlayerColor.RED);
        playerMoveStartup.setPlayerOwnerNickname(turn.getCurrentPlayer().getNickname());
        playerMoveStartup.setRemoteView(remoteView);
        gameInitializationManager.handlePlayerMoveStartup(playerMoveStartup);

        assertEquals(Color.PlayerColor.RED, gameInstance.getPlayerByName(playerMoveStartup.getPlayerOwnerNickname()).getPlayerColor());
    }

    @Test
    public void placeWorkerTest() throws ParseException, IllegalAccessException {
        Turn turn = gameInstance.getTurn();

        Player player1 = new Player("player1");
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = dateFormat.parse("23/5/1998");
        player1.setBirthday(date);
        Player player2 = new Player("player2");
        date = dateFormat.parse("23/5/1996");
        player2.setBirthday(date);
        gameInstance.setPlayerNumber(2);
        gameInstance.addPlayer(player1);
        gameInstance.addPlayer(player2);

        SocketConnection socketConnection = Mockito.mock(SocketConnection.class);
        RemoteView remoteView = new RemoteView(player1.getNickname(), socketConnection);

        turn.addUpdateTurnMessageObserver(remoteView.getUpdateTurnMessageReceiver());

        PlayerMoveStartup playerMoveStartup = new PlayerMoveStartup(
                TurnEvents.StartupActions.PLACE_WORKER_1
        );
        playerMoveStartup.setWorkerPosition(new Position(0, 0));
        playerMoveStartup.setPlayerOwnerNickname(turn.getCurrentPlayer().getNickname());
        playerMoveStartup.setRemoteView(remoteView);
        gameInitializationManager.handlePlayerMoveStartup(playerMoveStartup);

        assertEquals(new Position(0, 0), gameInstance.getPlayerByName(playerMoveStartup.getPlayerOwnerNickname()).getWorkers().get(0).getWorkerPosition());
    }
}