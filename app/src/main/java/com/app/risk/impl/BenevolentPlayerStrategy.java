package com.app.risk.impl;

import com.app.risk.Interfaces.Strategy;
import com.app.risk.controller.FortificationPhaseController;
import com.app.risk.model.Country;
import com.app.risk.model.GamePlay;
import com.app.risk.model.Player;
import com.app.risk.controller.PhaseViewController;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * A concrete strategy class that implements a benevolent player strategy.
 *
 * @author Sagar Vetal
 * @version 1.0.0 (Date: 22/11/2018)
 */
public class BenevolentPlayerStrategy implements Strategy,Serializable {
    /**
     * currentPhase stores the current phase of the player.
     *
     */
    private String currentPhase;

    /**
     * This is reinforcement method for benevolent strategy player.
     * It places the reinforcement armies on the weakest countries having less no of armies.
     *
     * @param gamePlay               The GamePlay object.
     * @param player                 The Player object.
     * @param countriesOwnedByPlayer The list of countries owned by player.
     * @param toCountry              The country where player placing the reinforcement armies.
     */
    @Override
    public void reinforcementPhase(final GamePlay gamePlay, final Player player, final ArrayList<Country> countriesOwnedByPlayer, final Country toCountry) {
        int reinforcement = gamePlay.getCurrentPlayer().getReinforcementArmies();
            int min = findleastArmies(countriesOwnedByPlayer);
            for (Country country : countriesOwnedByPlayer) {
                if (country.getNoOfArmies() == min) {
                    PhaseViewController.getInstance().addAction("\nweak Country found : " + country.getNameOfCountry());
                    PhaseViewController.getInstance().addAction("\n" + gamePlay.getCurrentPlayer().getName() + " is placing reinforcement armies on " + country.getNameOfCountry());
                    country.incrementArmies(reinforcement);
                    gamePlay.getCurrentPlayer().decrementReinforcementArmies(reinforcement);
                    PhaseViewController.getInstance().addAction("\n" + gamePlay.getCurrentPlayer().getName() + " has placed "+reinforcement+" armies on " + country.getNameOfCountry());
                }
            }
    }

    /**
     * This is method helps in finding weakest country owned by the player
     *
     * @param countryListOwnedByPlayer ArrayList of the country owned by the player
     * @return weakest country army count of type int
     */
    private int findleastArmies(ArrayList<Country> countryListOwnedByPlayer) {
        int min = Integer.MAX_VALUE;
        for (Country country : countryListOwnedByPlayer) {
            if (country.getNoOfArmies() < min) {
                min = country.getNoOfArmies();
            }
        }
        return min;
    }

    /**
     * This is method helps in minimum,average and maximum number of armies in countries occupied by the player
     *
     * @param countryListOwnedByPlayer ArrayList of the country owned by the player
     * @return HashMap with the details of minimum,maximum and average number of armies in the countries owned by the player
     */
    private HashMap<String, Integer> playerCountryDetails(ArrayList<Country> countryListOwnedByPlayer) {
        HashMap<String, Integer> armiesDetails = new HashMap<String, Integer>();
        int minArmies = Integer.MAX_VALUE, maxArmies = Integer.MIN_VALUE, avgArmies = 0;
        int count = 0, armiesSum = 0;
        for (Country country : countryListOwnedByPlayer) {
            if (country.getNoOfArmies() > maxArmies)
                maxArmies = country.getNoOfArmies();
            else if (country.getNoOfArmies() < minArmies)
                minArmies = country.getNoOfArmies();
            count++;
            armiesSum += country.getNoOfArmies();
        }
        avgArmies = armiesSum / count;
        armiesDetails.put("minimum", minArmies);
        armiesDetails.put("maximum", maxArmies);
        armiesDetails.put("average", avgArmies);
        armiesDetails.put("count", count);
        return armiesDetails;

    }

