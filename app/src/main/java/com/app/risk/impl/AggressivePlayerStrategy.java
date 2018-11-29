package com.app.risk.impl;

import com.app.risk.Interfaces.Strategy;
import com.app.risk.controller.AttackPhaseController;
import com.app.risk.controller.PhaseViewController;
import com.app.risk.model.Country;
import com.app.risk.model.GamePlay;
import com.app.risk.model.Player;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * A concrete strategy class that implements a aggressive player strategy.
 *
 * @author Sagar Vetal
 * @version 1.0.0 (Date: 22/11/2018)
 */
public class AggressivePlayerStrategy implements Strategy,Serializable {
    /**
     * country owned by player having maximum number of armies
     */
    private Country strongestCountry;

    /**
     * This is reinforcement method for aggressive strategy player.
     * It places the reinforcement armies on the strongest country having higher no of armies.
     * @param gamePlay The GamePlay object.
     * @param player The Player object.
     */
    @Override
    public void reinforcementPhase(final GamePlay gamePlay, final Player player, final ArrayList<Country> countriesOwnedByPlayer, final Country toCountry) {
        strongestCountry = countriesOwnedByPlayer.get(0);
        for (Country country: countriesOwnedByPlayer){
            if (country.getPlayer() == player){
                if (country.getNoOfArmies() > strongestCountry.getNoOfArmies()){
                    strongestCountry = country;
                }
            }
        }
        strongestCountry.incrementArmies(player.getReinforcementArmies());
        PhaseViewController.getInstance().addAction("Reinforcement Armies alloted : " + player.getReinforcementArmies());
        player.setReinforcementArmies(0);
    }

    /**
     * This is attack method for aggressive strategy player.
     * It attacks through the strongest country until it can not attack anymore.
     * @param gamePlay The GamePlay object.
     * @param player The Player object.
     */
    @Override
    public void attackPhase(final GamePlay gamePlay, final Player player, final ArrayList<Country> countriesOwnedByPlayer, final Country attackingCountry, final Country defendingCountry) {
        while (strongestCountry.getNoOfArmies()>1){
            Country toCountry = getToCountry(gamePlay, player);
            if (toCountry != null){
                int attackersDice = performAttack(player,toCountry);
                if (attackersDice > 0){
                    toCountry.getPlayer().decrementCountries(1);
                    toCountry.setPlayer(strongestCountry.getPlayer());
                    gamePlay.getCurrentPlayer().setNewCountryConquered(true);
                    int movingArmies = 0;
                    if (attackersDice <= strongestCountry.getNoOfArmies()){
                        movingArmies = attackersDice;
                    } else {
                        movingArmies = strongestCountry.getNoOfArmies();
                    }
                    toCountry.incrementArmies(movingArmies);
                    strongestCountry.decrementArmies(movingArmies);
                }
            } else {
                break;
            }
        }
        if(player.isPlayerWon(gamePlay.getCountries())) {
            player.setPlayerWon(true);
        }
    }

    /**
     * selects defender country
     * @param gamePlay - gameplay object
     * @param player - player object
     * @return defender country
     */
    public Country getToCountry(GamePlay gamePlay, Player player){
        HashMap<String,Country> countries = gamePlay.getCountries();
        for (String countryName :strongestCountry.getAdjacentCountries()){
            if (countries.get(countryName).getPlayer() != strongestCountry.getPlayer()){
                return countries.get(countryName);
            }
        }
        return null;
    }


    /**
     * Performs Attack
     * @param player  attacker player
     * @param toCountry defender player
     * @return no of attacker dice
     */
    public int performAttack(Player player, Country toCountry){
        int attackCount = 0;
        int noOfAttackerDice = 0;

        while(strongestCountry.getNoOfArmies() > 1 && toCountry.getNoOfArmies() != 0){
            noOfAttackerDice = getAttackerDice();
            final int noOfDefenderDice = AttackPhaseController.getInstance().getDefenderDices(toCountry);
            PhaseViewController.getInstance().addAction("\nAttack No : " + (++attackCount));
            PhaseViewController.getInstance().addAction("No of dice selected for attacker : " + noOfAttackerDice);
            PhaseViewController.getInstance().addAction("No of dice selected for defender : " + noOfDefenderDice);
            final String result = player.performAttack(strongestCountry, toCountry, noOfAttackerDice, noOfDefenderDice).toString();
            PhaseViewController.getInstance().addAction("Attack result : " + result);

        }
        return noOfAttackerDice;
    }

