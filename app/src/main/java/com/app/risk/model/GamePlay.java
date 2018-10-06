package com.app.risk.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * GamePlay model to store the details of game currently being played
 * like list of continents and countries retrieved from map,
 * list of players and available cards, etc.
 * @author Sagar Vetal
 * @date 05/10/2018
 * @version 1.0.0
 */
public class GamePlay implements Serializable {

    private String mapName;
    private List<Continent> continents;
    private List<Country> countries;
    private List<Player> players;
    private Player currentPlayer;
    private List<Card> cards;

    /**
     * This is a default constructor.
     * It initializes the continent, country, player and card list.
     */
    public GamePlay() {
        this.continents = new ArrayList<>();
        this.countries = new ArrayList<>();
        this.players = new ArrayList<>();
        this.cards = new ArrayList<>();
    }

    /**
     * Getter function to return the name of the map selected for game play
     * @return name of the map
     */
    public String getMapName() {
        return mapName;
    }

    /**
     * Setter function to set the name of the map selected for game play.
     * @param mapName The name of the map selected for game play.
     */
    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    /**
     * Getter function to return the list of continents retrieved from the map selected for game play.
     * @return the list of continents retrieved from the map.
     */
    public List<Continent> getContinents() {
        return continents;
    }

    /**
     * Setter function to set the list of continents retrieved from the map selected for game play.
     * @param continents The list of continents retrieved from the map.
     */
    public void setContinents(List<Continent> continents) {
        this.continents = continents;
    }

    /**
     * Getter function to return the list of countries retrieved from the map selected for game play.
     * @return the list of countries retrieved from the map.
     */
    public List<Country> getCountries() {
        return countries;
    }

    /**
     * Setter function to set the list of countries retrieved from the map selected for game play.
     * @param countries The list of countries retrieved from the map.
     */
    public void setCountries(List<Country> countries) {
        this.countries = countries;
    }

    /**
     * Getter function to return the list of players who are playing the game.
     * @return the list of players who are playing the game.
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Setter function to set the list of players who are playing the game.
     * @param players The list of players who are playing the game.
     */
    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    /**
     * Getter function to return the object of player whose turn is going on.
     * @return the object of player whose turn is going on.
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Setter function to set the object of player whose turn is going on.
     * @param currentPlayer The object of player whose turn is going on.
     */
    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    /**
     * Getter function to return the list of cards available in game play
     * @return list of cards
     */
    public List<Card> getCards() {
        return cards;
    }

    /**
     * Setter function to set the list of cards available in game play
     * @param cards The card list available in game play
     */
    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

}
