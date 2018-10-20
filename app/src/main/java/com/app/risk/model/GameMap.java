package com.app.risk.model;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * GameMap class to store each node (country) of the game map graph
 *
 * @author Akshita Angara
 * @version 1.0.0
 */
public class GameMap implements Serializable {

    private Country fromCountry;
    private float coordinateX = 0;
    private float coordinateY = 0;
    private ArrayList<GameMap> connectedToCountries = new ArrayList<>();
    private int indexOfCountryInList;
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

    public int getIndexOfCountryInList() {
        return indexOfCountryInList;
    }

    public int getContinentColor() {
        return continentColor;
    }

    public void setContinentColor(int continentColor) {
        this.continentColor = continentColor;
    }

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

//    public int hashCode() {
//
//        return fromCountry.hashCode();
//    }
//
//    public boolean equals(Object obj) {
//        if (obj instanceof GameMap) {
//            if (this.fromCountry.equals(((GameMap) obj).fromCountry)) ;
//            {
//                return true;
//            }
//        }
//        return false;
//    }
//
//
//    public int compareTo(@NonNull GameMap gameMap) {
//        if (this.fromCountry.equals(gameMap.fromCountry))
//            return 0;
//        return this.fromCountry.compareTo(gameMap.fromCountry);
//    }
}