    /**
     * Gets attacker dice according to its no of armies
     * @return attacker dice
     */
    public int getAttackerDice(){
        return strongestCountry.getNoOfArmies() > 3 ? 3 : strongestCountry.getNoOfArmies() - 1 ;
    }


    /**
     * This is fortification method for aggressive strategy player.
     * It fortifies in order to maximize aggregation of forces in one country.
     * @param gamePlay The GamePlay object.
     * @param player The Player object.
     */
    @Override
    public void fortificationPhase(final GamePlay gamePlay, final Player player, final ArrayList<Country> countriesOwnedByPlayer, final Country fromCountry) {
        Country countryOne = new Country();
        Country countryTwo = new Country();
        int maxNieghbourCountryCount = 0;
        for (Country c1 : countriesOwnedByPlayer){
            for (Country c2 : countriesOwnedByPlayer){
                if (c1 != c2){
                    if (checkIfPathExistBetweenCountries(countryOne,countryTwo,gamePlay)){
                        if ((c1.getNoOfArmies()+c2.getNoOfArmies()>maxNieghbourCountryCount)){
                            countryOne = c1;
                            countryTwo = c2;
                            maxNieghbourCountryCount = countryOne.getNoOfArmies() + countryTwo.getNoOfArmies();
                        }
                    }
                }
            }

        }

        performFortification(countryOne,countryTwo);
        PhaseViewController.getInstance().addAction("Country fortified : " + countryOne.getNameOfCountry());

    }

    /**
     * Performs actual fortification, that is increase and decreases country
     * @param fortifyCountry- Country whose armies would be used for fortification
     * @param country - countries having maximum armies in neighbour
     */

    public void performFortification(Country country , Country fortifyCountry){
        PhaseViewController.getInstance().addAction("Fortified Armies : " + Integer.toString(fortifyCountry.getNoOfArmies()-1));
        country.incrementArmies(fortifyCountry.getNoOfArmies()-1);
        fortifyCountry.decrementArmies(fortifyCountry.getNoOfArmies()-1);
    }

    /**
     * Checks if path exist between 2 given countries
     * @param startCountry - country with startpoint of path
     * @param endCountry - country with endpoint fo path
     * @param gamePlay - gameplayobjec
     * @return returns true if path exist and false otherwise
     */
    public boolean checkIfPathExistBetweenCountries(Country startCountry,Country endCountry,GamePlay gamePlay){
        ArrayList<Country> nieghbourCountries = addOwnAdjacentCountries(gamePlay,startCountry);
        boolean isEndCountryFound = false;
        while(nieghbourCountries.size()>0){
            Iterator<Country> nieghbourCountryList = nieghbourCountries.iterator();
            while (nieghbourCountryList.hasNext()){
                Country country = nieghbourCountryList.next();
                ArrayList<Country> ownAdjacentCountries = addOwnAdjacentCountries(gamePlay,country);
                if (ownAdjacentCountries.contains(endCountry)){
                    isEndCountryFound = true;
                    break;
                } else {
                    nieghbourCountries.addAll(ownAdjacentCountries);
                    nieghbourCountryList.remove();
                }
            }
        }
        return isEndCountryFound;
    }

    /**
     * Returns list of adjacent countries which are owned by same player
     * @param gamePlay gameplay object
     * @param country country whose adjacent countries are to be found
     * @return list of adjacent countries
     */
    public ArrayList<Country> addOwnAdjacentCountries(GamePlay gamePlay,Country country){
        ArrayList<Country> arrCountry = new ArrayList<>();
        for (String CountryName : country.getAdjacentCountries()){
            if (gamePlay.getCountries().get(CountryName).getPlayer() == country.getPlayer()){
                arrCountry.add(gamePlay.getCountries().get(CountryName));
            }
        }
        return arrCountry;
    }
}
