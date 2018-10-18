package com.app.risk.utility;


import android.content.Context;


import com.app.risk.model.Continent;
import com.app.risk.model.GameMap;

import java.io.BufferedWriter;

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
public class WriteGameMapToFile {

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


            FileOutputStream f = context.openFileOutput(fileName + ".map", Context.MODE_PRIVATE);
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
