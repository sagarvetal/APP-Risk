package com.app.risk.model;

import java.io.Serializable;
import java.util.List;

/**
 * Card model to store card details like card type
 *
 * @author Sagar Vetal
 * @version 1.0.0 (Date: 04/10/2018)
 */
public class Card implements Serializable {

    private String type;

    /**
     * This is default constructor.
     */
    public Card() {
    }

    /**
     * This parameterized constructor initialize the card type.
     *
     * @param type It is type of card.
     */
    public Card(String type) {
        this.type = type;
    }

    /**
     * Getter function to return the card type
     *
     * @return card type
     */
    public String getType() {
        return type;
    }

    /**
     * Setter function to set the unique id of the player
     *
     * @param type The type of card
     */
    public void setType(String type) {
        this.type = type;
    }

    public boolean cardsExchangeable(List<Card> cardsToExchange){

        if(cardsToExchange.size() == 3){
            if(cardsToExchange.get(0).type.equals(cardsToExchange.get(1).type) &&
                    cardsToExchange.get(0).type.equals(cardsToExchange.get(2).type)){
                return true;
            } else if(!cardsToExchange.get(0).type.equals(cardsToExchange.get(1).type) &&
                    !cardsToExchange.get(0).type.equals(cardsToExchange.get(2).type) &&
                    !cardsToExchange.get(1).type.equals(cardsToExchange.get(2).type)){
                return true;
            } else {
                System.out.println("Card similarity rule not satisfied.");
                return false;
            }
        } else {
            System.out.println("Didn't choose enough cards");
            return false;
        }
    }
}
