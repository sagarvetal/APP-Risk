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
    private boolean isSelected;
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

    /**
     * Getter method to return if the card is selected or not
     * @return check if card is selected
     */
    public boolean isSelected() {
        return isSelected;
    }

    /**
     * Setter method to set the card as selected or not
     * @param selected selected value of card
     */
    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
