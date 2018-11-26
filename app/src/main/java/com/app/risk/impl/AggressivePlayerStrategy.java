package com.app.risk.impl;

import com.app.risk.Interfaces.Strategy;
import com.app.risk.controller.AttackPhaseController;
import com.app.risk.model.Country;
import com.app.risk.model.GamePlay;
import com.app.risk.model.Player;
import com.app.risk.utility.LogManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Random;

/**
 * A concrete strategy class that implements a aggressive player strategy.
 *
 * @author Sagar Vetal
 * @version 1.0.0 (Date: 22/11/2018)
 */
public class AggressivePlayerStrategy implements Strategy {
    Country strongestCountry;
    /**
     * This is reinforcement method for aggressive strategy player.
     * It places the reinforcement armies on the strongest country having higher no of armies.
     * @param gamePlay The GamePlay object.
     * @param player The Player object.
     */
    @Override
    public void reinforcementPhase(final GamePlay gamePlay, final Player player, final ArrayList<Country> countriesOwnedByPlayer, final Country toCountry) {
        for (Country country: countriesOwnedByPlayer){
            if (country.getPlayer() == player){
                if (country.getNoOfArmies() > strongestCountry.getNoOfArmies()){
                    strongestCountry = country;
                }
            }
        }
        //handle case where there could be countries with same number of armies
        strongestCountry.incrementArmies(player.getReinforcementArmies());
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
            Country toCountry = getToCountry(gamePlay,player);
            int defenderDice = performAttack(player,toCountry);
            if (defenderDice > 0){
                toCountry.getPlayer().decrementCountries(1);
                toCountry.setPlayer(strongestCountry.getPlayer());
                gamePlay.getCurrentPlayer().setNewCountryConquered(true);
                int movingArmies = 0;
                if (defenderDice <= strongestCountry.getNoOfArmies()){
                    movingArmies = defenderDice;
                } else {
                    movingArmies = strongestCountry.getNoOfArmies();
                }
                strongestCountry.decrementArmies(movingArmies);
                toCountry.incrementArmies(movingArmies);
            }
        }
    }

    /**
     * selects defender country
     * @param gamePlay - gameplay object
     * @param player - player object
     * @return defender country
     */
    public Country getToCountry(GamePlay gamePlay, Player player){
        Country toCountry = getRandomCountry(gamePlay.getCountries().values());
        while (toCountry.getPlayer() == strongestCountry.getPlayer() && toCountry.getNoOfArmies()>0){
            toCountry = getRandomCountry(gamePlay.getCountries().values());
        }
        return toCountry;
    }

    public int performAttack(Player player, Country toCountry){
        int attackCount = 0;
        int noOfAttackerDice = 0;

        while(strongestCountry.getNoOfArmies() > 1 && toCountry.getNoOfArmies() != 0){
            noOfAttackerDice = getAttackerDice();
            final int noOfDefenderDice = getDefenderDice(toCountry);
            LogManager.getInstance().writeLog("\nAttack No : " + (++attackCount));
            LogManager.getInstance().writeLog("No of dice selected for attacker : " + noOfAttackerDice);
            LogManager.getInstance().writeLog("No of dice selected for defender : " + noOfDefenderDice);
            final String result = player.performAttack(strongestCountry, toCountry, noOfAttackerDice, noOfDefenderDice).toString();
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

    public int getDefenderDice(Country toCountry){
        int defendingDiceRoll = -1;
        if (toCountry.getPlayer().getStrategy() instanceof HumanPlayerStrategy)
            defendingDiceRoll = AttackPhaseController.getInstance()
                    .setUpDiceRollView(toCountry.getNoOfArmies() >= 2 ? 2 : 1);
        else if (toCountry.getPlayer().getStrategy() instanceof AggressivePlayerStrategy ||
                toCountry.getPlayer().getStrategy() instanceof CheaterPlayerStrategy)
            defendingDiceRoll = toCountry.getNoOfArmies() >= 2 ? 2 : 1;
        else if (toCountry.getPlayer().getStrategy() instanceof BenevolentPlayerStrategy)
            defendingDiceRoll = 1;
       return defendingDiceRoll;
    }

    /**
     * returns random country from give collection of countries
     * @param countries - collection of countries
     * @return random country form list of collection
     */

    private Country getRandomCountry(Collection countries) {
        Random rnd = new Random();
        int i = rnd.nextInt(countries.size());
        return (Country)countries.toArray()[i];
    }

    /**
     * This is fortification method for aggressive strategy player.
     * It fortifies in order to maximize aggregation of forces in one country.
     * @param gamePlay The GamePlay object.
     * @param player The Player object.
     */
    @Override
    public void fortificationPhase(final GamePlay gamePlay, final Player player, final ArrayList<Country> countriesOwnedByPlayer, final Country fromCountry) {
        Country countryMaxNieghbours = new Country();
        int maxCount = 0;
        for (Country country : countriesOwnedByPlayer){
            int nieghbourArmyCount = nieghbourArmiesCount(gamePlay,countriesOwnedByPlayer);
            int totalArmyCount = nieghbourArmyCount + country.getNoOfArmies();
            if (totalArmyCount > maxCount){
                maxCount = nieghbourArmyCount;
                countryMaxNieghbours = country;
            }
        }
        performFortification(gamePlay,countryMaxNieghbours,countriesOwnedByPlayer);
    }

    /**
     * Performs actual fortification, that is increase and decreases country
     * @param gamePlay- Gameplay object
     * @param country - countries having maximum armies in neighbour
     */

    public void performFortification(GamePlay gamePlay, Country country, final ArrayList<Country> countriesOwnedByPlayer ){

        for (Country neighbourCountry : countriesOwnedByPlayer){
            neighbourCountry.decrementArmies(neighbourCountry.getNoOfArmies() - 1);
            country.incrementArmies(neighbourCountry.getNoOfArmies() - 1);
        }
    }

    /**
     * Calculates maximum number of armies in nieghbouring countries for given country
     * @param gamePlay gameplay object
     * @return maximum count
     */

    public int nieghbourArmiesCount(GamePlay gamePlay,final ArrayList<Country> countriesOwnedByPlayer){
        int count = 0;
        for (Country  neighbourCountry: countriesOwnedByPlayer){
            count += neighbourCountry.getNoOfArmies();
        }
        return  count;
    }
}
