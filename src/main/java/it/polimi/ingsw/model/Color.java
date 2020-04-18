package it.polimi.ingsw.model;

public enum Color {

    ANSI_GREEN("\u001B[32m"),
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
    public String getEscape()
    {
        return escape;

    }
    @Override
    public String toString()
    {
        return escape;
    }
}
