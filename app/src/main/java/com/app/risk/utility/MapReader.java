package com.app.risk.utility;

import com.app.risk.model.Continent;
import com.app.risk.model.Country;
import com.app.risk.model.GamePlay;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class is used to read the map to be used for game play.
 * @author Sagar Vetal
 * @version 1.0.0 (Date: 05/10/2018)
 */
public class MapReader {

    /**
     * This method takes input stream of the map as an argument,
     * retrieves list of continents and countries stored in the map and
     * returns the gamePlay object.
     * @param inputStream Input stream of the map.
     * @return the gamePlay object
     */
    public static GamePlay readMap(final InputStream inputStream) {
        final GamePlay gamePlay = new GamePlay();
        try {
            final BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String line = br.readLine();

            while(line != null) {

                switch (line.toLowerCase()) {
                    case "[map]":
                        line = br.readLine();
                        while(!line.isEmpty() && !line.startsWith("[")) {
                            if(line.startsWith("image")) {
                                gamePlay.setMapName(line.split("=")[1].trim());
                            }
                            line = br.readLine();
                        }
                        break;

                    case "[continents]":
                        line = br.readLine();
                        final HashMap<String, Continent> continents = new HashMap<>();

                        while(!line.isEmpty() && !line.startsWith("[")) {
                            if(line.contains("=")) {
                                final Continent continent = new Continent();
                                continent.setNameOfContinent(line.split("=")[0].trim());
                                continent.setArmyControlValue(Integer.parseInt(line.split("=")[1].trim()));
                                continents.put(continent.getNameOfContinent(), continent);
                            }
                            line = br.readLine();
                        }
                        gamePlay.setContinents(continents);
                        break;

                    case "[territories]":
                    case "[countries]":
                        line = br.readLine();
                        final HashMap<String, Country> countries = new HashMap<>();

                        while(line != null) {
                            final String[] elements = line.trim().split(",");

                            final ArrayList<String> adjacentCountries = new ArrayList<>();
                            for(int i = 4; i < elements.length; i++) {
                                adjacentCountries.add(elements[i].trim());
                            }

                            final Country country = new Country();
                            country.setNameOfCountry(elements[0]);
                            country.setBelongsToContinent(gamePlay.getContinents().get(elements[3].trim()));
                            country.setAdjacentCountries(adjacentCountries);

                            countries.put(country.getNameOfCountry(), country);
                            line = br.readLine();

                            while(line != null && line.isEmpty()) {
                                line = br.readLine();
                            }
                        }
                        gamePlay.setCountries(countries);
                        break;
                }
                line = br.readLine();
                while(line != null && line.isEmpty()) {
                    line = br.readLine();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return gamePlay;
    }

    /**
     * This method reads the map directory and returns the list of map.
     * @return list of string of map name.
     */
    public static ArrayList<String> getMapList(){
        final ArrayList<String> mapList = new ArrayList<>();
        /*final String rootPath = Environment.getExternalStorageDirectory().toString();
        final File mapDir = new File(rootPath + File.separator + FileConstants.MAP_FILE_PATH);
        for(final File file: mapDir.listFiles()) {
            mapList.add(file.getName());
        }*/
        mapList.add("map1.map");
        mapList.add("map2.map");
        mapList.add("map3.map");
        mapList.add("map4.map");
        return mapList;
    }
}
