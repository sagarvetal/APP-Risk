package com.app.risk.utility;


import android.content.Context;


import com.app.risk.constants.FileConstants;
import com.app.risk.model.Continent;
import com.app.risk.model.Country;
import com.app.risk.model.GameMap;
import com.app.risk.model.GamePlay;

import java.io.BufferedReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Read map from its text file representation to a class object to load the map from internal storage
 *
 * @author Akshita Angara
 * @version 1.0.0
 */
public class MapReader {

    private static String line;

    private static HashMap<String, Continent> continentHashMap = new HashMap<>();
    private static HashMap<String, Country> countryHashMap = new HashMap<>();
    private static GamePlay finalGamePlay = new GamePlay();

    private static HashMap<String, GameMap> countryGameMapList = new HashMap<>();
    private static List<Continent> continentList = new ArrayList<>();
    private static List<GameMap> finalGameMapList = new ArrayList<>();

    /**
     * Return GamePlay object after loading file to start playing
     *
     * @param context  current state/context of the application
     * @param fileName user requested file name
     * @return GamePlay object
     */
    public static GamePlay returnGamePlayFromFile(Context context, String fileName) {
        readGameMapFromFile(context, fileName);
        return finalGamePlay;
    }

    /**
     * Return GameMap object after loading file to edit map
     *
     * @param context  current state/context of the application
     * @param fileName user requested file name
     * @return List of GameMap object
     */
    public static List<GameMap> returnGameMapFromFile(Context context, String fileName) {
        readGameMapFromFile(context, fileName);
        return finalGameMapList;
    }

    /**
     * Function to read from Conquest map file format to GameMap class object (loading a map)
     *
     * @param context  current state/context of the application
     * @param fileName user requested file name
     */
    private static void readGameMapFromFile(Context context, String fileName) {

        try {

            String mapDir = context.getFilesDir() + File.separator + FileConstants.MAP_FILE_PATH;
            File mapDirectory = new File(mapDir);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(mapDirectory, fileName))));

            while ((line = bufferedReader.readLine()) != null) {

                if (line.equalsIgnoreCase("[Map]")) {
                    while (true) {

                        line = bufferedReader.readLine();
                        if (line.isEmpty())
                            break;

                    }
                }

                if (line.equalsIgnoreCase("[Continents]")) {
                    while (true) {
                        line = bufferedReader.readLine();
                        if (line.isEmpty())
                            break;

                        String[] words = line.split("=");
                        final Continent continent = new Continent(words[0].trim(), Integer.parseInt(words[1]));
                        continentList.add(continent);
                        continentHashMap.put(continent.getNameOfContinent(), continent);
                    }

                    finalGamePlay.setContinents(continentHashMap);
                }

                if (line.equalsIgnoreCase("[Territories]") || line.equalsIgnoreCase("[countries]")) {
                    while (true) {

                        line = bufferedReader.readLine();

                        if (line == null)
                            break;

                        if(line.isEmpty())
                            continue;

                        String[] words = line.split(",");

                        countryHashMap.put(words[0], new Country(words[0], finalGamePlay.getContinents().get(words[3]), setAdjacentCountriesListString(words)));

                        if (continentBelongsToContinentList(words[3])) {
                            Continent continent = finalGamePlay.getContinents().get(words[3]);
                            if (continent != null) {

                                final Country country = new Country(words[0].trim(), continent, setAdjacentCountriesListString(words));
                                continent.setCountries(country);
                                countryHashMap.put(country.getNameOfCountry(), country);

                                if (countryGameMapList != null && countryGameMapList.containsKey(words[0])) {
                                    countryGameMapList.get(words[0]).getFromCountry().setBelongsToContinent(continent);
                                } else {
                                    countryGameMapList.put(words[0], new GameMap(new Country(words[0], continent)));
                                }

                                GameMap gameMapForFinalList = countryGameMapList.get(words[0]);

                                gameMapForFinalList.setFromCountry(countryGameMapList.get(words[0]).getFromCountry());
                                gameMapForFinalList.setCoordinateX(Float.parseFloat(words[1]));
                                gameMapForFinalList.setCoordinateY(Float.parseFloat(words[2]));
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

                    finalGamePlay.setCountries(countryHashMap);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Function to set the list of all connected countries in the game map object
     *
     * @param words array of names of connected countries
     * @return list of country objects as string to be set in the game play object
     */
    private static ArrayList<String> setAdjacentCountriesListString(String[] words) {

        ArrayList<String> returnCountryList = new ArrayList<>();

        for (int i = 4; i < words.length; i++) {
            returnCountryList.add(words[i]);
        }

        return returnCountryList;
    }

    /**
     * This method reads the map directory and returns the list of map.
     *
     * @return list of string of map names.
     */
    public static ArrayList<String> getMapList(Context context) {

        final ArrayList<String> mapList = new ArrayList<>();
        final String rootPath = context.getFilesDir().getAbsolutePath();
        /*final File mapDir = new File(rootPath + File.separator + FileConstants.MAP_FILE_PATH);
        System.out.println(mapDir);
        for (final String file : mapDir.list()) {
            mapList.add(file);
        }*/
        mapList.add("cliff.map");
        mapList.add("twin.map");
        mapList.add("world.map");

        return mapList;
    }

    /**
     * Function to set the list of all connected countries in the game map object
     *
     * @param words array of names of connected countries
     * @return list of country objects as objects to be set in the game map object
     */
    private static ArrayList<GameMap> setAdjacentCountriesList(String[] words) {

        ArrayList<GameMap> returnCountryList = new ArrayList<>();

        for (int i = 4; i < words.length; i++) {
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
     *
     * @param continentName name of the continent
     * @return true if continent belongs to continent list, false otherwise
     */
    private static boolean continentBelongsToContinentList(String continentName) {

        int flag = 0;

        if (continentList.isEmpty()) {
            System.out.println("Error: Continent list is empty.");
            return false;
        } else {
            for (Continent continent : continentList) {
                if (continent.getNameOfContinent().equals(continentName))
                    flag = 1;
            }
        }

        if (flag == 1)
            return true;
        else
            return false;
    }

    /**
     * Return a continent object from a list if its name equals the name of the continent passed as parameter
     *
     * @param continentName name of the continent
     * @return continent object which has the same name as the parameter
     */
    private static Continent getContinentByName(String continentName) {

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
