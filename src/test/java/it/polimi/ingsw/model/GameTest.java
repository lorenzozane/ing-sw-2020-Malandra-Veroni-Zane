package it.polimi.ingsw.model;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.*;

public class GameTest {

    @Before
    public void setUpBeforeMethod(){
        Game gameInstance = new Game();
        gameInstance.getPlayerList().clear();
    }

    @Test
    public void addPlayer() throws ParseException, IllegalAccessException {
        Game gameInstance = new Game();
        Player p1 = new Player("test1");
        Player p2 = new Player("test2");
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = dateFormat.parse("23/5/1998");
        p1.setBirthday(date);
        Date date2 = dateFormat.parse("23/5/2000");
        p2.setBirthday(date2);
        gameInstance.setPlayerNumber(2);

        gameInstance.addPlayer(p1);
        gameInstance.addPlayer(p2);
    }

    @Test
    public void challenge() throws ParseException, IOException, IllegalAccessException {
        Game gameInstance = new Game();
        Player p1 = new Player("test1");
        Player p2 = new Player("test2");
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = dateFormat.parse("23/5/1998");
        p1.setBirthday(date);
        Date date2 = dateFormat.parse("23/5/2000");
        p2.setBirthday(date2);
        gameInstance.setPlayerNumber(2);

        gameInstance.addPlayer(p1);
        gameInstance.addPlayer(p2);

        gameInstance.challenge();
        //da completare

    }



}

