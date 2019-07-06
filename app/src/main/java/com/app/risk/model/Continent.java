package com.app.risk.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Continent model to save details of each continent
 *
 * @author Akshita Angara
 * @version 1.0.0
 */
public class Continent implements Serializable {

    /**
     * Defines the name of continent
     */
    private String nameOfContinent;
    /**
     * stores the control value for given continent
     */
    private int armyControlValue;
    /**
     * Stores the list of coutries belonging to continent
     */
    private ArrayList<Country> countries;

    /**
     * This is a default constructor.
     */
    public Continent() {
        super();
        countries = new ArrayList<>();
    }

    /**
     * This is a parameterized constructor.
     * It initializes the name of continent and army control value.
     *
     * @param new_nameOfContinent  Name of the continent
     * @param new_armyControlValue Army control value
     */
    public Continent(String new_nameOfContinent, int new_armyControlValue) {
        nameOfContinent = new_nameOfContinent;
        armyControlValue = new_armyControlValue;
        countries = new ArrayList<>();
    }

    /**
     * This is a parameterized constructor.
     * It initializes the name of continent.
     *
     * @param new_nameOfContinent Name of the continent
     */
    public Continent(String new_nameOfContinent) {

        nameOfContinent = new_nameOfContinent;
    }

    /**
     * Getter function to return the name of continent
     *
     * @return name of continent
     */
    public String getNameOfContinent() {
        return nameOfContinent;
    }

    /**
     * Setter function to set the name of continent
     *
     * @param nameOfContinent Name of the continent
     */
    public void setNameOfContinent(String nameOfContinent) {
        this.nameOfContinent = nameOfContinent;
    }

    /**
     * Getter function to return control value of continent
     * Control value - number of armies which will be allocated once the player acquires the whole continent
     *
     * @return control value
     */
    public int getArmyControlValue() {
        return armyControlValue;
    }

    /**
     * Setter function to set the control value of continent
     * Control value - number of armies which will be allocated once the player acquires the whole continent
     *
     * @param armyControlValue Army control value
     */
    public void setArmyControlValue(int armyControlValue) {
        this.armyControlValue = armyControlValue;
    }

    /**
     * This method returns the hashcode of continent.
     * @return The hashcode of continent.
     */
    public int hashCode() {
        return nameOfContinent.hashCode();
    }

    /**
     * This is overridden method to check two continents are equal .
     * @param obj The object of continent.
     * @return true if two continents are equal; otherwise return false.
     */
    public boolean equals(Object obj) {
        if (obj instanceof Continent) {
            if (this.nameOfContinent.equalsIgnoreCase(((Continent) obj).nameOfContinent)) {
                return true;
            }
        }
        return false;
    }

    /**
     * This is overridden method to compare two continents are equal .
     * @param new_continent The object of continent.
     * @return 0 if two continents are equal; 1 if it is greater; -1 if it is smaller.
     */
    public int compareTo(Continent new_continent) {
        if (this.nameOfContinent.equals(new_continent.nameOfContinent))
            return 0;
        return this.nameOfContinent.compareToIgnoreCase(new_continent.nameOfContinent);
    }

    /**
     * Getter function to return list of countries belongs to this continents
     * @return List of countries belongs to this continents
     */
    public ArrayList<Country> getCountries() {
        return countries;
    }

    /**
     * Setter function to add country into list belonging countries
     * @param country The country belongs to this continents
     */
    public void setCountries(Country country) {
        this.countries.add(country);
    }


}
