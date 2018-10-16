package com.app.risk.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Country model to save details of each country
 * @author Akshita Angara
 */
public class Country {

    String nameOfCountry;
    Continent belongsToContinent;
    private List<String> adjacentCountries;
    private Player player;
    private int noOfArmies;

    /**
     * This is a default constructor.
     * It initializes the neighbouring country list.
     */
    public Country() {
        adjacentCountries = new ArrayList<>();
    }

    /**
     * Getter function to get name of country
     * @return name of country
     */
    public String getNameOfCountry() {
        return nameOfCountry;
    }

    /**
     * Setter function to set name of country
     * @param nameOfCountry
     */
    public void setNameOfCountry(String nameOfCountry) {
        this.nameOfCountry = nameOfCountry;
    }

    /**
     * Getter function to return the continent that the country belongs to
     * @return object of Continent that the country belongs to
     */
    public Continent getBelongsToContinent() {
        return belongsToContinent;
    }

    /**
     * Setter function to set details of the continent to which the country belongs
     * @param belongsToContinent
     */
    public void setBelongsToContinent(Continent belongsToContinent) {
        this.belongsToContinent = belongsToContinent;
    }

    /**
     * Getter function to return the list of adjacent countries.
     * @return list of adjacent countries
     */
    public List<String> getAdjacentCountries() {
        return adjacentCountries;
    }

    /**
     * Setter function to set the list of adjacent countries.
     * @param adjacentCountries The list of adjacent countries.
     */
    public void setAdjacentCountries(List<String> adjacentCountries) {
        this.adjacentCountries = adjacentCountries;
    }

    /**
     * Getter function to return the player object who owns this country.
     * @return player object who owns this country.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Setter function to set the player object who owns this country.
     * @param player The player who owns this country.
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * Getter function to return the no of armies placed on this country.
     * @return the no of armies placed on this country.
     */
    public int getNoOfArmies() {
        return noOfArmies;
    }

    /**
     * Setter function to set the no of armies to be placed on this country.
     * @param noOfArmies The no of armies to be placed on this country.
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

}
