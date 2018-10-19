package com.app.risk.model;

import java.util.ArrayList;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Country model to save details of each country
 * @author Akshita Angara
 * @version 1.0.0
 */

public class Country implements Serializable, Comparable<Country> {

    private String nameOfCountry;
    private Continent belongsToContinent;
    private ArrayList<String> adjacentCountries;
    private Player player;
    private int noOfArmies;

    /**
     * This is a default constructor.
     * It initializes the adjacent country list.
     */
    public Country() {
        super();
        adjacentCountries = new ArrayList<>();
    }

    /**
     * This is a parameterized constructor.
     * It initializes the name of country.
     * @param new_nameOfCountry Name of country
     */
    public Country(String new_nameOfCountry) {
        nameOfCountry = new_nameOfCountry;
    }

    /**
     * This is a parameterized constructor.
     * It initializes the name of country and continent it belongs to.
     * @param new_nameOfCountry Name of country
     * @param new_belongsToContinent Continent that the country belongs to
     */
    public Country(String new_nameOfCountry, Continent new_belongsToContinent) {
        nameOfCountry = new_nameOfCountry;
        belongsToContinent = new_belongsToContinent;
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
     * @param nameOfCountry Name of country
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
     * @param belongsToContinent Continent that the country belongs to
     */
    public void setBelongsToContinent(Continent belongsToContinent) {
        this.belongsToContinent = belongsToContinent;
    }

    /**
     * Getter function to return the list of adjacent countries.
     * @return list of adjacent countries
     */
    public ArrayList<String> getAdjacentCountries() {
        return adjacentCountries;
    }

    /**
     * Setter function to set the list of adjacent countries.
     * @param adjacentCountries The list of adjacent countries.
     */
    public void setAdjacentCountries(ArrayList<String> adjacentCountries) {
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

    public int hashCode() {

        return nameOfCountry.hashCode();
    }

    public boolean equals(Object obj) {
        if (obj instanceof Country) {
            if (this.nameOfCountry.equalsIgnoreCase(((Country) obj).nameOfCountry)) ;
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public int compareTo(@NonNull Country country) {
        if (this.nameOfCountry.equals(country.nameOfCountry))
            return 0;
        return this.nameOfCountry.compareToIgnoreCase(country.nameOfCountry);
    }
}
