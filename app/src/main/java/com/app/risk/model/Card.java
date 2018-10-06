package com.app.risk.model;

import java.io.Serializable;

/**
 * Card model to store card details like card type
 * @author Sagar Vetal
 * @date 04/10/2018
 * @version 1.0.0
 */
public class Card implements Serializable {

    private String type;

    /**
     * Getter function to return the card type
     * @return card type
     */
    public String getType() {
        return type;
    }

    /**
     * Setter function to set the unique id of the player
     * @param type The type of card
     */
    public void setType(String type) {
        this.type = type;
    }
}
