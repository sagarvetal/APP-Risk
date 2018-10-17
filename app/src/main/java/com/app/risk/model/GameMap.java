package com.app.risk.model;

import java.util.ArrayList;
import java.util.List;

/**
 * GameMap class to store each node (country) of the game map graph
 * @author Akshita Angara
 * @version 1.0.0
 */
public class GameMap {

    private Country fromCountry;
    private float coordinateX = 0;
    private float coordinateY = 0;
    private ArrayList<GameMap> connectedToCountries = new ArrayList<>();
    private int indexOfCountryInList;
    private int continentColor;


    public int getIndexOfCountryInList(){
        return indexOfCountryInList;
    }

    public int getContinentColor(){
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
    public ArrayList<GameMap> getConnectedToCountries() {

        return connectedToCountries;
    }

    /**
     * Return list of all connected countries as a comma separated string
     * @return comma separated string of all connected countries
     */
    public String getConnectedCountriesAsString() {

        String returnString = "";
        for(GameMap map: connectedToCountries) {
            returnString += map.fromCountry.getNameOfCountry() + ",";
        }
        return returnString;
    }

    /**
     * Setter function to set the list of all countries that the from country is connected to (adjacency list)
     * @param connectedToCountries
     */
    public void setConnectedToCountries(ArrayList<GameMap> connectedToCountries) {

        this.connectedToCountries = connectedToCountries;
    }

    /**
     * Getter function to return the x-coordinate of the position of the from country
     * @return x-coordinate
     */
    public float getCoordinateX() {

        return coordinateX;
    }

    /**
     * Setter function to set the x-coordinate of the position of the from country
     * @param coordinateX
     */
    public void setCoordinateX(float coordinateX) {

        this.coordinateX = coordinateX;
    }

    /**
     * Getter function to return the y-coordinate of the position of the from country
     * @return y-coordinate
     */
    public float getCoordinateY() {

        return coordinateY;
    }

    /**
     * Setter function to set the y-coordinate of the position of the from country
     * @param coordinateY
     */
    public void setCoordinateY(float coordinateY) {

        this.coordinateY = coordinateY;
    }
}