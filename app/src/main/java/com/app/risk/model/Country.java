package com.app.risk.model;

/**
 * Country model to save details of each country
 * @author Akshita Angara
 * @version 1.0.0
 */
public class Country {

    private String nameOfCountry;
    private Continent belongsToContinent;

    /**
     * Default constructor
     */
    public Country() {
    }

    /**
     * Constructor to create a new object with the given name and its continent.
     * @param nameOfCountry Name of the country
     * @param belongsToContinent Continent object to which the country belongs
     */
    public Country(String nameOfCountry, Continent belongsToContinent) {
        this.nameOfCountry = nameOfCountry;
        this.belongsToContinent = belongsToContinent;
    }

    /**
     * Constructor to create a new object with a given name.
     * @param nameOfCountry Name of the country
     */
    public Country(String nameOfCountry) {
        this.nameOfCountry = nameOfCountry;
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
     * @param nameOfCountry Name of the country
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
     * @param belongsToContinent Continent object to which the country belongs
     */
    public void setBelongsToContinent(Continent belongsToContinent) {
        this.belongsToContinent = belongsToContinent;
    }
}
