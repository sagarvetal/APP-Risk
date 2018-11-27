package com.app.risk.controller;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.app.risk.R;
import com.app.risk.constants.GamePlayConstants;
import com.app.risk.model.Country;
import com.app.risk.model.GamePlay;
import com.app.risk.utility.LogManager;
import com.app.risk.view.PlayScreenActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * This class is used for the fortification phase.
 * Player can move his armies from a country he owns to another country
 * through a path formed by countries belonging to him.
 *
 * @author Sagar Vetal
 * @version 2.0.0 (Date: 05/11/2018)
 */
public class FortificationPhaseController {

    private GamePlay gamePlay;
    private Context context;
    private static FortificationPhaseController fortificationPhaseController;

    /**
     * This is default constructor.
     */
    private FortificationPhaseController() {
    }

    /**
     * This method implements the singleton pattern for FortificationPhaseController
     * @return The static reference of FortificationPhaseController.
     */
    public static FortificationPhaseController getInstance() {
        if(fortificationPhaseController == null) {
            fortificationPhaseController = new FortificationPhaseController();
        }
        return fortificationPhaseController;
    }

    /**
     * This method implements the singleton pattern for FortificationPhaseController and
     * also sets GamePlay and Context object.
     * @param gamePlay The GamePlay object
     * @param context The Context object
     * @return The static reference of FortificationPhaseController.
     */
    public static FortificationPhaseController init(final Context context, final GamePlay gamePlay) {
        getInstance();
        fortificationPhaseController.context = context;
        fortificationPhaseController.gamePlay = gamePlay;
        return fortificationPhaseController;
    }

    /**
     * This method shows the dailog box for fortification phase.
     * @param position The position of country in list owned by the player from where player wants to move armies.
     * @param countries The list of countries owned by current player.
     */
    public void showFortificationDialogBox(final int position, final ArrayList<Country> countries){
        if(countries.get(position).getNoOfArmies() > 1){
            LogManager.getInstance().addAction("Checking all connected countries owned by " + gamePlay.getCurrentPlayer().getName());

            final ArrayList<String> reachableCountries  = getReachableCountries(countries.get(position), countries);
            final String[] reachableCountryArray = new String[reachableCountries.size()];
            reachableCountries.toArray(reachableCountryArray);

            new AlertDialog.Builder(context)
                    .setTitle("Move Armies")
                    .setItems((CharSequence[]) reachableCountryArray, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int index) {
                            showDialogBoxToPlaceArmies(reachableCountryArray[index], position, countries);
                        }
                    })
                    .create()
                    .show();
        }
        else{
            LogManager.getInstance().addAction(countries.get(position) + " must have armies more than one to move.");
            Toast.makeText(context, "Country must have armies greater than one", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This method shows the dialog box to place armies on selected country.
     * @param toCountry The country to where player wants to move armies.
     * @param position The position of country in list owned by the player from where player wants to move armies.
     * @param countries The list of countries owned by current player.
     */
    public void showDialogBoxToPlaceArmies(String toCountry, final int position, final ArrayList<Country> countries){

        final AlertDialog.Builder placeArmiesDialogBox = new AlertDialog.Builder(context);
        placeArmiesDialogBox.setTitle("Place Armies");
        toCountry = toCountry.split(":")[0].trim();
        final View view = View.inflate(context,R.layout.play_screen_reinforcement_option,null);
        placeArmiesDialogBox.setView(view);

        final NumberPicker numberPicker = (NumberPicker) view.findViewById(R.id.number_picker);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(countries.get(position).getNoOfArmies() - 1);
        numberPicker.setWrapSelectorWheel(false);

        final String destinationCountry = toCountry;
        placeArmiesDialogBox.setPositiveButton("Move", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                fortifyCountry(countries.get(position), gamePlay.getCountries().get(destinationCountry), numberPicker.getValue());

                final String message = numberPicker.getValue() + " armies moved from " + countries.get(position).getNameOfCountry() + " to " + destinationCountry;
                LogManager.getInstance().addAction(message);
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                if(gamePlay.getCurrentPlayer().isNewCountryConquered()){
                    gamePlay.getCurrentPlayer().assignCards(gamePlay);
                    gamePlay.getCurrentPlayer().setNewCountryConquered(false);
                }
                getActivity().notifyPlayScreenRVAdapter();
                getActivity().changePhase(GamePlayConstants.REINFORCEMENT_PHASE);
            }
        });
        placeArmiesDialogBox.setNegativeButton("Cancel",null);
        placeArmiesDialogBox.create();
        placeArmiesDialogBox.show();
    }

    /**
     * This method is to perform DFS on the list of countries that belong to the player and find a path between two countries.
     * The path should also be formed by countries belonging to the same player
     *
     * @param fromCountry The object of from country
     * @param toCountry   The object of to country
     * @return true if there exists a path between two countries pertaining to the conditions, false otherwise
     */
    public boolean isCountriesConneted(final Country fromCountry, final Country toCountry) {

        final Stack<Country> depthFirstTraversalStack = new Stack<>();
        final List<Country> countriesVisited = new ArrayList<>();
        final List<Country> countriesBelongToPlayer = gamePlay.getCountryListByPlayerId(gamePlay.getCurrentPlayer().getId());

        if (countriesBelongToPlayer.contains(fromCountry)) {

            depthFirstTraversalStack.push(fromCountry);

            while (!depthFirstTraversalStack.empty()) {

                Country countryVisited = depthFirstTraversalStack.pop();

                if (countriesVisited.size() != 0 && countriesVisited.contains(countryVisited)) {
                    continue;
                } else {
                    countriesVisited.add(countryVisited);

                    for (String neighbourCountry : countryVisited.getAdjacentCountries()) {
                        Country country = gamePlay.getCountries().get(neighbourCountry);
                        if (countriesBelongToPlayer.contains(country)
                                && !countriesVisited.contains(country)) {
                            depthFirstTraversalStack.push(country);
                        }
                    }
                }
            }
        } else {
            System.out.print(fromCountry.getNameOfCountry() + " does not belong to player.");
        }

        if (countriesVisited.contains(fromCountry) && countriesVisited.contains(toCountry))
            return true;
        else
            return false;

    }

    /**
     * This method gives list country names which are connected to given country of same player.
     * @param fromCountry The country from which it needs to find other connected countries.
     * @param countriesOwnedByPlayer List of countries owned by player.
     * @return List of connected countries from given country.
     */
    public ArrayList<String> getReachableCountries(final Country fromCountry, final ArrayList<Country> countriesOwnedByPlayer){
        final ArrayList<String> reachableCountries = new ArrayList<>();
        for(final Country toCountry : countriesOwnedByPlayer) {
            if(!fromCountry.getNameOfCountry().equalsIgnoreCase(toCountry.getNameOfCountry()) &&
                    isCountriesConneted(fromCountry, toCountry)) {
                reachableCountries.add(toCountry.getNameOfCountry() + " : " + toCountry.getNoOfArmies());
            }
        }
        return reachableCountries;
    }

    /**
     * This method starts the fortification.
     * @param fromCountry The country from where player wants to move armies.
     * @param toCountry The country to where player wants to move armies.
     * @param noOfArmies The no of armies to be moved.
     */
    public void fortifyCountry(final Country fromCountry, final Country toCountry, final int noOfArmies) {
        gamePlay.getCurrentPlayer().fortificationPhase(fromCountry, toCountry, noOfArmies);
    }

    /**
     * This method cast the context and returns PlayScreenActivity object.
     * @return The PlayScreenActivity object.
     */
    public PlayScreenActivity getActivity() {
        return (PlayScreenActivity) context;
    }
}
