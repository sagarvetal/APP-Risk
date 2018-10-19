package com.app.risk.controller;

import com.app.risk.model.Country;
import com.app.risk.model.GamePlay;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Fortification phase class: Player can move his armies from a country he owns to another country
 * through a path formed by countries belonging to him.
 * @author Akshita Angara
 * @version 1.0.0
 */
public class FortificationPhaseController {

    /**
     * Method to perform DFS on the list of countries that belong to the player and find a path between two countries.
     * The path should also be formed by countries belonging to the same player
     * @return true if there exists a path between two countries pertaining to the conditions, false otherwise
     */
    private boolean findPathBetweenCountries(int playerId, GamePlay gamePlay, Country fromCountry, Country toCountry) {

        Stack<Country> depthFirstTraversalStack = new Stack<>();
        List<Country> countriesVisited = new ArrayList<>();
        List<Country> countriesBelongingToPlayer = (new StartupPhaseController()).getCountryListByPlayerId(playerId, gamePlay);

        if(countriesBelongingToPlayer.contains(fromCountry)) {

            depthFirstTraversalStack.push(fromCountry);

            while (!depthFirstTraversalStack.empty()) {

                Country countryVisited = depthFirstTraversalStack.pop();

                if (countriesVisited.size() != 0 && countriesVisited.contains(countryVisited)) {
                    continue;
                } else {

                    countriesVisited.add(countryVisited);

                    for (String neighbourCountry : countryVisited.getAdjacentCountries()) {
                        Country country = gamePlay.getCountries().get(neighbourCountry);
                        if (countriesBelongingToPlayer.contains(country)
                                && !countriesVisited.contains(country)) {
                            depthFirstTraversalStack.push(country);
                        }
                    }
                }
            }
        } else {
            System.out.print(fromCountry.getNameOfCountry() + " does not belong to player.");
        }

        if(countriesVisited.contains(fromCountry) && countriesVisited.contains(toCountry))
            return true;
        else
            return false;

    }
}
