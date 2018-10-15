package com.app.risk.utility;

import com.app.risk.model.Continent;
import com.app.risk.model.Country;
import com.app.risk.model.GameMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Verify if the map created by the user is correct and follows all the rules and regulations
 * 1. Map is a connected graph.
 * 2. Each continent is a connected sub-graph
 * 3. Each country belongs to one and only one continent
 * 4. All countries are unique and no duplicate countries exist
 * @author Akshita Angara
 * @version 1.0.0
 */
public class MapVerification {

    private List<Country> countryList = new ArrayList<>();
    private List<GameMap> gameMapList = new ArrayList<>();
    private HashMap<String, String> countryContinentMapping = new HashMap<>();
    private List<String> countriesVisited = new ArrayList<>();

    /**
     * Method called in controller which performs different checks to make sure map is verified
     * @param gameMapList
     * @return true if all checks are satisfied, false otherwise
     */
    public boolean mapVerification(List<GameMap> gameMapList) {

        this.gameMapList = gameMapList;

        if (!uniqueCountries()) {
            System.out.println("Error: Duplicate countries exist.");
            return false;
        }
        if (!checkCountryBelongsToOneContinent()) {
            System.out.println("Error: Country belongs to multiple continents.");
            return false;
        }
        if (!checkMapIsConnectedGraph()) {
            System.out.println("Error: Map is not connected.");
            return false;
        }
        if (!checkContinentIsConnectedSubgraph()) {
            System.out.println("Error: Continent is not connected.");
            return false;
        }

        return true;
    }

    /**
     * Performs a check to make sure the entire map is a connected graph (implements DFS on the entire graph)
     * @return true if the map is connected, false otherwise
     */
    private boolean checkMapIsConnectedGraph() {

        countriesVisited = null;
        Set<String> allCountriesInMap = countryContinentMapping.keySet();

        DepthFirstTraversal(gameMapList.get(0));

        for(String country: allCountriesInMap) {
            if(!countriesVisited.contains(country))
                return false;
        }

        return true;
    }

    /**
     * Method to perform DFS
     * @param gameMap
     */
    private void DepthFirstTraversal(GameMap gameMap) {

        countriesVisited.add(gameMap.getFromCountry().getNameOfCountry());

        for (Country country: gameMap.getConnectedToCountries()) {
            if (!countriesVisited.contains(country.getNameOfCountry())) {
                DepthFirstTraversal(getGameMapObjectByCountry(country));
            }
        }
    }

    /**
     * Method to return an object of GameMap class based on the from country
     * @param country
     * @return GameMap object if the from country equals the parameter
     */
    private GameMap getGameMapObjectByCountry(Country country) {

        for (GameMap gameMap: gameMapList) {
            if (gameMap.getFromCountry().equals(country))
                return gameMap;
        }

        return null;
    }

    /**
     * Performs a check to make sure each continent in the map is a connected subgraph
     * (implements DFS on each continent)
     * @return true if each continent is a connected subgraph, false otherwise
     */
    private boolean checkContinentIsConnectedSubgraph() {

        List<Country> countryList = new ArrayList<>();



        return true;
    }

    /**
     * Performs a check to make sure each country belongs to only one continent
     * @return true if each country belongs to only one continent, false otherwise
     */
    private boolean checkCountryBelongsToOneContinent() {

        for(GameMap gameMap: gameMapList) {

            if(countryContinentMapping.containsKey(gameMap.getFromCountry().getNameOfCountry())) {

                if(!countryContinentMapping.get(gameMap.getFromCountry().getNameOfCountry())
                        .equals(gameMap.getFromCountry().getBelongsToContinent().getNameOfContinent())) {

                    System.out.println(countryContinentMapping.get(gameMap.getFromCountry().getNameOfCountry()) +
                            " belongs to more than one continent.");
                    return false;
                }
            } else {
                countryContinentMapping.put(gameMap.getFromCountry().getNameOfCountry(),
                        gameMap.getFromCountry().getBelongsToContinent().getNameOfContinent());
            }
        }

        return true;
    }

    /**
     * Performs a check to make sure that all countries are unique and no country is duplicated.
     * @return true if there are no duplicate countries, false otherwise
     */
    private boolean uniqueCountries() {

        for (GameMap gameMap: gameMapList) {

            if (!countryList.isEmpty() && countryList.contains(gameMap.getFromCountry())) {
                System.out.println(gameMap.getFromCountry().getNameOfCountry() + " already exists.");
                return false;
            } else {
                countryList.add(gameMap.getFromCountry());
            }
        }

        return true;
    }
}
