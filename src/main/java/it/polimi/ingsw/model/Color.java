package it.polimi.ingsw.model;

import java.io.Serializable;

/**
 * Color useful to the game experience.
 */
public enum Color implements Serializable {

    ANSI_GREEN("\u001B[32m"),
    ANSI_BLUE("\u001B[34m"),
    ANSI_PURPLE("\u001B[35m"),
    ANSI_BRIGHT_BLUE("\u001b[34;1m");

    public enum PlayerColor {
        YELLOW("\u001B[33m"),
        RED("\u001B[31m"),
        CYAN("\u001b[36;1m");

        public String escape;

        PlayerColor(String escape) {
            this.escape = escape;
        }

        public String getEscape() {
            return escape;
        }

        public String getColorAsString() {
            if (this.getEscape().equals(RED.getEscape()))
                return "RED";
            else if (this.getEscape().equals(CYAN.getEscape()))
                return "CYAN";
            else if (this.getEscape().equals(YELLOW.getEscape()))
                return "YELLOW";
            else
                throw new IllegalArgumentException();
        }
    }


    public static final String RESET = "\u001B[0m";
    public String escape;

    Color(String escape) {
        this.escape = escape;
    }

    public String getEscape() {
        return escape;
    }

    @Override
    public String toString() {
        return escape;
    }
}
