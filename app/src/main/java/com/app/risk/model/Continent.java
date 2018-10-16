package com.app.risk.model;

import android.graphics.Color;

/**
 * Continent model to save details of each continent
 * @author Akshita Angara
 */
public class Continent {

    String nameOfContinent;
    int armyControlValue;

    /**
     * Getter function to return the name of continent
     * @return name of continent
     */
    public String getNameOfContinent() {
        return nameOfContinent;
    }

    /**
     * Setter function to set the name of continent
     * @param nameOfContinent
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
     * @param armyControlValue
     */
    public void setArmyControlValue(int armyControlValue) {
        this.armyControlValue = armyControlValue;
    }
}
