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
import java.util.List;

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
        PhaseViewController.getInstance().addAction(player.getName() + " has placed " + player.getReinforcementArmies() + " armies on " + strongestCountry.getNameOfCountry());
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
            final Country toCountry = getToCountry(gamePlay, player);
            if (toCountry != null){
                performAllOutAttack(player, strongestCountry, toCountry, countriesOwnedByPlayer);
            } else {
                break;
            }
        }
        if(player.isPlayerWon(gamePlay.getCountries())) {
            PhaseViewController.getInstance().addAction(player.getName() + " has conquered the entire map.");
            player.setPlayerWon(true);
        }
    }

    /**
     * This method finds the defender country.
     * @param gamePlay The GamePlay object.
     * @param player The Player object.
     * @return The defender country.
     */
    public Country getToCountry(final GamePlay gamePlay, final Player player){
        final HashMap<String,Country> countries = gamePlay.getCountries();
        for (String countryName :strongestCountry.getAdjacentCountries()){
            if (countries.get(countryName).getPlayer().getId() != player.getId()){
                return countries.get(countryName);
            }
        }
        return null;
    }


    /**
     * Method to perform all out attack from a country to another country till either the attacking country has only 1 army
     * or till the defending country has no armies remaining.
     * @param player Current player
     * @param fromCountry Attacking country
     * @param toCountry Defending country
     * @param countriesOwnedByPlayer All countries owned by the player
     */
    public void performAllOutAttack(final Player player, final Country fromCountry, final Country toCountry, final List<Country> countriesOwnedByPlayer){
        int noOfAttackerDice = 0;
        PhaseViewController.getInstance().addAction("\nAttacking country: " + strongestCountry.getNameOfCountry());
        PhaseViewController.getInstance().addAction("Defending country: " + toCountry.getNameOfCountry());
        final StringBuilder attackResult = new StringBuilder();
        while(strongestCountry.getNoOfArmies() > 1 && toCountry.getNoOfArmies() != 0){
            noOfAttackerDice = strongestCountry.getNoOfArmies() > 3 ? 3 : strongestCountry.getNoOfArmies() - 1 ;
            final int noOfDefenderDice = AttackPhaseController.getInstance().getDefenderDices(toCountry);
            final String result = player.performAttack(strongestCountry, toCountry, noOfAttackerDice, noOfDefenderDice).toString();
            attackResult.append(result);
        }
        PhaseViewController.getInstance().addAction(attackResult.toString());

        if(toCountry.getNoOfArmies() == 0){
            int noOfArmiesToMove = 1;
            if(noOfAttackerDice > fromCountry.getNoOfArmies()){
                noOfArmiesToMove = fromCountry.getNoOfArmies() - 1;
            } else {
                noOfArmiesToMove = noOfAttackerDice;
            }
            player.setNewCountryConquered(true);
            PhaseViewController.getInstance().addAction(player.getName() + " conquered " + toCountry.getNameOfCountry());
            toCountry.getPlayer().decrementCountries(1);
            toCountry.setNoOfArmies(noOfArmiesToMove);
            PhaseViewController.getInstance().addAction(noOfArmiesToMove +" armies moved from " + fromCountry.getNameOfCountry() + " to " + toCountry.getNameOfCountry());
            fromCountry.decrementArmies(noOfArmiesToMove);
            toCountry.setPlayer(player);
            player.incrementCountries(1);
            countriesOwnedByPlayer.add(toCountry);
        }
    }


    /**
     * This is fortification method for aggressive strategy player.
     * It fortifies in order to maximize aggregation of forces in one country.
     * @param gamePlay The GamePlay object.
     * @param player The Player object.
     */
    @Override
    public void fortificationPhase(final GamePlay gamePlay, final Player player, final ArrayList<Country> countriesOwnedByPlayer, final Country fromCountry) {
        Country srcCountry = null;
        Country destCountry = null;
        int maxNieghbourCountryCount = 0;
        for (final Country c1 : countriesOwnedByPlayer){
            for (final Country c2 : countriesOwnedByPlayer){
                if (c1 != c2){
                    if (checkIfPathExistBetweenCountries(c1,c2,gamePlay)){
                        if ((c1.getNoOfArmies() + c2.getNoOfArmies()) > maxNieghbourCountryCount){
                            srcCountry = c1;
                            destCountry = c2;
                            maxNieghbourCountryCount = srcCountry.getNoOfArmies() + destCountry.getNoOfArmies();
                        }
                    }
                }
            }
        }

        if(srcCountry != null && destCountry != null){
            final int noOfArmiesToMove = srcCountry .getNoOfArmies() - 1;
            destCountry.incrementArmies(noOfArmiesToMove);
            srcCountry.decrementArmies(noOfArmiesToMove);
            PhaseViewController.getInstance().addAction(noOfArmiesToMove + " armies moved from " + srcCountry.getNameOfCountry() + " to " + destCountry.getNameOfCountry());
        } else {
            PhaseViewController.getInstance().addAction("Fortification was not possible.");
        }

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
                    return isEndCountryFound;
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
        ArrayList<Country> countryList = new ArrayList<>();
        for (String CountryName : country.getAdjacentCountries()){
            if (gamePlay.getCountries().get(CountryName).getPlayer().getId() == country.getPlayer().getId()){
                countryList.add(gamePlay.getCountries().get(CountryName));
            }
        }
        return countryList;
    }
}
