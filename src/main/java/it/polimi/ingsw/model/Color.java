package it.polimi.ingsw.model;

public enum Color {

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

        public String getColorAsString(PlayerColor color){
            if(color.getEscape().equals(RED.getEscape()))
                return "RED";
            else if (color.getEscape().equals(CYAN.getEscape()))
                return "CYAN";
            else if(color.getEscape().equals(YELLOW.getEscape()))
                return "YELLOW";
            else
                throw new IllegalArgumentException();
        }
    }



    public static final String RESET = "\u001B[0m";
    public String escape;

    Color(String escape)
    {
        this.escape = escape;
    }

    public String getEscape() {
        return escape;
    }

    @Override
    public String toString()
    {
        return escape;
    }
}
