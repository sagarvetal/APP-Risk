package com.app.risk.java.com.app.risk.utility;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import com.app.risk.constants.FileConstants;
import com.app.risk.constants.GamePlayConstants;
import com.app.risk.impl.CheaterPlayerStrategy;
import com.app.risk.impl.HumanPlayerStrategy;
import com.app.risk.model.Card;
import com.app.risk.model.Continent;
import com.app.risk.model.Country;
import com.app.risk.model.GamePlay;
import com.app.risk.model.Player;
import com.app.risk.utility.SaveLoadGame;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import static org.junit.Assert.assertTrue;

/**
 * This class is used to check whether the game which is saved to the file is loaded properly or not
 *
 * @author Akhila Chilukuri
 * @version 1.0.0
 */
public class LoadGameTest {
    /**
     * context instance would hold the instance of the target activity
     */
    private Context context = null;
    /**
     * gameplay instances would hold the objects required for the test cases
     */
    private GamePlay gm = null;

    /**
     * This method gets executed before the test case
     * sets the gameplay instance with the values required for the testing and context of the test case
     */
    @Before
    public void setUp() {
        gm = new GamePlay();
        ArrayList<String> playerNames = new ArrayList<String>();
        playerNames.add("Player1");
        playerNames.add("Player2");
        HashMap<String, Country> countryList = new HashMap<String, Country>();
        countryList.put("India", new Country("India", new Continent("Asia", 1)));
        countryList.put("Pakistan", new Country("Pakistan", new Continent("Africa", 2)));

        ArrayList<String> india = new ArrayList<String>();
        india.add("Pakistan");

        ArrayList<String> pakistan = new ArrayList<String>();
        pakistan.add("India");


        countryList.get("India").setAdjacentCountries(india);
        countryList.get("Pakistan").setAdjacentCountries(pakistan);

        gm.setCountries(countryList);
        ArrayList<String> strategy=new ArrayList<String>();
        strategy.add(GamePlayConstants.HUMAN_STRATEGY);
        strategy.add(GamePlayConstants.CHEATER_STRATEGY);
        gm.setPlayers(playerNames,strategy);
        gm.getCountries().get("India").setPlayer(gm.getPlayers().get(0));

        gm.getCountries().get("Pakistan").setPlayer(gm.getPlayers().get(1));

        gm.setCurrentPlayer(gm.getPlayers().get(0));
        gm.getCountries().get("Pakistan").setNoOfArmies(5);
        gm.getCountries().get("India").setNoOfArmies(5);
        context = InstrumentationRegistry.getTargetContext();
    }
    /**
     * This method checks whether the game which is saved to the file is loaded properly or not
     */
    @Test
    public void saveGameTest() {
        SaveLoadGame saveLoadGame=new SaveLoadGame();
        saveLoadGame.saveGame(gm,"saveLoadGameTest",context);
        String mapDir = context.getFilesDir() + File.separator + FileConstants.GAME_SAVE_LOAD_FILE_PATH + File.separator +"saveLoadGameTest.ser";
        File mapDirectory = new File(mapDir);
        assertTrue(mapDirectory.exists());
        GamePlay loadedGame=saveLoadGame.loadGame("saveLoadGameTest.ser",context);
        HashMap<Integer,Player> player=loadedGame.getPlayers();
        assertTrue(loadedGame.getPlayers().get(0).getStrategy() instanceof HumanPlayerStrategy);
        assertTrue(loadedGame.getPlayers().get(1).getStrategy() instanceof CheaterPlayerStrategy);
    }

    /**
     * This method gets executed after the test case has been executed
     * its sets the game map to null
     */
    @After
    public void cleanUp()
    {

        gm=null;
        context=null;
    }
}