    /**
     * This is attack method for benevolent strategy player.
     * It never attacks on any country.
     *
     * @param gamePlay               The GamePlay object.
     * @param player                 The Player object.
     * @param countriesOwnedByPlayer The list of countries owned by player.
     * @param attackingCountry       The attacker country
     * @param defendingCountry       The defender country
     */
    @Override
    public void attackPhase(final GamePlay gamePlay, final Player player, final ArrayList<Country> countriesOwnedByPlayer, final Country attackingCountry, final Country defendingCountry) {
        PhaseViewController.getInstance().addAction("Benevolent never attacks");
    }

    /**
     * This is fortification method for benevolent strategy player.
     * It fortifies in order to move armies to weaker countries.
     *
     * @param gamePlay               The GamePlay object.
     * @param player                 The Player object.
     * @param countriesOwnedByPlayer The list of countries owned by player.
     * @param fromCountry            The country from where player wants to move armies.
     */
    @Override
    public void fortificationPhase(final GamePlay gamePlay, final Player player, final ArrayList<Country> countriesOwnedByPlayer, final Country fromCountry) {
        ArrayList<Country> weakCountries = new ArrayList<Country>();
        weakCountries.addAll(countriesOwnedByPlayer);
        sortTheCountries(weakCountries, true);
        PhaseViewController.getInstance().addAction("Checking all weaker countries to make a fortification by " + gamePlay.getCurrentPlayer().getName());
        boolean fortification = false;
        for (Country weakCountry : weakCountries) {
            ArrayList<String> reachableCountries = FortificationPhaseController.getInstance().getReachableCountries(weakCountry, countriesOwnedByPlayer,false);
            if (reachableCountries.size() != 0) {
                ArrayList<Country> reachableCountryArrayList = getCountryArrayList(reachableCountries, gamePlay);
                sortTheCountries(reachableCountryArrayList, false);
                for (Country strongestNearWeakest : reachableCountryArrayList) {
                    if (strongestNearWeakest.getNoOfArmies() - weakCountry.getNoOfArmies() > 1) {
                        fortification = true;
                        int noOfArmies =( strongestNearWeakest.getNoOfArmies() - weakCountry.getNoOfArmies()) / 2;
                        strongestNearWeakest.decrementArmies(noOfArmies);
                        weakCountry.incrementArmies(noOfArmies);
                        PhaseViewController.getInstance().addAction(weakCountry.getNameOfCountry() + " is the one of the weaker countries owned by " + gamePlay.getCurrentPlayer().getName());
                        PhaseViewController.getInstance().addAction(noOfArmies + " armies are moved from " + strongestNearWeakest.getNameOfCountry() + " to " + weakCountry.getNameOfCountry());
                        break;
                    }
                }
                if (fortification) {
                    break;
                }
            }
        }
        if (!fortification) {
            PhaseViewController.getInstance().addAction("Fortification is not possible!!.Reason:Weak Country is not surrounded by any strong country");
        }
    }

    /**
     * This is method gets the list of countries that are reachable for a player
     *
     * @param reachableCountries ArrayList of the country names which are reachable
     * @param gamePlay           The GamePlay object.
     * @return ArrayList of country which are reachable
     */
    private ArrayList<Country> getCountryArrayList(ArrayList<String> reachableCountries, GamePlay gamePlay) {
        ArrayList<Country> countries = new ArrayList<Country>();
        for (String name : reachableCountries) {
            if(gamePlay.getCountries().containsKey(name))
            {
             countries.add(gamePlay.getCountries().get(name));
            }
        }
        return countries;
    }


    private void sortTheCountries(ArrayList<Country> countryList, boolean ascending) {
        if(countryList.size() > 0){
            final boolean ascend = ascending;
            Collections.sort(countryList, new Comparator<Country>() {
                public int compare(Country s1, Country s2) {
                    int result;
                    if (ascend) {
                        result = s1.getNoOfArmies() - (s2.getNoOfArmies());
                    } else {
                        result = s2.getNoOfArmies() - (s1.getNoOfArmies());
                    }
                    return result;
                }
            });
        }

    }
}
