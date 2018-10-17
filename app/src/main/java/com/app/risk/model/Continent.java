package com.app.risk.model;

import java.io.Serializable;

/**
 * Continent model to save details of each continent
 * @author Akshita Angara
 */
public class Continent implements Serializable {

    String nameOfContinent;
    int armyControlValue;
    Continent()
    {
        super();
    }
    public Continent(String new_nameOfContinent,int new_armyControlValue)
    {
        nameOfContinent=new_nameOfContinent;
        armyControlValue=new_armyControlValue;
    }
    public Continent(String new_nameOfContinent)
    {
        nameOfContinent=new_nameOfContinent;
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
    public int hashCode(){

        return nameOfContinent.hashCode();
    }

    public boolean equals(Object obj) {
        if (obj instanceof Continent) {
            if (this.nameOfContinent.equalsIgnoreCase(((Continent) obj).nameOfContinent))
            {
                return true;
            }
        }
        return false;
    }

    public int compareTo(Continent new_continent) {

        if (this.nameOfContinent.equals(new_continent.nameOfContinent))
            return 0;
        return this.nameOfContinent.compareToIgnoreCase(new_continent.nameOfContinent);
    }
}
