package com.app.risk.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Player model to capture player details
 * like name, assigned countries and armies, and cards earned
 * @author Sagar Vetal
 * @version 1.0.0 (Date: 04/10/2018)
 */
public class Player implements Serializable {

    private int id;
    private String name;
    private String colorCode;
    private int noOfCountries;
    private int noOfArmies;
    private int reinforcementArmies;
    private List<Card> cards;
    private boolean isActive;

    /**
     * This is a default constructor and it initializes the card list.
     */
    public Player() {
        this.cards = new ArrayList<>();
    }

    /**
     * Getter function to return the unique id of the player
     * @return id of player
     */
    public int getId() {
        return id;
    }

    /**
     * Setter function to set the unique id of the player
     * @param id The unique id of player
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Getter function to return the name of the player
     * @return name of the player
     */
    public String getName() {
        return name;
    }

    /**
     * Setter function to set the name of the player
     * @param name The name of the player
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter function to return the color code of the player
     * @return color code of the player
     */
    public String getColorCode() {
        return colorCode;
    }

    /**
     * Setter function to set the color code of the player to identify them
     * @param colorCode The unique color code of the player
     */
    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    /**
     * Getter function to return the no of countries assigned to the player
     * @return no of countries
     */
    public int getNoOfCountries() {
        return noOfCountries;
    }

    /**
     * Setter function to set the no of countries assigned to the player
     * @param noOfCountries The number of countries assigned
     */
    public void setNoOfCountries(int noOfCountries) {
        this.noOfCountries = noOfCountries;
    }

    /**
     * This function is to increment no of countries by given count.
     * @param count The increment count by which the no of countries to be incremented.
     */
    public void incrementCountries(final int count) {
        this.noOfCountries += count;
    }

    /**
     * Getter function to return the no of armies assigned to the player
     * @return no of armies
     */
    public int getNoOfArmies() {
        return noOfArmies;
    }

    /**
     * Setter function to set the no of armies assigned to the player
     * @param noOfArmies The number of armies assigned
     */
    public void setNoOfArmies(int noOfArmies) {
        this.noOfArmies = noOfArmies;
    }

    /**
     * This function is to increment no of armies by given count.
     * @param count The increment count by which the no of armies to be incremented.
     */
    public void incrementArmies(final int count) {
        this.noOfArmies += count;
    }

    /**
     * Getter function to return the no of reinforcement armies given to the player
     * @return no of reinforcement armies
     */
    public int getReinforcementArmies() {
        return reinforcementArmies;
    }

    /**
     * Setter function to set the no of reinforcement armies given to the player
     * @param reinforcementArmies The number of reinforcement armies
     */
    public void setReinforcementArmies(int reinforcementArmies) {
        this.reinforcementArmies = reinforcementArmies;
    }

    /**
     * This function is to decrement no of reinforcement armies by given count.
     * @param count The decrement count by which the no of reinforcement armies to be decremented.
     */
    public void decrementReinforcementArmies(final int count) {
        this.reinforcementArmies -= count;
    }

    /**
     * Getter function to return the list of cards earned by player
     * @return list of cards
     */
    public List<Card> getCards() {
        return cards;
    }

    /**
     * Setter function to set the card earned by player
     * @param card The card earned by player
     */
    public void setCards(Card card) {
        this.cards.add(card);
    }

    /**
     * Getter function to get the flag to determine game is over on not for respective player
     * @return true if player's game is not over; otherwise return false.
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Setter function to set the flag to determine game is over on not for respective player
     * @param active It is true if player's game is not over; otherwise false.
     */
    public void setActive(boolean active) {
        isActive = active;
    }

}
