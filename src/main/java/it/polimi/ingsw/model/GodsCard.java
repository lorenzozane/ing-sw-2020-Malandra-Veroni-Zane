package it.polimi.ingsw.model;

public class GodsCard {

    private final String cardName;
    private String cardDescription;
    private String cardPowerDescription;

    public GodsCard(String cardName){
        this.cardName = cardName;
    }

    public String getCardName() {
        return cardName;
    }

    public String getCardDescription() {
        return cardDescription;
    }

    public void setCardDescription(String cardDescription) {
        this.cardDescription = cardDescription;
    }

    public String getCardPowerDescription() {
        return cardPowerDescription;
    }

    public void setCardPowerDescription(String cardPowerDescription) {
        this.cardPowerDescription = cardPowerDescription;
    }

    //TODO: Implementare getter e setter
}
