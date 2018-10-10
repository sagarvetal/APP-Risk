package com.app.risk.utility;

import com.app.risk.model.Continent;
import com.app.risk.model.GameMap;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Create and write game map defined by user to text file
 * @author Akshita Angara
 * @version 1.0.0
 */
public class WriteGameMapToFile {

    /**
     * Function to write the game map data to a text file in the given Conquest map file format.
     * @param fileName - user specified file name
     * @param continentList - list of continents chosen by the user
     * @param gameMapList - list of continent and connected countries of each country chosen by the user
     */
    public void writeGameMapToFile (String fileName, List<Continent> continentList, List<GameMap> gameMapList){

        try {

            FileWriter fileWriter = new FileWriter("C:\\Users\\akshi\\IdeaProjects\\Test\\src\\com\\app\\risk\\utility\\" + fileName + ".map");
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

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
