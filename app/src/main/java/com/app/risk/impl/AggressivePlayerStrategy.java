package com.app.risk.impl;

import com.app.risk.Interfaces.Strategy;
import com.app.risk.controller.AttackPhaseController;
import com.app.risk.controller.PhaseViewController;
import com.app.risk.model.Country;
import com.app.risk.model.GamePlay;
import com.app.risk.model.Player;

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
            } else {
                break;
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
        HashMap<String,Country> countries = gamePlay.getCountries();
        for (String countryName :strongestCountry.getAdjacentCountries()){
            if (countries.get(countryName).getPlayer() != strongestCountry.getPlayer()){
                return countries.get(countryName);
            }
        }
        return null;
    }

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
        Country countryMaxNieghbours = new Country();
        int maxCount = 0;
        for (Country country : countriesOwnedByPlayer){
            int nieghbourArmyCount = neighbourArmiesCount(gamePlay,countriesOwnedByPlayer);
            int totalArmyCount = nieghbourArmyCount + country.getNoOfArmies();
            if (totalArmyCount > maxCount){
                maxCount = nieghbourArmyCount;
                countryMaxNieghbours = country;
            }
        }
        performFortification(gamePlay,countryMaxNieghbours,countriesOwnedByPlayer);
        PhaseViewController.getInstance().addAction("Country fortified : " + countryMaxNieghbours.getNameOfCountry());

    }

    /**
     * Performs actual fortification, that is increase and decreases country
     * @param gamePlay- Gameplay object
     * @param country - countries having maximum armies in neighbour
     */

    public void performFortification(GamePlay gamePlay, Country country, final ArrayList<Country> countriesOwnedByPlayer ){
        for (Country neighbourCountry : countriesOwnedByPlayer){
            country.incrementArmies(neighbourCountry.getNoOfArmies() - 1);
            neighbourCountry.decrementArmies(neighbourCountry.getNoOfArmies() - 1);
        }
    }

    /**
     * Calculates maximum number of armies in nieghbouring countries for given country
     * @param gamePlay gameplay object
     * @return maximum count
     */

    public int neighbourArmiesCount(GamePlay gamePlay, final ArrayList<Country> countriesOwnedByPlayer){
        int count = 0;
        for (Country  neighbourCountry: countriesOwnedByPlayer){
            count += neighbourCountry.getNoOfArmies();
        }
        return  count;
    }
}
