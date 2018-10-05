package com.app.risk.utility;

import com.app.risk.model.Continent;
import com.app.risk.model.Country;
import com.app.risk.model.GameMap;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Read map from its text file representation to a class object to load the map
 * @author Akshita Angara
 */
public class ReadGameMapFromFile {

    private String line;
    private List<Continent> continentList = new ArrayList<>();
    private List<GameMap> gameMapList = new ArrayList<>();

    /**
     * Function to read from Conquest map file format to GameMap class object (loading a map)
     * @param fileName
     */
    public void readGameMapFromFile (String fileName) {

        try {

            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {

                if (line.equals("[Continents]")) {
                    while((line = bufferedReader.readLine()) != "\n\n\n") {

                        String[] words = line.split("=");
                        continentList.add(new Continent(words[0], Integer.parseInt(words[1])));
                    }
                }

                if (line.equals("[Territories]")) {
                    while((line = bufferedReader.readLine()) != "\n\n\n") {

                        String[] words = line.split(",");
                        GameMap tempGameMap = new GameMap();
                        if (continentBelongsToContinentList(words[3])) {

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
                if (continent.getNameOfContinent() == continentName)
                    flag = 1;
            }
        }

        if (flag==1)
            return true;
        else
            return false;
    }
}
