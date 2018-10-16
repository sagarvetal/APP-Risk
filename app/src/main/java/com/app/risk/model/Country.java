package com.app.risk.model;

import android.support.annotation.NonNull;

/**
 * Country model to save details of each country
 *
 * @author Akshita Angara
 */
public class Country implements Comparable<Country> {

    String nameOfCountry;
    Continent belongsToContinent;

    Country() {
        super();
    }

    public Country(String new_nameOfCountry) {
        nameOfCountry = new_nameOfCountry;
    }

    public Country(String new_nameOfCountry, Continent new_belongsToContinent) {
        nameOfCountry = new_nameOfCountry;
        belongsToContinent = new_belongsToContinent;
    }

    /**
     * Getter function to get name of country
     *
     * @return name of country
     */
    public String getNameOfCountry() {
        return nameOfCountry;
    }

    /**
     * Setter function to set name of country
     *
     * @param nameOfCountry
     */
    public void setNameOfCountry(String nameOfCountry) {
        this.nameOfCountry = nameOfCountry;
    }

    /**
     * Getter function to return the continent that the country belongs to
     *
     * @return object of Continent that the country belongs to
     */
    public Continent getBelongsToContinent() {
        return belongsToContinent;
    }

    /**
     * Setter function to set details of the continent to which the country belongs
     *
     * @param belongsToContinent
     */
    public void setBelongsToContinent(Continent belongsToContinent) {
        this.belongsToContinent = belongsToContinent;
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
