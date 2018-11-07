package com.app.risk.controller;

import android.content.Context;

import com.app.risk.model.Country;
import com.app.risk.model.GamePlay;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Fortification phase class: Player can move his armies from a country he owns to another country
 * through a path formed by countries belonging to him.
 *
 * @author Sagar Vetal
 * @version 2.0.0 (Date: 05/11/2018)
 */
public class FortificationPhaseController {

    private GamePlay gamePlay;
    private Context context;
    private static FortificationPhaseController fortificationPhaseController;

    /**
     * This is default constructor.
     */
    private FortificationPhaseController() {
    }

    /**
     * This method implements the singleton pattern for FortificationPhaseController
     * @return The static reference of FortificationPhaseController.
     */
    public static FortificationPhaseController getInstance() {
        if(fortificationPhaseController == null) {
            fortificationPhaseController = new FortificationPhaseController();
        }
        return fortificationPhaseController;
    }

    /**
     * This method implements the singleton pattern for FortificationPhaseController and
     * also sets GamePlay and Context object.
     * @param gamePlay The GamePlay object
     * @param context The Context object
     * @return The static reference of FortificationPhaseController.
     */
    public static FortificationPhaseController init(final Context context, final GamePlay gamePlay) {
        getInstance();
        fortificationPhaseController.context = context;
        fortificationPhaseController.gamePlay = gamePlay;
        return fortificationPhaseController;
    }

    /**
     * This method is to perform DFS on the list of countries that belong to the player and find a path between two countries.
     * The path should also be formed by countries belonging to the same player
     *
     * @param fromCountry The object of from country
     * @param toCountry   The object of to country
     * @return true if there exists a path between two countries pertaining to the conditions, false otherwise
     */
    public boolean isCountriesConneted(final Country fromCountry, final Country toCountry) {

        final Stack<Country> depthFirstTraversalStack = new Stack<>();
        final List<Country> countriesVisited = new ArrayList<>();
        final List<Country> countriesBelongToPlayer = gamePlay.getCountryListByPlayerId(gamePlay.getCurrentPlayer().getId());

        if (countriesBelongToPlayer.contains(fromCountry)) {

            depthFirstTraversalStack.push(fromCountry);

            while (!depthFirstTraversalStack.empty()) {

                Country countryVisited = depthFirstTraversalStack.pop();

                if (countriesVisited.size() != 0 && countriesVisited.contains(countryVisited)) {
                    continue;
                } else {
                    countriesVisited.add(countryVisited);

                    for (String neighbourCountry : countryVisited.getAdjacentCountries()) {
                        Country country = gamePlay.getCountries().get(neighbourCountry);
                        if (countriesBelongToPlayer.contains(country)
                                && !countriesVisited.contains(country)) {
                            depthFirstTraversalStack.push(country);
                        }
                    }
                }
            }
        } else {
            System.out.print(fromCountry.getNameOfCountry() + " does not belong to player.");
        }

        if (countriesVisited.contains(fromCountry) && countriesVisited.contains(toCountry))
            return true;
        else
            return false;

    }

    /**
     * This method gives list country names which are connected to given country of same player.
     * @param fromCountry The country from which it needs to find other connected countries.
     * @param countriesOwnedByPlayer List of countries owned by player.
     * @return List of connected countries from given country.
     */
    public ArrayList<String> getReachableCountries(final Country fromCountry, final ArrayList<Country> countriesOwnedByPlayer){
        final ArrayList<String> reachableCountries = new ArrayList<>();
        for(final Country toCountry : countriesOwnedByPlayer) {
            if(!fromCountry.getNameOfCountry().equalsIgnoreCase(toCountry.getNameOfCountry()) &&
                    isCountriesConneted(fromCountry, toCountry)) {
                reachableCountries.add(toCountry.getNameOfCountry() + " : " + toCountry.getNoOfArmies());
            }
        }
        return reachableCountries;
    }

    /**
     * This method starts the fortification.
     * @param fromCountry The country from which armies need to be moved.
     * @param toCountry The country where armies need to be moved.
     * @param noOfArmies The no of armies to be moved.
     */
    public void fortifyCountry(final Country fromCountry, final Country toCountry, final int noOfArmies) {
        gamePlay.getCurrentPlayer().fortificationPhase(fromCountry, toCountry, noOfArmies, gamePlay);
    }
}
