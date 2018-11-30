package com.app.risk.controller;

import android.content.Context;

import com.app.risk.model.GameMap;
import com.app.risk.utility.MapReader;
import com.app.risk.utility.MapWriter;

import java.util.List;

/**
 * This controller is used to read and write the map.
 *
 * @author Sagar Vetal
 * @version 1.0.0
 */
public class MapDriverController {

    /**
     * It is a singleton instance of the controller
     */
    private static MapDriverController mapDriverController;

    /**
     * This is default constructor.
     */
    private MapDriverController() {
    }

    /**
     * This method implements the singleton pattern for MapDriverController
     * @return The static reference of MapDriverController.
     */
    public static MapDriverController getInstance() {
        if(mapDriverController == null) {
            mapDriverController = new MapDriverController();
        }
        return mapDriverController;
    }

    /**
     * Return GameMap object after loading file to edit map
     *
     * @param context current state/context of the application
     * @param mapName user requested file name
     * @return List of GameMap object
     */
    public List<GameMap> readmap(final Context context, String mapName) {
        final MapReader mapReader = new MapReader();
        return mapReader.returnGameMapFromFile(context, mapName);
    }

    /**
     * Function to write the game map data to a text file in the given Conquest map file format.
     *
     * @param context     current state/context of the application
     * @param mapName     user specified file name
     * @param gameMapList list of continent and connected countries of each country chosen by the user
     */
    public void writeMap(Context context, String mapName, List<GameMap> gameMapList) {
        final MapWriter mapWriter = new MapWriter();
        mapWriter.writeGameMapToFile(context, mapName, gameMapList);
    }
}
