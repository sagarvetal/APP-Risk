package com.app.risk.utility;

import android.content.Context;

import com.app.risk.constants.FileConstants;
import com.app.risk.model.GamePlay;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Save a serializable game object to file and load a game from a file for play.
 *
 * @author Akshita Angara
 * @version 1.0.0
 */
public class SaveLoadGame {

    /**
     * Save a serialzable game object to a filename chosen by the user
     * @param gamePlay Serializable gampeplay object to be saved
     * @param fileName Filename chosen by the user
     * @param context Context of the application
     * @return True if the file is successfully written, false otherwise
     */
    public boolean saveGame(GamePlay gamePlay, String fileName, Context context) {

        try {

            String mapDir = context.getFilesDir() + File.separator + FileConstants.GAME_SAVE_LOAD_FILE_PATH;
            File mapDirectory = new File(mapDir);
            if (!mapDirectory.exists())
                mapDirectory.mkdirs();

            FileOutputStream fileOutputStream = new FileOutputStream(new File(mapDirectory, fileName + FileConstants.GAME_SAVE_LOAD_FILE_EXTENSTION));
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(gamePlay);
            objectOutputStream.close();
            fileOutputStream.close();
            return true;

        } catch (FileNotFoundException e) {
            System.out.println("SaveLoadGameController (Save): File exception with stacktrace: ");
            e.printStackTrace();
            return false;
        } catch (java.io.IOException e) {
            System.out.println("SaveLoadGameController (Save): IO exception with stacktrace: ");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Load a game for play from the file chosen by the user
     * @param fileName Filename chosen by the user
     * @param context Context of the application
     * @return Gameplay object loaded from the file chosen by the user for play
     */
    public GamePlay loadGame(String fileName, Context context) {

        try {

            String mapDir = context.getFilesDir() + File.separator + FileConstants.GAME_SAVE_LOAD_FILE_PATH;
            File gamePlayFile = new File(mapDir, fileName);
            FileInputStream fileInputStream = new FileInputStream(gamePlayFile);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            GamePlay gamePlay = (GamePlay) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
            return gamePlay;

        } catch (FileNotFoundException e) {
            System.out.println("SaveLoadGameController (Load): File exception with stacktrace: ");
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            System.out.println("SaveLoadGameController (Load): IO exception with stacktrace: ");
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException e) {
            System.out.println("SaveLoadGameController (Load): Class exception with stacktrace: ");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Method to return a list of saved games
     * @param context Context of the application
     * @return List of all the saved games
     */
    public String[] savedGamesList(Context context) {

        final String rootPath = context.getFilesDir().getAbsolutePath();
        final File mapDir = new File(rootPath + File.separator + FileConstants.GAME_SAVE_LOAD_FILE_PATH);
        System.out.println(mapDir);

        if(mapDir.list() == null)
            return null;
        else
            return mapDir.list();
    }
}
