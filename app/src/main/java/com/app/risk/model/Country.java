package com.app.risk.model;

import java.io.Serializable;

/**
 * Country model to save details of each country
 * @author Akshita Angara
 */
public class Country implements Serializable {

    String nameOfCountry;
    Continent belongsToContinent;

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
}
