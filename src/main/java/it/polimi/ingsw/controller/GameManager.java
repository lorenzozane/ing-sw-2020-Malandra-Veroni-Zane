package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PlayerMove;
import it.polimi.ingsw.model.Turn;
import it.polimi.ingsw.model.TurnEvents.Actions.*;
import it.polimi.ingsw.observer.Observer;

public class GameManager implements Observer<PlayerMove> {

    private final Game game;
    private Turn turn;

    public GameManager() {
        this.game = Game.getInstance();
        turn = this.game.getTurn();
    }

    protected synchronized void HandleMove(PlayerMove move) {
        if (!turn.isPlayerTurn(move.getPlayer())) {
            //WrongTurnMessage
            return;
        }

        //TODO: Logica gestione mosse
    }

    protected void initialCheck() {

    }

    protected void performMove() {

    }

    protected void performBuilding() {

    }

    protected void checkWinConditions() {

    }

    protected void checkTurnIsOver() {

    }

    @Override
    public void update(PlayerMove message) {
        HandleMove(message);
    }
}
