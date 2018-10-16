package com.app.risk.utility;

import com.app.risk.model.Continent;
import com.app.risk.model.Country;
import com.app.risk.model.GameMap;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Read map from its text file representation to a class object to load the map
 * @author Akshita Angara
 * @version 1.0.0
 */
public class ReadGameMapFromFile {

    private String line;
    private HashMap<String, GameMap> countryGameMapList = new HashMap<>();
    private List<Continent> continentList = new ArrayList<>();
    private List<GameMap> finalGameMapList = new ArrayList<>();

    /**
     * Function to read from Conquest map file format to GameMap class object (loading a map)
     * @param fileName
     */
    public List<GameMap> readGameMapFromFile (String fileName) {

        try {

            FileReader fileReader = new FileReader("C:\\Users\\akshi\\IdeaProjects\\Test\\src\\com\\app\\risk\\utility\\" + fileName + ".map");
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {

                if (line.equals("[Continents]")) {
                    while(true) {

                        line = bufferedReader.readLine();
                        if(line.equals(""))
                            break;

                        String[] words = line.split("=");
                        continentList.add(new Continent(words[0], Integer.parseInt(words[1])));
                    }
                }

                if (line.equals("[Territories]")) {
                    while(true) {

                        line = bufferedReader.readLine();
                        if((line == null) || (line.equals("")))
                            break;

                        String[] words = line.split(",");
                        GameMap gameMapForFinalList = new GameMap();

                        if (continentBelongsToContinentList(words[3])) {
                            Continent tempContinent = getContinentByName(words[3]);
                            if (tempContinent != null) {

                                if(!countryGameMapList.isEmpty() && countryGameMapList.containsKey(words[0])) {
                                    countryGameMapList.get(words[0]).getFromCountry().setBelongsToContinent(tempContinent);
                                } else {
                                    countryGameMapList.put(words[0], new GameMap(new Country(words[0], tempContinent)));
                                }

                                gameMapForFinalList.setFromCountry(countryGameMapList.get(words[0]).getFromCountry());
                                gameMapForFinalList.setCoordinateX(Integer.parseInt(words[1]));
                                gameMapForFinalList.setCoordinateY(Integer.parseInt(words[2]));
                                gameMapForFinalList.setConnectedToCountries(setAdjacentCountriesList(words));

                                finalGameMapList.add(gameMapForFinalList);

                            } else {
                                System.out.println("Error: Continent with name: " + words[3] +
                                        "does not exist in continent list.");
                            }
                        } else {
                            System.out.println("Error: Continent for country: " + words[0] +
                                    "does not belong to continent list.");
                        }
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return finalGameMapList;
    }

    /**
     * Function to set the list of all connected countries in the game map object
     * @param words - array of names of connected countries
     * @return list of country objects to be set in the game map object
     */
    private ArrayList<GameMap> setAdjacentCountriesList(String[] words) {

        ArrayList<GameMap> returnCountryList = new ArrayList<>();

        for (int i=4; i<words.length; i++) {
            if (countryGameMapList.containsKey(words[i])) {
                returnCountryList.add(countryGameMapList.get(words[i]));
            } else {
                countryGameMapList.put(words[i], new GameMap(new Country(words[i])));
                returnCountryList.add(countryGameMapList.get(words[i]));
            }
        }

        return returnCountryList;
    }

    /**
     * Check if the continent that the country belongs to is part of the continent list
     * @param continentName
     * @return true if continent belongs to continent list, false otherwise
     */
    private boolean continentBelongsToContinentList(String continentName) {

        int flag = 0;

        if(continentList.isEmpty()) {
            System.out.println("Error: Continent list is empty.");
            return false;
        } else {
            for (Continent continent : continentList) {
                if (continent.getNameOfContinent().equals(continentName))
                    flag = 1;
            }
        }

        if (flag==1)
            return true;
        else
            return false;
    }

    /**
     * Return a continent object from a list if its name equals the name of the continent passed as parameter
     * @param continentName
     * @return continent object which has the same name as the parameter
     */
    private Continent getContinentByName (String continentName) {

        if (continentList.isEmpty()) {
            System.out.println("Error: Continent list is empty");
            return null;
        } else {
            for (Continent continent : continentList) {
                if (continent.getNameOfContinent().equals(continentName))
                    return continent;
            }
        }

        return null;
    }
}
