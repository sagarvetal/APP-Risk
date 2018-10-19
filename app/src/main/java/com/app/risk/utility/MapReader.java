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
 * @author Akshita Angara
 * @version 1.0.0
 */
public class MapReader {

    private String line;
    private HashMap<String, Continent> continentHashMap = new HashMap<>();
    private HashMap<String, Country> countryHashMap = new HashMap<>();
    private GamePlay finalGamePlay = new GamePlay();

    /**
     * Function to read from Conquest map file format to GameMap class object (loading a map)
     * @param context current state/context of the application
     * @param fileName user requested file name
     * @return GamePlay object
     */
    public GamePlay readGameMapFromFile (Context context, String fileName) {

        try {

            String mapDir = context.getFilesDir() + File.separator + FileConstants.MAP_FILE_PATH;
            File mapDirectory = new File(mapDir);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(mapDirectory, fileName))));

            while((line = bufferedReader.readLine()) != null) {

                if (line.equals("[Map]")) {
                    while (true) {

                        line = bufferedReader.readLine();

                        if(line.startsWith("image=")){
                            finalGamePlay.setMapName(line.split("=")[1].trim());
                        } else
                            break;
                    }
                }

                if (line.equals("[Continents]")) {
                    while(true) {

                        line = bufferedReader.readLine();
                        if(line.equals(""))
                            break;

                        String[] words = line.split("=");
                        continentHashMap.put(words[0], new Continent(words[0], Integer.parseInt(words[1])));
                    }

                    finalGamePlay.setContinents(continentHashMap);
                }

                if (line.equals("[Territories]")) {
                    while(true) {

                        line = bufferedReader.readLine();

                        if((line == null) || (line.equals("")))
                            break;

                        String[] words = line.split(",");

                        countryHashMap.put(words[0], new Country(words[0], finalGamePlay.getContinents().get(words[3]), setAdjacentCountriesList(words)));
                    }

                    finalGamePlay.setCountries(countryHashMap);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return finalGamePlay;
    }

    /**
     * Function to set the list of all connected countries in the game map object
     * @param words array of names of connected countries
     * @return list of country objects to be set in the game play object
     */
    private ArrayList<String> setAdjacentCountriesList(String[] words) {

        ArrayList<String> returnCountryList = new ArrayList<>();

        for (int i=4; i<words.length; i++) {
            returnCountryList.add(words[i]);
        }

        return returnCountryList;
    }

    /**
     * This method reads the map directory and returns the list of map.
     * @return list of string of map names.
     */
    public static ArrayList<String> getMapList(Context context){

        final ArrayList<String> mapList = new ArrayList<>();
        final String rootPath = context.getFilesDir().getAbsolutePath();
        final File mapDir = new File(rootPath + File.separator + FileConstants.MAP_FILE_PATH);
        System.out.println(mapDir);
        for(final String file: mapDir.list()) {
            mapList.add(file);
        }
        return mapList;
    }
}
