package com.app.risk.utility;


import android.content.Context;


import com.app.risk.constants.FileConstants;
import com.app.risk.model.Continent;
import com.app.risk.model.GameMap;

import java.io.BufferedWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import java.util.ArrayList;
import java.util.List;

/**
 * Create and write game map defined by user to text file in internal storage
 * @author Akshita Angara
 * @version 1.0.0
 */
public class MapWriter {

    /**
     * Function to write the game map data to a text file in the given Conquest map file format.
     * @param context current state/context of the application
     * @param fileName user specified file name
     * @param gameMapList list of continent and connected countries of each country chosen by the user
     */
    public void writeGameMapToFile (Context context, String fileName, List<GameMap> gameMapList){

        List<Continent> continentList = new ArrayList<>();
        for(GameMap gameMap: gameMapList){
            if(!continentList.isEmpty() && continentList.contains(gameMap.getFromCountry().getBelongsToContinent()))
                continue;
            else
                continentList.add(gameMap.getFromCountry().getBelongsToContinent());
        }

        try {

            String mapDir = context.getFilesDir() + File.separator + FileConstants.MAP_FILE_PATH;
            File mapDirectory = new File(mapDir);
            if (!mapDirectory.exists())
                mapDirectory.mkdirs();
            FileOutputStream f = new FileOutputStream(new File(mapDirectory, fileName + FileConstants.MAP_FILE_EXTENSTION));
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(f));

            bufferedWriter.write("[Map]\n");
            bufferedWriter.write("author=\n");
            bufferedWriter.write("image=\n");
            bufferedWriter.write("wrap=\n");
            bufferedWriter.write("scroll=\n");
            bufferedWriter.write("warn=\n");
            bufferedWriter.newLine();

            bufferedWriter.write("[Continents]\n");
            for(Continent continent: continentList) {
                bufferedWriter.write(continent.getNameOfContinent() +
                        "=" +
                        continent.getArmyControlValue() +
                        "\n"
                );
            }
            bufferedWriter.newLine();

            bufferedWriter.write("[Territories]\n");
            for(GameMap gameMap: gameMapList) {
                bufferedWriter.write(gameMap.getFromCountry().getNameOfCountry() +
                        "," +
                        gameMap.getCoordinateX() +
                        "," +
                        gameMap.getCoordinateY() +
                        "," +
                        gameMap.getFromCountry().getBelongsToContinent().getNameOfContinent() +
                        "," +
                        gameMap.getConnectedCountriesAsString() +
                        "\n"
                );
            }
            bufferedWriter.newLine();

            bufferedWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
