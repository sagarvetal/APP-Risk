package com.app.risk.model;

import java.util.List;

/**
 * GameMap class to store each node (country) of the game map graph
 * @author Akshita Angara
 */
public class GameMap {

    private Country fromCountry;
    private int coordinateX = 0;
    private int coordinateY = 0;
    private List<Country> connectedToCountries;

    /**
     * Getter function to return the from country
     * @return from country
     */
    public Country getFromCountry() {
        return fromCountry;
    }

    /**
     * Setter function to set the from country
     * @param fromCountry
     */
    public void setFromCountry(Country fromCountry) {
        this.fromCountry = fromCountry;
    }

    /**
     * Getter function to return list of all the countries that the from country is connected to (adjacency list)
     * @return list of all connected countries
     */
    public List<Country> getConnectedToCountries() {
        return connectedToCountries;
    }

    /**
     * Return list of all connected countries as a comma separated string
     * @return comma separated string of all connected countries
     */
    public String getConnectedCountriesAsString() {
        String returnString = "";
        for(Country country: connectedToCountries) {
            returnString += country.getNameOfCountry() + ",";
        }
        return returnString;
    }

    /**
     * Setter function to set the list of all countries that the from country is connected to (adjacency list)
     * @param connectedToCountries
     */
    public void setConnectedToCountries(List<Country> connectedToCountries) {
        this.connectedToCountries = connectedToCountries;
    }

    /**
     * Getter function to return the x-coordinate of the position of the from country
     * @return x-coordinate
     */
    public int getCoordinateX() {
        return coordinateX;
    }

    /**
     * Setter function to set the x-coordinate of the position of the from country
     * @param coordinateX
     */
    public void setCoordinateX(int coordinateX) {
        this.coordinateX = coordinateX;
    }

    /**
     * Getter function to return the y-coordinate of the position of the from country
     * @return y-coordinate
     */
    public int getCoordinateY() {
        return coordinateY;
    }

    /**
     * Setter function to set the y-coordinate of the position of the from country
     * @param coordinateY
     */
    public void setCoordinateY(int coordinateY) {
        this.coordinateY = coordinateY;
    }
}
