package com.app.risk.model;

import com.app.risk.constants.GamePlayConstants;
import com.app.risk.utility.LogManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Player model to capture player details
 * like name, assigned countries and armies, and cards earned
 *
 * @author Sagar Vetal
 * @version 1.0.0 (Date: 04/10/2018)
 */
public class Player extends Observable implements Serializable {

    private int id;
    private String name;
    private int colorCode;
    private int noOfCountries;
    private int noOfArmies;
    private int noOfContinents;
    private int reinforcementArmies;
    private List<Card> cards;
    private boolean isActive;
    private int armiesInExchangeOfCards;
    private boolean cardsExchangedInRound;
    private boolean isNewCountryConquered;

    /**
     * Check if the player has exchanged cards in ongoing round
     * @return true if cards have been exchanged, false otherwise
     */
    public boolean isCardsExchangedInRound() {
        return cardsExchangedInRound;
    }

    /**
     * Set true or false depending on if the player has exchanged cards in ongoing round
     * @param cardsExchangedInRound true if cards have been exchanged, false otherwise
     */
    public void setCardsExchangedInRound(boolean cardsExchangedInRound) {
        this.cardsExchangedInRound = cardsExchangedInRound;
    }

    /**
     * This is a default constructor and it initializes the card list.
     */
    public Player() {
        this.cards = new ArrayList<>();
        this.armiesInExchangeOfCards = 0;
        cardsExchangedInRound = false;
    }

    /**
     * Getter to return number of armies that should be awarded in exchange of cards
     * Getter to return number of armies that should be awarded in exchange of cards
     *
     * @return number of armies to be awarded in exchange of cards
     */
    public int getArmiesInExchangeOfCards() {
        return armiesInExchangeOfCards;
    }

