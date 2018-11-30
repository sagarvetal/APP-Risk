package com.app.risk.controller;

import android.content.Context;

import com.app.risk.model.GamePlay;
import com.app.risk.utility.SaveLoadGame;

/**
 * Controller to save and load a game.
 *
 * @author Akshita Angara
 * @version 1.0.0
 */
public class SaveLoadGameController {

    /**
     * saveLoadGame : Holds the save game instance
     */
    private static SaveLoadGame saveLoadGame = new SaveLoadGame();

    /**
     * Method to save the game and its state with the filename chosen by the user
     * @param gamePlay Game object to be saved
     * @param fileName Name of file chosen by user
     * @param context Context of the application
     * @return True if file is successfully written, false otherwise
     */
    public static boolean saveGame(GamePlay gamePlay, String fileName, Context context) {

        return saveLoadGame.saveGame(gamePlay, fileName, context);
    }

    /**
     * Method to load the game chosen by the user for play
     * @param fileName Name of file chosen by user to be loaded
     * @param context Context of the application
     * @return GamePlay object that is loaded from the file chosen by the user
     */
    public static GamePlay loadGame(String fileName, Context context) {

        return saveLoadGame.loadGame(fileName, context);
    }

    /**
     * Method to return a list of all the saved games
     * @param context Context of the application
     * @return List of all the saved games
     */
    public static String[] savedGamesList(Context context){

        return saveLoadGame.savedGamesList(context);
    }

    /**
     * Method to delete a saved file from the device memory once the file has been loaded for game play
     * @param fileName name of the file to be deleted
     * @param context context of the application
     */
    public static void deleteSavedFile(String fileName, Context context){

        saveLoadGame.deleteSavedFile(fileName, context);
    }
}
