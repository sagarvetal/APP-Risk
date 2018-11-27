package com.app.risk.impl;

import com.app.risk.Interfaces.Strategy;
import com.app.risk.constants.GamePlayConstants;
import com.app.risk.controller.FortificationPhaseController;
import com.app.risk.controller.ReinforcementPhaseController;
import com.app.risk.model.Country;
import com.app.risk.model.GamePlay;
import com.app.risk.model.Player;
import com.app.risk.controller.PhaseViewController;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A concrete strategy class that implements a benevolent player strategy.
 *
 * @author Sagar Vetal
 * @version 1.0.0 (Date: 22/11/2018)
 */
public class BenevolentPlayerStrategy implements Strategy {
    private String currentPhase;

    /**
     * This is reinforcement method for benevolent strategy player.
     * It places the reinforcement armies on the weakest countries having less no of armies.
     * @param gamePlay The GamePlay object.
     * @param player The Player object.
     * @param countriesOwnedByPlayer The list of countries owned by player.
     * @param toCountry The country where player placing the reinforcement armies.
     */
    @Override
    public void reinforcementPhase(final GamePlay gamePlay, final Player player, final ArrayList<Country> countriesOwnedByPlayer, final Country toCountry) {
        int reinforcement = gamePlay.getCurrentPlayer().getReinforcementArmies();
        while (reinforcement != 0) {
            int min = findleastArmies(countriesOwnedByPlayer);
            for (Country country : countriesOwnedByPlayer) {
                if (country.getNoOfArmies() == min) {
                    PhaseViewController.getInstance().addAction("\nweak Country found : " + country.getNameOfCountry());
                    PhaseViewController.getInstance().addAction("\n" + gamePlay.getCurrentPlayer().getName() + " is placing reinforcement armies on " + country.getNameOfCountry());
                    gamePlay.getCurrentPlayer().decrementReinforcementArmies(1);
                    reinforcement-=1;
                    country.incrementArmies(1);
                    PhaseViewController.getInstance().addAction("\n" + gamePlay.getCurrentPlayer().getName() + " has placed 1 army on " + country.getNameOfCountry());
                    if (reinforcement == 0) {
                        PhaseViewController.getInstance().addAction("\n" + gamePlay.getCurrentPlayer().getName() + " has placed all his reinforcement armies.");
                        break;
                    }
                }
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
     * @param gamePlay The GamePlay object.
     * @param player The Player object.
     * @param countriesOwnedByPlayer The list of countries owned by player.
     * @param attackingCountry The attacker country
     * @param defendingCountry The defender country
     */
    @Override
    public void attackPhase(final GamePlay gamePlay, final Player player, final ArrayList<Country> countriesOwnedByPlayer, final Country attackingCountry, final Country defendingCountry) {
        PhaseViewController.getInstance().addAction("Benevolent never attacks");
    }

    /**
     * This is fortification method for benevolent strategy player.
     * It fortifies in order to move armies to weaker countries.
     * @param gamePlay The GamePlay object.
     * @param player The Player object.
     * @param countriesOwnedByPlayer The list of countries owned by player.
     * @param fromCountry The country from where player wants to move armies.
     */
    @Override
    public void fortificationPhase(final GamePlay gamePlay, final Player player, final ArrayList<Country> countriesOwnedByPlayer, final Country fromCountry) {
        HashMap<String, Integer> playerCountryDetails = playerCountryDetails(countriesOwnedByPlayer);
        int minimumArmies = playerCountryDetails.get("minimum");
        Country weakestCountry = null;

        for (Country country : countriesOwnedByPlayer) {
            if (country.getNoOfArmies() == minimumArmies)
                weakestCountry = country;
            break;
        }

        PhaseViewController.getInstance().addAction(weakestCountry.getNameOfCountry() + " is the weakest country owned by " + gamePlay.getCurrentPlayer().getName());
        PhaseViewController.getInstance().addAction("Checking all connected countries owned by " + gamePlay.getCurrentPlayer().getName());

        final ArrayList<String> reachableCountries = FortificationPhaseController.getInstance().getReachableCountries(weakestCountry, countriesOwnedByPlayer);
        ArrayList<Country> reachableCountryArrayList = getCountryArrayList(reachableCountries, countriesOwnedByPlayer);
        HashMap<String, Integer> strongestCountry = playerCountryDetails(reachableCountryArrayList);
        int maxInConnected = strongestCountry.get("maximum");
        int avgInConnected = strongestCountry.get("average");
        Country StrongCountry = null;
        if (maxInConnected >= 3 && (maxInConnected - avgInConnected) >= 1 && maxInConnected > minimumArmies) {
            for (Country country : reachableCountryArrayList) {
                if (country.getNoOfArmies() == maxInConnected) {
                    StrongCountry = country;
                    break;
                }
            }
            final int noOfArmies = maxInConnected - avgInConnected;
            StrongCountry.decrementArmies(noOfArmies);
            weakestCountry.incrementArmies(noOfArmies);
            PhaseViewController.getInstance().addAction(maxInConnected - avgInConnected + " armies are moved from " + StrongCountry.getNameOfCountry() + " to " + weakestCountry);
        } else {
            PhaseViewController.getInstance().addAction(weakestCountry.getNameOfCountry() + " is surrounded by weak countries");
        }
    }
    /**
     * This is method gets the list of countries that are reachable for a player
     * @param reachableCountries ArrayList of the country names which are reachable
     * @param playerCountryList ArrayList of the country owned by the player
     * @return ArrayList of country which are reachable
     */
    private ArrayList<Country> getCountryArrayList(ArrayList<String> reachableCountries, ArrayList<Country> playerCountryList) {
        ArrayList<Country> countries = new ArrayList<Country>();
        for (Country country : playerCountryList) {
            for (String name : reachableCountries) {
                if (country.getNameOfCountry().equalsIgnoreCase(name)) {
                    countries.add(country);
                    break;
                }
            }
        }
        return countries;
    }
}
