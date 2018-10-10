package com.app.risk.utility;

import android.os.Environment;

import com.app.risk.constants.FileConstants;
import com.app.risk.model.Continent;
import com.app.risk.model.Country;
import com.app.risk.model.GamePlay;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class is used to read the map to be used for game play.
 * @author Sagar Vetal
 * @date 05/10/2018
 * @version 1.0.0
 */
public class MapReader {

    /**
     * This method takes path of the map as an argument,
     * retrieves list of continents and countries stored in the map and
     * returns the gamePlay object.
     * @param mapfilePath Path of the map file.
     * @return the gamePlay object
     */
    public GamePlay readMap(final String mapfilePath) {
        final GamePlay gamePlay = new GamePlay();
        try {
            final FileReader fileReader = new FileReader(new File(mapfilePath));
            final BufferedReader br = new BufferedReader(fileReader);
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

                            final List<String> adjacentCountries = new ArrayList<>();
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
    public List<String> getMapList(){
        final List<String> mapList = new ArrayList<>();
        final String rootPath = Environment.getExternalStorageDirectory().toString();
        final File mapDir = new File(rootPath + File.separator + FileConstants.LOG_FILE_PATH);
        for(final File file: mapDir.listFiles()) {
            mapList.add(file.getName());
        }
        return mapList;
    }
}
