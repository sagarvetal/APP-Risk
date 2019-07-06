package com.app.risk.constants;

/**
 * This class contains all game play constant values
 * Like armies multiplier, etc.
 *
 * @author Sagar Vetal
 * @version 1.0.0 (Date: 08/10/2018)
 */
public class GamePlayConstants {

    public static final int AMRIES_MULTIPLIER = 2;

    public static final int MIN_REINFORCEMENT_AMRIES = 3;

    //Phases
    public static final String STARTUP_PHASE = "Startup Phase";
    public static final String REINFORCEMENT_PHASE = "Reinforcement Phase";
    public static final String ATTACK_PHASE = "Attack Phase";
    public static final String FORTIFICATION_PHASE = "Fortification Phase";
    public static boolean PHASE_IN_PROGRESS = false;

    //Cards
    public static final String INFANTRY_CARD = "Infantry Card";
    public static final String CAVALRY_CARD = "Cavalry Card";
    public static final String ARTILLERY_CARD = "Artillery Card";

    //Message
    public static final String REINFORCEMENT_MSG = "%s armies are holding %s countries. \n" +
                                                    "So %s has been awarded with %s additional armies";
    public static final String NO_CARDS_AVAILABLE = "No Cards Available";

    //Strategies
    public static final String HUMAN_STRATEGY = "Human Strategy";
    public static final String BENEVOLENT_STRATEGY = "Benevolent Strategy";
    public static final String AGGRESSIVE_STRATEGY = "Aggressive Strategy";
    public static final String RANDOM_STRATEGY = "Random Strategy";
    public static final String CHEATER_STRATEGY = "Cheater Strategy";

    //Sleep time (in milliseconds) after every phase
    public static final int SLEEP_TIME = 2500;

    //Game Mode
    public static final String GAME_MODE = "Game Mode";
    public static final String SINGLE_GAME_MODE = "Single Game Mode";
    public static final String TOURNAMENT_MODE = "Tournament Mode";

}
