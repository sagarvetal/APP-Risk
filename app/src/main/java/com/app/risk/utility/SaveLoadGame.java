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
import java.util.ArrayList;
import java.util.List;

public class SaveLoadGame {

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

    public GamePlay loadGame(String fileName, Context context) {

        try {

            String mapDir = context.getFilesDir() + File.separator + FileConstants.MAP_FILE_PATH;
            File mapDirectory = new File(mapDir, fileName);
            FileInputStream fileInputStream = new FileInputStream(new File(mapDirectory, fileName + FileConstants.GAME_SAVE_LOAD_FILE_EXTENSTION));
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
