package com.app.risk.controller;

import android.content.Context;

import com.app.risk.model.GamePlay;
import com.app.risk.utility.SaveLoadGame;

public class SaveLoadGameController {

    private static SaveLoadGame saveLoadGame = new SaveLoadGame();

    public static boolean saveGame(GamePlay gamePlay, String fileName, Context context) {

        return saveLoadGame.saveGame(gamePlay, fileName, context);
    }

    public static GamePlay loadGame(String fileName, Context context) {

        return saveLoadGame.loadGame(fileName, context);
    }

    public static String[] savedGamesList(Context context){

        return saveLoadGame.savedGamesList(context);
    }
}
