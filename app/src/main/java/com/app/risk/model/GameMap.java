package com.app.risk.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * GameMap class to store each node (country) of the game map graph
 *
 * @author Akshita Angara
 * @version 1.0.0
 */
public class GameMap implements Serializable {

    /**
     * Name of the source country
     */
    private Country fromCountry;
    /**
     * Its position on the view (x-coordinate)
     */
    private float coordinateX = 0;
    /**
     * Its position on the view (y-coordinate)
     */
    private float coordinateY = 0;
    /**
     * List of all the countries (their respective GameMap objects) that the source (this) country is connected to
     */
    private ArrayList<GameMap> connectedToCountries = new ArrayList<>();
    /**
     * Index of the country in the list
     */
    private int indexOfCountryInList;
    /**
     * Color of the continent this country belongs to
     */
    private int continentColor;

    /**
     * Default constructor
     */
    public GameMap() {
    }

    /**
     * Constructor to create a GameMap object with the given country, its coordinates and its list of connected countries
     *
     * @param fromCountry          From country
     * @param coordinateX          X-coordinate in the location of the country on map
     * @param coordinateY          Y-coordinate in the location of the country on map
     * @param connectedToCountries List of GameMap objects of the countries that the fromCountry is connected to
     */
    public GameMap(Country fromCountry, float coordinateX, float coordinateY, ArrayList<GameMap> connectedToCountries) {
        this.fromCountry = fromCountry;
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
        this.connectedToCountries = connectedToCountries;
    }

    /**
     * Constructor to create a GameMap object with the given fromCountry
     *
     * @param fromCountry From country
     */
    public GameMap(Country fromCountry) {
        this.fromCountry = fromCountry;
    }

    /**
     * Returns index of country in the list
     * @return index of country in the list
     */
    public int getIndexOfCountryInList() {
        return indexOfCountryInList;
    }

    /**
     * Returns color of the given continent
     * @return color of the given continent
     */
    public int getContinentColor() {
        return continentColor;
    }

    /**
     * Set the color of the continent
     * @param continentColor
     */
    public void setContinentColor(int continentColor) {
        this.continentColor = continentColor;
    }

    /**
     * Set index of country in list
     * @param indexOfCountryInList
     */
    public void setIndexOfCountryInList(int indexOfCountryInList) {
        this.indexOfCountryInList = indexOfCountryInList;
    }

    /**
     * Getter function to return the from country
     *
     * @return from country
     */
    public Country getFromCountry() {

        return fromCountry;
    }

    /**
     * Setter function to set the from country
     *
     * @param fromCountry From country
     */
    public void setFromCountry(Country fromCountry) {

        this.fromCountry = fromCountry;
    }

    /**
     * Getter function to return list of all the countries that the from country is connected to (adjacency list)
     *
     * @return List of GameMap objects of the countries that the fromCountry is connected to
     */
    public ArrayList<GameMap> getConnectedToCountries() {

        return connectedToCountries;
    }

    /**
     * Return list of all connected countries as a comma separated string
     *
     * @return comma separated string of all connected countries
     */
    public String getConnectedCountriesAsString() {

        String returnString = "";
        for (GameMap map : connectedToCountries) {
            returnString += map.fromCountry.getNameOfCountry() + ",";

        }

        return returnString;
    }

    /**
     * Setter function to set the list of all countries that the from country is connected to (adjacency list)
     *
     * @param connectedToCountries List of GameMap objects of the countries that the fromCountry is connected to
     */
    public void setConnectedToCountries(ArrayList<GameMap> connectedToCountries) {

        this.connectedToCountries = connectedToCountries;
    }

    /**
     * Getter function to return the x-coordinate of the position of the from country
     *
     * @return X-coordinate in the location of the country on map
     */
    public float getCoordinateX() {

        return coordinateX;
    }

    /**
     * Setter function to set the x-coordinate of the position of the from country
     *
     * @param coordinateX X-coordinate in the location of the country on map
     */
    public void setCoordinateX(float coordinateX) {

        this.coordinateX = coordinateX;
    }

    /**
     * Getter function to return the y-coordinate of the position of the from country
     *
     * @return Y-coordinate in the location of the country on map
     */
    public float getCoordinateY() {

        return coordinateY;
    }

    /**
     * Setter function to set the y-coordinate of the position of the from country
     *
     * @param coordinateY Y-coordinate in the location of the country on map
     */
    public void setCoordinateY(float coordinateY) {

        this.coordinateY = coordinateY;
    }


}
