package com.app.risk.model;

import android.graphics.Color;

import com.app.risk.constants.GamePlayConstants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * GamePlay model to store the details of game currently being played
 * like list of continents and countries retrieved from map,
 * list of players and available cards, etc.
 *
 * @author Sagar Vetal
 * @version 1.0.0 (Date: 05/10/2018)
 */
public class GamePlay implements Serializable {

    private String mapName;
    private HashMap<String, Continent> continents;
    private HashMap<String, Country> countries;
    private HashMap<Integer, Player> players;
    private Player currentPlayer;
    private String currentPhase;
    private List<Card> cards;
    private Queue<Integer> playerIdQueue;
    private final int[] colorCodes = {Color.RED,Color.GREEN,Color.BLUE
            ,Color.DKGRAY,Color.MAGENTA,Color.YELLOW};
    private int armiesInExchangeOfCards;

    /**
     * Getter method to set the number of armies to be awarded in exchange of cards
     * @return number of armies that will be awarded in exchange of cards
     */
    public int getArmiesInExchangeOfCards() {
        return armiesInExchangeOfCards;
    }

    /**
     * Setter method to set number of armies to be awarded in exchange of cards
     * @param armiesInExchangeOfCards number of armies that will be awarded in exchange of cards
     */
    public void setArmiesInExchangeOfCards(int armiesInExchangeOfCards) {
        this.armiesInExchangeOfCards = armiesInExchangeOfCards;
    }

    /**
     * This is a default constructor.
     * It initializes the continent, country, player and card list.
     */
    public GamePlay() {
        this.continents = new HashMap<>();
        this.countries = new HashMap<>();
        this.players = new HashMap<>();
        this.cards = new ArrayList<>();
        this.playerIdQueue = new LinkedList<Integer>();
        this.armiesInExchangeOfCards = 0;
    }

    /**
     * Getter function to return the name of the map selected for game play
     *
     * @return name of the map
     */
    public String getMapName() {
        return mapName;
    }

    /**
     * Setter function to set the name of the map selected for game play.
     *
     * @param mapName The name of the map selected for game play.
     */
    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    /**
     * Getter function to return the list of continents retrieved from the map selected for game play.
     *
     * @return the list of continents retrieved from the map.
     */
    public HashMap<String, Continent> getContinents() {
        return continents;
    }

    /**
     * Setter function to set the list of continents retrieved from the map selected for game play.
     *
     * @param continents The list of continents retrieved from the map.
     */
    public void setContinents(HashMap<String, Continent> continents) {
        this.continents = continents;
    }

    /**
     * Getter function to return the list of countries retrieved from the map selected for game play.
     *
     * @return the list of countries retrieved from the map.
     */
    public HashMap<String, Country> getCountries() {
        return countries;
    }

    /**
     * Setter function to set the list of countries retrieved from the map selected for game play.
     *
     * @param countries The list of countries retrieved from the map.
     */
    public void setCountries(HashMap<String, Country> countries) {
        this.countries = countries;
    }

    /**
     * Getter function to return the list of players who are playing the game.
     *
     * @return the list of players who are playing the game.
     */
    public HashMap<Integer, Player> getPlayers() {
        return players;
    }

    /**
     * Setter function to set the list of players who are playing the game.
     *
     * @param players The list of players who are playing the game.
     */
    public void setPlayers(HashMap<Integer, Player> players) {
        this.players = players;
    }

    /**
     * Getter function to return the object of player whose turn is going on.
     *
     * @return the object of player whose turn is going on.
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Setter function to set the object of player whose turn is going on.
     *
     * @param currentPlayer The object of player whose turn is going on.
     */
    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    /**
     * This function is to set the object of player whose turn is going on.
     * It fetches the player id from queue.
     * If the player is active, it will set as a current player and push that player in end of queue.
     * If the player is inactive, it will remove that player from queue and recursively call itself.
     */
    public void setCurrentPlayer() {
        final Player player = players.get(playerIdQueue.poll());
        if (!player.isActive()) {
            setCurrentPlayer();
        } else {
            currentPlayer = player;
            playerIdQueue.offer(currentPlayer.getId());
        }
    }

    /**
     * Getter function to return the current phase.
     *
     * @return The current phase of the game.
     */
    public String getCurrentPhase() {
        return currentPhase;
    }

    /**
     * Setter function to set the current phase.
     *
     * @param currentPhase The current phase of the game.
     */
    public void setCurrentPhase(String currentPhase) {
        this.currentPhase = currentPhase;
    }

    /**
     * Getter function to return the list of cards available in game play
     *
     * @return list of cards
     */
    public List<Card> getCards() {
        return cards;
    }

    /**
     * Setter function to set the list of cards available in game play
     */
    public void setCards() {
        this.cards.add(new Card(GamePlayConstants.INFANTRY_CARD));
        this.cards.add(new Card(GamePlayConstants.CAVALRY_CARD));
        this.cards.add(new Card(GamePlayConstants.ARTILLERY_CARD));
    }

    /**
     * Getter function to return the queue of player ids.
     *
     * @return The queue of player ids.
     */
    public Queue<Integer> getPlayerIdQueue() {
        return playerIdQueue;
    }

    /**
     * Setter function to set the queue of player ids.
     *
     * @param playerIdQueue The queue of player ids.
     */
    public void setPlayerIdQueue(Queue<Integer> playerIdQueue) {
        this.playerIdQueue = playerIdQueue;
    }

    /**
     * This method gives list of countries concurred by given player.
     *
     * @param playerId This is player id.
     */
    public ArrayList<Country> getCountryListByPlayerId(final int playerId) {
        final ArrayList<Country> countryList = new ArrayList<>();
        for (final Country country : countries.values()) {
            if (country.getPlayer().getId() == playerId) {
                countryList.add(country);
            }
        }
        return countryList;
    }

    /**
     * This method set given players into GamePlaye object and assign ids.
     *
     * @param playerNames This is list of player names of type string.
     */
    public void setPlayers(final ArrayList<String> playerNames) {
        int id = 0;
        players.clear();
        playerIdQueue.clear();
        for (final String playerName : playerNames) {
            final Player player = new Player();
            player.setId(id++);
            player.setColorCode(colorCodes[player.getId()]);
            player.setName(playerName);
            player.setActive(true);
            players.put(player.getId(), player);
            playerIdQueue.add(player.getId());
        }
    }

    /**
     * Check if the cards selected by the player can be exchanged based on if they're similar or not
     * @param cardsToExchange list of cards selected by the player
     * @return true if all cards are same or all cards are completely different, false otherwise
     */
    public boolean cardsExchangeable(List<Card> cardsToExchange){

        if(cardsToExchange.size() == 3){
            if(cardsToExchange.get(0).getType().equals(cardsToExchange.get(1).getType()) &&
                    cardsToExchange.get(0).getType().equals(cardsToExchange.get(2).getType())){
                return true;
            } else if(!cardsToExchange.get(0).getType().equals(cardsToExchange.get(1).getType()) &&
                    !cardsToExchange.get(0).getType().equals(cardsToExchange.get(2).getType()) &&
                    !cardsToExchange.get(1).getType().equals(cardsToExchange.get(2).getType())){
                return true;
            } else {
                System.out.println("Card similarity rule not satisfied.");
                return false;
            }
        } else {
            System.out.println("Didn't choose enough cards");
            return false;
        }
    }
}
