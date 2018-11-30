package com.app.risk.utility;

import com.app.risk.model.Continent;
import com.app.risk.model.Country;
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
 *
 * @author Akshita Angara
 * @version 1.0.0
 */
public class MapVerification {

    /**
     * List of gameMap objects that will be sent as parameter by the method requesting map verification
     */
    private List<GameMap> gameMapList = new ArrayList<>();
    /**
     * A common hashmap which is being used to verify the 3rd and 4th validity checks
     */
    private HashMap<Object, Object> mappingForVerification = new HashMap<>();
    /**
     * Stack to perform depth first search
     */
    private Stack<GameMap> depthFirstTraversalStack = new Stack<>();
    /**
     * List of countries visited during depth first traversal
     */
    private List<String> countriesVisited = new ArrayList<>();
    /**
     * Hashmap to store a mapping of all the countries that belong to one continent with continent as the key
     */
    private HashMap<Continent, List<GameMap>> continentCountryMapping = new HashMap<>();

    /**
     * Method called in controller which performs different checks to make sure map is valid
     *
     * @param gameMapList List of GameMap objects forming the map to be verified
     * @return true if all checks are satisfied, false otherwise
     */
    public boolean mapVerification(List<GameMap> gameMapList) {

        this.gameMapList = gameMapList;

        if (gameMapList.size()==0){
            System.out.println("Error: Size zero");
            return false;
        }

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
     *
     * @return true if there are no duplicate countries, false otherwise
     */
    public boolean uniqueCountries() {

        mappingForVerification.clear();

        for (GameMap gameMap : gameMapList) {

            if (mappingForVerification != null && mappingForVerification.containsKey(gameMap.getFromCountry().getNameOfCountry())) {
                Country country = (Country) mappingForVerification.get(gameMap.getFromCountry().getNameOfCountry());
                System.out.println(country.getNameOfCountry() + " already exists.");

                return false;
            } else {
                mappingForVerification.put(gameMap.getFromCountry().getNameOfCountry(), gameMap.getFromCountry());
            }
        }

        return true;
    }

    /**
     * Performs a check to make sure each country belongs to only one continent
     *
     * @return true if each country belongs to only one continent, false otherwise
     */
    public boolean checkCountryBelongsToOneContinent() {

        mappingForVerification.clear();

        for (GameMap gameMap : gameMapList) {

            if (mappingForVerification != null && mappingForVerification.containsKey(gameMap.getFromCountry())

                    && !mappingForVerification.get(gameMap.getFromCountry()).equals(gameMap.getFromCountry().getBelongsToContinent())) {
                System.out.println(mappingForVerification.get(gameMap.getFromCountry().getNameOfCountry()) + " belongs to more than one continent.");
                return false;
            } else {
                mappingForVerification.put(gameMap.getFromCountry(), gameMap.getFromCountry().getBelongsToContinent());
            }
        }

        return true;
    }

    /**
     * Performs a check to make sure the entire map is a connected graph
     * (implements DFS on the entire graph)
     *
     * @return true if the map is connected, false otherwise
     */
    public boolean checkMapIsConnectedGraph() {

        countriesVisited.clear();
        depthFirstTraversalStack.clear();
        depthFirstTraversalStack.push(gameMapList.get(0));

        depthFirstTraversal(gameMapList);

        for (GameMap gameMap : gameMapList) {
            if (!countriesVisited.contains(gameMap.getFromCountry().getNameOfCountry()))
                return false;
        }

        return true;
    }

    /**
     * Performs a check to make sure each continent in the map is a connected subgraph
     * (implements DFS on each continent)
     * @return true if each continent is a connected subgraph, false otherwise
     */
    public boolean checkContinentIsConnectedSubgraph() {

        continentCountryMapping.clear();

        generateContinentCountryMapping();

        for (Continent continent : continentCountryMapping.keySet()) {

            countriesVisited.clear();
            depthFirstTraversalStack.clear();

            List<GameMap> traversableCountries = continentCountryMapping.get(continent);
            depthFirstTraversalStack.push(traversableCountries.get(0));
            depthFirstTraversal(traversableCountries);

            for (GameMap gameMap : traversableCountries) {
                if (!countriesVisited.contains(gameMap.getFromCountry().getNameOfCountry())) {
                    System.out.println(continent.getNameOfContinent() + " is not a connected subgraph.");
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Method to perform DFS on the list of countries that belong to the entire map or to a continent
     *
     * @param traversableCountries Countries that belong to either the entire map (forming a map) or a continent (forming a subgraph)
     */
    private void depthFirstTraversal(List<GameMap> traversableCountries) {


        while (!depthFirstTraversalStack.empty()) {

            GameMap countryVisited = depthFirstTraversalStack.pop();
            countryVisited = getGameMapObjectFromList(countryVisited);

            if (countriesVisited != null && countriesVisited.contains(countryVisited.getFromCountry().getNameOfCountry())) {
                continue;
            } else {
                countriesVisited.add(countryVisited.getFromCountry().getNameOfCountry());
                for (GameMap neighbourCountry : countryVisited.getConnectedToCountries()) {
                    if (isCountryANeighbour(traversableCountries, neighbourCountry)
                            && !countriesVisited.contains(neighbourCountry.getFromCountry().getNameOfCountry())) {
                        depthFirstTraversalStack.push(neighbourCountry);
                    }
                }
            }
        }
    }

    /**
     * Get updated object from gamemaplist
     * @param map map object from DFS
     * @return updated object from game map list
     */
    public GameMap getGameMapObjectFromList(GameMap map){
        for (GameMap map1 : gameMapList){
            if (map1.getFromCountry().getNameOfCountry().equals(map.getFromCountry().getNameOfCountry())){
                return map1;
            }
        }
        return null;
    }
    /**
     * Method to check if the adjacent neighbour country can be traversed to find a path
     * @param traversableCountries list of traversable countries
     * @param neighbourCountry adjacent neighbour country
     * @return true if the neighbour country belongs to the list of trversable countries, false otherwise
     */
    private boolean isCountryANeighbour(List<GameMap> traversableCountries, GameMap neighbourCountry) {
        for(GameMap country: traversableCountries){
            if(country.getFromCountry().getNameOfCountry().equals(neighbourCountry.getFromCountry().getNameOfCountry()))
                return true;
        }
        return false;
    }

    /**
     * Method to generate a hashmap with continent as key and a list of countries belonging to that continent as its value
     */
    public void generateContinentCountryMapping() {

        for (GameMap gameMap : gameMapList) {

            if (continentCountryMapping != null && continentCountryMapping.containsKey(gameMap.getFromCountry().getBelongsToContinent())) {

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
