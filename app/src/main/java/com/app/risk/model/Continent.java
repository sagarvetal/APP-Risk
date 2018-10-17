package com.app.risk.model;

import android.graphics.Color;

/**
 * Continent model to save details of each continent
 * @author Akshita Angara
 * @version 1.0.0
 */
public class Continent {

    private String nameOfContinent;
    private int armyControlValue;

    /**
     * Constructor to create a new object with the given name and army control value
     * @param nameOfContinent Name of the continent
     * @param armyControlValue Army control value given when a country is acquired
     */
    public Continent(String nameOfContinent, int armyControlValue) {
        this.nameOfContinent = nameOfContinent;
        this.armyControlValue = armyControlValue;
    }

    /**
     * Getter function to return the name of continent
     * @return name of continent
     */
    public String getNameOfContinent() {
        return nameOfContinent;
    }

    /**
     * Setter function to set the name of continent
     * @param nameOfContinent Name of the continent
     */
    public void setNameOfContinent(String nameOfContinent) {
        this.nameOfContinent = nameOfContinent;
    }

    /**
     * Getter function to return control value of continent
     * Control value - number of armies which will be allocated once the player acquires the whole continent
     * @return control value
     */
    public int getArmyControlValue() {
        return armyControlValue;
    }

    /**
     * Setter function to set the control value of continent
     * Control value - number of armies which will be allocated once the player acquires the whole continent
     * @param armyControlValue Army control value given when a country is acquired
     */
    public void setArmyControlValue(int armyControlValue) {
        this.armyControlValue = armyControlValue;
    }
}
