package com.app.risk.model;

import java.io.Serializable;

/**
 * Continent model to save details of each continent
 *
 * @author Akshita Angara
 * @version 1.0.0
 */
public class Continent implements Serializable {

    private String nameOfContinent;
    private int armyControlValue;

    /**
     * This is a default constructor.
     */
    public Continent() {
        super();
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
     * @param obj The object of continent.
     * @return 0 if two continents are equal; 1 if it is greater; -1 if it is smaller.
     */
    public int compareTo(Continent new_continent) {
        if (this.nameOfContinent.equals(new_continent.nameOfContinent))
            return 0;
        return this.nameOfContinent.compareToIgnoreCase(new_continent.nameOfContinent);
    }
}
