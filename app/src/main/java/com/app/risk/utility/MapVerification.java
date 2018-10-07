package com.app.risk.utility;

import com.app.risk.model.GameMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Verify if the map created by the user is correct and follows all the rules and regulations
 * 1. Map is a connected graph.
 * 2. Each continent is a connected sub-graph
 * 3. Each country belongs to one and only one continent
 * @author Akshita Angara
 */
public class MapVerification {

    private List<GameMap> gameMapList = new ArrayList<>();

    /**
     * Method called in controller which performs different checks to make sure map is verified
     * @param gameMapList
     * @return true if all checks are satisfied, false otherwise
     */
    public boolean mapVerification(List<GameMap> gameMapList) {

        this.gameMapList = gameMapList;

        if (!checkCountryBelongsToOneContinent()) {
            System.out.println("Error: Country belongs to multiple continents.");
            return false;
        }

        return true;
    }

    /**
     * Performs a check to make sure each country belongs to only one continent
     * @return true if each country belongs to only one continent, false otherwise
     */
    private boolean checkCountryBelongsToOneContinent() {

        HashMap<String, String> countryContinentMapping = new HashMap<>();

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
}
