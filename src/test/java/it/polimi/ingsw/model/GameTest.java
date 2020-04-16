package it.polimi.ingsw.model;

import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.*;

public class GameTest {

    @Test
    public void addPlayer() throws ParseException {
        Game game = Game.getInstance();
        Player p1 = new Player("test1");
        Player p2 = new Player("test2");
        DateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy");
        Date date = dateFormat.parse("23/5/1998");
        p1.setBirthday(date);
        Date date2 = dateFormat.parse("23/5/2000");
        p2.setBirthday(date2);

        game.addPlayer(p1);
        game.addPlayer(p2);

        System.out.println("ok");





    }
}