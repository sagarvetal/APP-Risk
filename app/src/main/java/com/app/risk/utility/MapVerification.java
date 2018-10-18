package com.app.risk.utility;

import com.app.risk.model.Continent;
import com.app.risk.model.GameMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

/**
 * Verify if the map created by the user is correct and follows all the rules and regulations
 * 1. Map is a connected graph.
 * 2. Each continent is a connected sub-graph
 * 3. Each country belongs to one and only one continent
 * 4. All countries are unique and no duplicate countries exist in the map
 * @author Akshita Angara
 * @version 1.0.0
 */
public class MapVerification {

    private List<GameMap> gameMapList = new ArrayList<>();
    private HashMap<Object, Object> mappingForVerification = new HashMap<>();
    private Stack<GameMap> depthFirstTraversalStack = new Stack<>();
    private List<String> countriesVisited = new ArrayList<>();
    private HashMap<Continent, List<GameMap>> continentCountryMapping = new HashMap<>();

    /**
     * Method called in controller which performs different checks to make sure map is valid
     * @param gameMapList List of GameMap objects forming the map to be verified
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
     * Performs a check to make sure that all countries are unique and no country is duplicated.
     * @return true if there are no duplicate countries, false otherwise
     */
    private boolean uniqueCountries() {

        for (GameMap gameMap: gameMapList) {

            if(!mappingForVerification.isEmpty() && mappingForVerification.containsKey(gameMap.getFromCountry().getNameOfCountry())){
                System.out.println(mappingForVerification.containsKey(gameMap.getFromCountry().getNameOfCountry()) + " already exists.");
                return false;
            } else {
                mappingForVerification.put(gameMap.getFromCountry().getNameOfCountry(), gameMap.getFromCountry());
            }
        }

        mappingForVerification = null;
        return true;
    }

    /**
     * Performs a check to make sure each country belongs to only one continent
     * @return true if each country belongs to only one continent, false otherwise
     */
    private boolean checkCountryBelongsToOneContinent() {

        for(GameMap gameMap: gameMapList) {

            if(!mappingForVerification.isEmpty() && mappingForVerification.containsKey(gameMap.getFromCountry())
                    && !mappingForVerification.get(gameMap.getFromCountry()).equals(gameMap.getFromCountry().getBelongsToContinent())) {
                System.out.println(mappingForVerification.get(gameMap.getFromCountry().getNameOfCountry()) + " belongs to more than one continent.");
                return false;
            } else {
                mappingForVerification.put(gameMap.getFromCountry(), gameMap.getFromCountry().getBelongsToContinent());
            }
        }

        mappingForVerification = null;
        return true;
    }

    /**
     * Performs a check to make sure the entire map is a connected graph
     * (implements DFS on the entire graph)
     * @return true if the map is connected, false otherwise
     */
    private boolean checkMapIsConnectedGraph() {

        countriesVisited = null;
        depthFirstTraversalStack = null;
        depthFirstTraversalStack.push(gameMapList.get(0));

        DepthFirstTraversal(gameMapList);

        for(GameMap gameMap: gameMapList) {
            if(!countriesVisited.contains(gameMap.getFromCountry().getNameOfCountry()))
                return false;
        }

        return true;
    }

    /**
     * Performs a check to make sure each continent in the map is a connected subgraph
     * (implements DFS on each continent)
     * @return true if each continent is a connected subgraph, false otherwise
     */
    private boolean checkContinentIsConnectedSubgraph() {

        continentCountryMapping = null;
        generateContinentCountryMapping();

        for(Continent continent: continentCountryMapping.keySet()) {

            countriesVisited = null;
            depthFirstTraversalStack = null;
            List<GameMap> traversableCountries = continentCountryMapping.get(continent);
            depthFirstTraversalStack.push(traversableCountries.get(0));
            DepthFirstTraversal(traversableCountries);

            for(GameMap gameMap: traversableCountries) {
                if(!countriesVisited.contains(gameMap.getFromCountry().getNameOfCountry())) {
                    System.out.println(continent.getNameOfContinent() + " is not a connected subgraph.");
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Method to perform DFS on the list of countries that belong to the entire map or to a continent
     * @param traversableCountries Countries that belong to either the entire map (forming a map) or a continent (forming a subgraph)
     */
    private void DepthFirstTraversal(List<GameMap> traversableCountries) {

        while (!depthFirstTraversalStack.empty()){

            GameMap countryVisited = depthFirstTraversalStack.pop();

            if(!countriesVisited.isEmpty() && countriesVisited.contains(countryVisited.getFromCountry().getNameOfCountry())){
                continue;
            } else {
                countriesVisited.add(countryVisited.getFromCountry().getNameOfCountry());

                for (GameMap neighbourCountry: countryVisited.getConnectedToCountries()){
                    if(traversableCountries.contains(neighbourCountry)
                            && !countriesVisited.contains(neighbourCountry.getFromCountry().getNameOfCountry())){
                        depthFirstTraversalStack.push(neighbourCountry);
                    }
                }
            }
        }
    }

    /**
     * Method to generate a hashmap with continent as key and a list of countries belonging to that continent as its value
     */
    private void generateContinentCountryMapping() {

        for (GameMap gameMap: gameMapList) {
            if (!continentCountryMapping.isEmpty() && continentCountryMapping.containsKey(gameMap.getFromCountry().getBelongsToContinent())) {
                continentCountryMapping.get(gameMap.getFromCountry().getBelongsToContinent())
                        .add(gameMap);
            } else {
                List<GameMap> country = new ArrayList<>();
                country.add(gameMap);
                continentCountryMapping.put(gameMap.getFromCountry().getBelongsToContinent(), country);
            }
        }
    }
}
