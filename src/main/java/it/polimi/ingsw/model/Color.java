package it.polimi.ingsw.model;

public enum Color {

    ANSI_GREEN("\u001B[32m"),
    ANSI_YELLOW("\u001B[33m"),
    ANSI_RED("\u001B[31m"),
    ANSI_BLUE("\u001B[34m"),
    ANSI_PURPLE("\u001B[35m"),
    ANSI_BRIGHT_BLUE("\u001b[34;1m"),
    ANSI_BRIGHT_CYAN("\u001b[36;1m");



    static final String RESET = "\u001B[0m";
    public String escape;

    Color(String escape)
    {
        this.escape = escape;
    }

    public String getEscape() {
        return escape;
    }

    public String getColorAsString(Color color){
        if(color.getEscape().equals(ANSI_RED.getEscape()))
            return "RED";
        else if (color.getEscape().equals(ANSI_BRIGHT_CYAN.getEscape()))
            return "CYAN";
        else if(color.getEscape().equals(ANSI_YELLOW.getEscape()))
            return "YELLOW";
        else
            throw new IllegalArgumentException();
    }

    @Override
    public String toString()
    {
        return escape;
    }
}
