package com.app.risk.controller;

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
 * @author Akshita Angara
 * @version 1.0.0
 */
public class FortificationPhaseController {

    private GamePlay gamePlay;

    /**
     * This parameterized constructor initializes the GamePlay object.
     *
     * @param gamePlay The GamePlay object.
     */
    public FortificationPhaseController(final GamePlay gamePlay) {
        this.gamePlay = gamePlay;
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

    public void assignCards() {
        final int randomIndex = ThreadLocalRandom.current().nextInt(gamePlay.getCards().size());
        gamePlay.getCurrentPlayer().setCards(gamePlay.getCards().get(randomIndex));
    }

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

}
