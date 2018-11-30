package com.app.risk.model;

import android.graphics.Color;

import com.app.risk.constants.GamePlayConstants;
import com.app.risk.impl.AggressivePlayerStrategy;
import com.app.risk.impl.BenevolentPlayerStrategy;
import com.app.risk.impl.CheaterPlayerStrategy;
import com.app.risk.impl.HumanPlayerStrategy;
import com.app.risk.impl.RandomPlayerStrategy;

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

    /**
     * Stores map name used in Game
     */
    private String mapName;
    /**
     * Stores list all continent name corresponding to its continent object
     */
    private HashMap<String, Continent> continents;
    /**
     * Stores list of all country name corresponding to its country object
     */
    private HashMap<String, Country> countries;
    /**
     * Stores list of all players id corresponding to is player object
     */
    private HashMap<Integer, Player> players;
    /**
     * Stores current player playing the game
     */
    private Player currentPlayer;
    /**
     * Stores current phase running ie. reinforcement,attack or fortification
     */
    private String currentPhase;
    /**
     * Stores list of cards used in game
     */
    private List<Card> cards;
    /**
     * Stores list of player id
     */
    private Queue<Integer> playerIdQueue;
    /**
     * Stores color code
     */
    private final int[] colorCodes = {Color.RED,Color.GREEN,Color.BLUE
            ,Color.DKGRAY,Color.MAGENTA,Color.YELLOW};
    /**
     * Stores no of turns for phases for tournament mode
     */
    private int noOfTurns;

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
        decrementNoOfTurns(1);
        final Player player = players.get(playerIdQueue.poll());
        if (!player.isActive()) {
            setCurrentPlayer();
        } else {
            currentPlayer = player;
            playerIdQueue.offer(currentPlayer.getId());
        }
    }

    /**
     * Getter function to get the no of turns remaining for the game in tournament mode.
     * @return The no of turns remaining for the game in tournament mode.
     */
    public int getNoOfTurns() {
        return noOfTurns;
    }

    /**
     * Setter function to set the no of turns given for the game in tournament mode.
     * @return The no of turns given for the game in tournament mode.
     */
    public void setNoOfTurns(int noOfTurns) {
        this.noOfTurns = noOfTurns;
    }

    /**
     * This method decrement the no of turn given for the game by given count.
     * @param count The count to decrease the no of turns of the game.
     */
    public void decrementNoOfTurns(int count) {
        if(this.noOfTurns > 0){
            this.noOfTurns -= count;
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
     * This method set given players into GamePlay object and assign ids.
     * @param playerNames This is list of player names of type string.
     * @param playerStrategies This is list of player strategies of type string.
     */
    public void setPlayers(final ArrayList<String> playerNames, final ArrayList<String> playerStrategies) {
        int id = 0;
        players.clear();
        playerIdQueue.clear();
        for (final String playerName : playerNames) {
            final Player player = new Player();
            player.setId(id++);
            player.setColorCode(colorCodes[player.getId()]);
            player.setName(playerName);
            player.setActive(true);
            setPlayerStrategy(player, playerStrategies.get(player.getId()));
            players.put(player.getId(), player);
            playerIdQueue.add(player.getId());
        }
    }

    /**
     * This method sets the strategy to the player.
     * @param player The player object.
     * @param strategyName The name of the strategy.
     */
    private void setPlayerStrategy(final Player player, final String strategyName) {
        switch(strategyName) {
            case GamePlayConstants.HUMAN_STRATEGY :
                player.setHuman(true);
                player.setStrategy(new HumanPlayerStrategy());
                break;
            case GamePlayConstants.AGGRESSIVE_STRATEGY :
                player.setHuman(false);
                player.setStrategy(new AggressivePlayerStrategy());
                break;
            case GamePlayConstants.BENEVOLENT_STRATEGY :
                player.setHuman(false);
                player.setStrategy(new BenevolentPlayerStrategy());
                break;
            case GamePlayConstants.RANDOM_STRATEGY :
                player.setHuman(false);
                player.setStrategy(new RandomPlayerStrategy());
                break;
            case GamePlayConstants.CHEATER_STRATEGY :
                player.setHuman(false);
                player.setStrategy(new CheaterPlayerStrategy());
                break;
        }
    }

    /**
     * This method checks whether player is holding any country or not.
     * If not, it makes that player inactive.
     */
    public void checkPlayerStatus(){
        for(final Player player : getPlayers().values()) {
            if(getCountryListByPlayerId(player.getId()).size() == 0){
                player.setActive(false);
            }
        }
    }
}