    /**
     * Setter to set the number of armies that should be awarded in exchange of cards
     *
     * @param armiesInExchangeOfCards number of armies that should be awarded in exchange of cards
     */
    public void setArmiesInExchangeOfCards(int armiesInExchangeOfCards) {
        this.armiesInExchangeOfCards = armiesInExchangeOfCards;
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

    /**
     * Getter function to return the unique id of the player
     * @return id of player
     */
    public int getId() {
        return id;
    }

    /**
     * Setter function to set the unique id of the player
     * @param id The unique id of player
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Getter function to return the name of the player
     * @return name of the player
     */
    public String getName() {
        return name;
    }

    /**
     * Setter function to set the name of the player
     * @param name The name of the player
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter function to return the color code of the player
     * @return color code of the player
     */
    public int getColorCode() {
        return colorCode;
    }

    /**
     * Setter function to set the color code of the player to identify them
     *
     * @param colorCode The unique color code of the player
     */
    public void setColorCode(int colorCode) {
        this.colorCode = colorCode;
    }

    /**
     * Getter function to return the no of countries assigned to the player
     *
     * @return no of countries
     */
    public int getNoOfCountries() {
        return noOfCountries;
    }

    /**
     * Setter function to set the no of countries assigned to the player
     *
     * @param noOfCountries The number of countries assigned
     */
    public void setNoOfCountries(int noOfCountries) {
        this.noOfCountries = noOfCountries;
    }

    /**
     * This is function to set no of continents
     * @param noOfContinents
     */
    public void setNoOfContinents(int noOfContinents) {
        this.noOfContinents = noOfContinents;
    }

    /**
     * This is function to get  no of continents
     * @return
     */
    public int getNoOfContinents() {
        return noOfContinents;
    }

    /**
     * This function is to increment no of countries by given count.
     *
     * @param count The increment count by which the no of countries to be incremented.
     */
    public void incrementCountries(final int count) {
        this.noOfCountries += count;
        setChanged();
        notifyObservers(this);
    }

    /**
     * This function is to decrement no of countries by given count.
     *
     * @param count The decrement count by which the no of countries to be incremented.
     */
    public void decrementCountries(final int count) {
        this.noOfCountries -= count;
        setChanged();
        notifyObservers(this);
    }

    /**
     * Getter function to return the no of armies assigned to the player
     *
     * @return no of armies
     */
    public int getNoOfArmies() {
        return noOfArmies;
    }
    /**
     * Setter function to set the no of armies assigned to the player
     *
     * @param noOfArmies The number of armies assigned
     */
    public void setNoOfArmies(int noOfArmies) {
        this.noOfArmies = noOfArmies;
    }
    /**
     * This function is to increment no of armies by given count.
     *
     * @param count The increment count by which the no of armies to be incremented.
     */
    public void incrementArmies(final int count) {
        this.noOfArmies += count;
        setChanged();
        notifyObservers(this);
    }

    /**
     * This function is to decrement no of armies by given count.
     *
     * @param count The decrement count by which the no of armies to be decremented.
     */
    public void decrementArmies(final int count) {

        this.noOfArmies -= count;
        setChanged();
        notifyObservers(this);
    }

    /**
     * Getter function to return the no of reinforcement armies given to the player
     *
     * @return no of reinforcement armies
     */
    public int getReinforcementArmies() {
        return reinforcementArmies;
    }

    /**
     * Setter function to set the no of reinforcement armies given to the player
     *
     * @param reinforcementArmies The number of reinforcement armies
     */
    public void setReinforcementArmies(int reinforcementArmies) {
        this.reinforcementArmies = reinforcementArmies;
        setChanged();
        notifyObservers(this);
    }

    /**
     * This function is to decrement no of reinforcement armies by given count.
     *
     * @param count The decrement count by which the no of reinforcement armies to be decremented.
     */
    public void decrementReinforcementArmies(final int count) {
        this.reinforcementArmies -= count;
    }

    /**
     * Getter function to return the list of cards earned by player
     *
     * @return list of cards
     */
    public List<Card> getCards() {
        return cards;
    }
    /**
     * Setter function to set the card earned by player
     *
     * @param card The card earned by player
     */
    public void setCards(Card card) {
        this.cards.add(card);
    }

    /**
     * Setter function to update the list of cards owned by the player after exchange
     * @param cards list of cards owned by the player after exchange
     */
    public void setCards(List<Card> cards){
        this.cards = cards;
        setChanged();
        notifyObservers(this);
    }

    /**
     * Getter function to get the flag to determine game is over on not for respective player
     *
     * @return true if player's game is not over; otherwise return false.
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Setter function to set the flag to determine game is over on not for respective player
     *
     * @param active It is true if player's game is not over; otherwise false.
     */
    public void setActive(boolean active) {
        isActive = active;
    }


    /**
     * Getter function to get the flag to determine whether player conquered new country.
     * @return true if player conquered new country, otherwise return false.
     */
    public boolean isNewCountryConquered() {
        return isNewCountryConquered;
    }

    /**
     * Setter function to set the flag to determine whether player conquered new country.
     * @param isNewCountryConquered true if player conquered new country, otherwise false.
     */
    public void setNewCountryConquered(boolean isNewCountryConquered) {
        this.isNewCountryConquered = isNewCountryConquered;
    }


    /**
     * This is reinforcement method.
     * It sets the no of reinforcement armies given to the player based on no of countries player owns.
     *
     * @param gamePlay The GamePlay object.
     */
    public void reinforcementPhase(final GamePlay gamePlay) {
        final int reinforcementArmies = calculateReinforcementArmies(gamePlay);
        final int continentValue = getContinentValue(gamePlay);
        LogManager.getInstance().writeLog("Total reinforcement armies awarded : " + reinforcementArmies);
        LogManager.getInstance().writeLog("Total continent control value awarded : " + continentValue);
        setReinforcementArmies(reinforcementArmies + continentValue);
        incrementArmies(reinforcementArmies + continentValue);
    }

    /**
     * This method calculates the no of reinforcement armies.
     * @param gamePlay The GamePlay object.
     * @return The no of reinforcement armies given to the player based on no of countries player owns.
     */
    public int calculateReinforcementArmies(final GamePlay gamePlay) {
        final ArrayList<Country> countriesOwnedByPlayer = gamePlay.getCountryListByPlayerId(getId());
        final int reinforcementArmies = countriesOwnedByPlayer.size() / 3;
        if (reinforcementArmies > GamePlayConstants.MIN_REINFORCEMENT_AMRIES) {
            return reinforcementArmies;
        }
        return GamePlayConstants.MIN_REINFORCEMENT_AMRIES;
    }

    /**
     * This method gets continent value if player holds the complete continent.
     * @param gamePlay The GamePlay object.
     * @return The continent value if player holds the complete continent.
     */
    public int getContinentValue(final GamePlay gamePlay){
        int continentValue = 0;
        for(final Continent continent : gamePlay.getContinents().values()){
            boolean isWholeContinentOccupied = true;
            for(final Country country : continent.getCountries()) {
                if(getId() != gamePlay.getCountries().get(country.getNameOfCountry()).getPlayer().getId()) {
                    isWholeContinentOccupied = false;
                    break;
                }
            }
            if(isWholeContinentOccupied) {
                LogManager.getInstance().writeLog(getName() + " holds complete continent " + continent);
                LogManager.getInstance().writeLog(getName() + " gets " + continent.getArmyControlValue() + " armies corresponding to continent's control value.");
                continentValue += continent.getArmyControlValue();
            }
        }
        return continentValue;
    }

    /**
     * This is all out attack method in which attacker continues to attack
     * until either all his armies or all the defending armies have been eliminated.
     * If all the defender's armies are eliminated the attacker captures the territory.
     * @param attackingCountry The attacker country
     * @param defendingCountry The defender country
     * @return The attack result of type StringBuilder.
     */
    public StringBuilder performAllOutAttack(final Country attackingCountry, final Country defendingCountry){
        final StringBuilder attackResult = new StringBuilder();
        int attackCount = 0;
        while(attackingCountry.getNoOfArmies() > 1 && defendingCountry.getNoOfArmies() != 0){
            final int noOfAttackerDice = attackingCountry.getNoOfArmies() > 3 ? 3 : attackingCountry.getNoOfArmies() - 1;
            final int noOfDefenderDice = defendingCountry.getNoOfArmies() > 2 ? 2 : defendingCountry.getNoOfArmies();

            LogManager.getInstance().writeLog("\nAttack No : " + (++attackCount));
            LogManager.getInstance().writeLog("No of dice selected for attacker : " + noOfAttackerDice);
            LogManager.getInstance().writeLog("No of dice selected for defender : " + noOfDefenderDice);

            final String result = performAttack(attackingCountry, defendingCountry, noOfAttackerDice, noOfDefenderDice).toString();
            attackResult.append(result);
            attackResult.append("-------------------------------------------\n");
        }
        return attackResult;
    }

    /**
     * This is attack method in which given no of dices are rolled for attacker and defender.
     * If the defender rolls greater or equal to the attacker then the attacker loses an army,
     * otherwise the defender loses an army.
     * @param attackingCountry The attacker country
     * @param defendingCountry The defender country
     * @param noOfAttackerDice The no of dices chosen by attacker
     * @param noOfDefenderDice The no of dices chosen by defender
     * @return The attack result of type StringBuilder.
     */
    public StringBuilder performAttack(final Country attackingCountry, final Country defendingCountry, final int noOfAttackerDice, final int noOfDefenderDice){

        final ArrayList<Integer> attackerDiceRollsOutputList = getDiceRollsOutput(noOfAttackerDice);
        final ArrayList<Integer> defenderDiceRollsOutputList = getDiceRollsOutput(noOfDefenderDice);

        final StringBuilder attackResult = new StringBuilder();
        attackResult.append("\nBefore Attack : \n");
        attackResult.append("Attacker armies : " + attackingCountry.getNoOfArmies() + ", Defender armies : " + defendingCountry.getNoOfArmies() + "\n\n");

        while(!defenderDiceRollsOutputList.isEmpty() && !attackerDiceRollsOutputList.isEmpty() ){

            attackResult.append("Attacker dice : " + attackerDiceRollsOutputList.get(0) + ", Defender dice : " + defenderDiceRollsOutputList.get(0));

            if(defenderDiceRollsOutputList.get(0) >= attackerDiceRollsOutputList.get(0)){
                attackingCountry.decrementArmies(1);
                attackingCountry.getPlayer().decrementArmies(1);
                attackResult.append("\nDefender won \n");
            } else{
                defendingCountry.decrementArmies(1);
                defendingCountry.getPlayer().decrementArmies(1);
                attackResult.append("\nAttacker won \n");
            }
            defenderDiceRollsOutputList.remove(0);
            attackerDiceRollsOutputList.remove(0);
        }

        attackResult.append("\nAfter Attack : \n");
        attackResult.append("Attacker armies : " + attackingCountry.getNoOfArmies() + " Defender armies: " + defendingCountry.getNoOfArmies() + "\n");
        LogManager.getInstance().writeLog(attackResult.toString());
        return attackResult;
    }

    /**
     * This method returns the dice roll output for given no of dices.
     * @param noOfDices The number of dices to be rolled.
     * @return The dice roll output for given no of dices.
     */
    public ArrayList<Integer> getDiceRollsOutput(final int noOfDices) {
        final ArrayList<Integer> diceRollsOutput = new ArrayList<>();
        for(int i = 0; i < noOfDices; i++) {
            diceRollsOutput.add(generateRandom(1, 6));
        }
        Collections.sort(diceRollsOutput, Collections.<Integer>reverseOrder());
        return diceRollsOutput;
    }

    /**
     * Generates a random number for dice
     * @param lower : lower bound for the dice
     * @param upper: upper bound for the dice
     * @return Random integer between given lower and upper bound.
     */
    public int generateRandom(int lower,int upper){
        return (int)((Math.random() * upper) + lower);
    }

    /**
     * This method checks whether any country belonging to player has more than one armies.
     * @param gamePlay The GamePlay object.
     * @param countries List of countries owned by player.
     * @return true if any country belonging to player has more than one armies, otherwise false.
     */
    public boolean isMoreAttackPossible(final GamePlay gamePlay, final ArrayList<Country> countries) {
        for(final Country country : countries) {
            if(country.getNoOfArmies() > 1){
                for(final String adjacentCountry : country.getAdjacentCountries()){
                    if(gamePlay.getCountries().get(adjacentCountry).getPlayer().getId() != getId()){
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * This method checks whether the player conquered the whole map.
     * @param countries List of countries owned by player.
     * @return true if the player conquered the whole map, otherwise false.
     */
    public boolean isPlayerWon(final ArrayList<Country> countries) {
        for(final Country country : countries) {
            if(country.getPlayer().getId() != getId()){
                return false;
            }
        }
        return true;
    }


    /**
     * This is fortification method.
     * It moves armies from one country to another country.
     * @param fromCountry The country from where player wants to move armies.
     * @param toCountry The country to where player wants to move armies.
     * @param noOfArmies The no of armies to be moved.
     */
    public void fortificationPhase(final Country fromCountry, final Country toCountry, final int noOfArmies){
        fromCountry.decrementArmies(noOfArmies);
        toCountry.incrementArmies(noOfArmies);
    }

    /**
     * This method picks random card from game play and assign it to player.
     * @param gamePlay The GamePlay object.
     */
    public void assignCards(final GamePlay gamePlay) {
        final int randomIndex = ThreadLocalRandom.current().nextInt(gamePlay.getCards().size());
        final Card card = new Card(gamePlay.getCards().get(randomIndex).getType());
        setCards(card);
        LogManager.getInstance().writeLog(card.getType() + " has been awarded to " + getName());
    }

    /**
     * Returns number of continents owned by player
     * @param gamePlay current gameplay object
     * @return
     */
    public int getContinentsOwnedByPlayer(GamePlay gamePlay){
        int continentsOwnedByPlayer = 0;
        ArrayList<Country> arrCountiesOwnedByPlayer = gamePlay.getCountryListByPlayerId(getId());
        for (Continent continent : gamePlay.getContinents().values()) {
            ArrayList<Country> arrCountry = continent.getCountries();
            if(checkIfAllCountriesInContinents(arrCountiesOwnedByPlayer,arrCountry)){
                continentsOwnedByPlayer++;
            }
        }
        return continentsOwnedByPlayer;
    }

    /**
     * Checks if all countries of continent are owned by player
     * @param arrCountiesOwnedByPlayer - array of countries owned by player
     * @param arrCountriesOfContinent - arry of countries of continents
     * @return true if all countries of of continent are owned by user false otherwise
     */

    boolean checkIfAllCountriesInContinents(ArrayList<Country> arrCountiesOwnedByPlayer,ArrayList<Country> arrCountriesOfContinent){
        for (Country countryContinent : arrCountriesOfContinent){
            if (!checkIfCountryIsContainedWithPlayer(arrCountiesOwnedByPlayer,countryContinent)){
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if a country exist in list of countries
     * @param arrCountry list countries
     * @param country country to be checked
     * @return true if country is contained in list of countries
     */

    boolean checkIfCountryIsContainedWithPlayer(ArrayList<Country> arrCountry,Country country){
        for(Country countryPlayer : arrCountry){
            if (country == countryPlayer){
                return true;
            }
        }
        return false;
    }

    /**
     * Returns Percentage of map occupied by player
     * @param gamePlay
     * @return
     */
    public float getPercentageOfMapOwnedByPlayer(GamePlay gamePlay){
        ArrayList<Country> arrCountiesOwnedByPlayer = gamePlay.getCountryListByPlayerId(getId());
        int totalCountries = gamePlay.getCountries().values().size();
        float size=arrCountiesOwnedByPlayer.size();
        float countriesSize=totalCountries;
        float percentage = (size/countriesSize) *100;
        return percentage;
    }

}
