package com.app.risk.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.app.risk.R;
import com.app.risk.constants.GamePlayConstants;
import com.app.risk.model.GameMap;
import com.app.risk.model.GamePlay;
import com.app.risk.utility.MapReader;
import com.app.risk.utility.MapVerification;

import java.util.ArrayList;
import java.util.List;

public class TournamentMenuActivity extends AppCompatActivity implements View.OnClickListener {


    private Button mapSelectionButton, stratergiesSelectionButton, okayButton;
    private NumberPicker playerRestrictionPicker,gameSelectionPicker;

    private ArrayList<String> mapArrayList;
    private ArrayList<String> selectedPlayerStratergies,selectedMapList;
    private String[] playerStratergiesArray = {GamePlayConstants.AGGRESSIVE_STRATEGY,
            GamePlayConstants.BENEVOLENT_STRATEGY,GamePlayConstants.RANDOM_STRATEGY,
            GamePlayConstants.CHEATER_STRATEGY};

    private String[] mapListArray;
    private boolean[] checkedStateMapArray,checkedStateStratergiesArray;
    private MapReader mapReader = new MapReader();


    /**
     * {@inheritDoc}
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournament_menu);

        getSupportActionBar().setTitle("Tournament Selection");

        initializeId();
        initilizeElements();

    }

    /**
     * This method initialize the Ids and sets listeners to buttons
     */
    public void initializeId(){

        mapSelectionButton = findViewById(R.id.tournament_menu_map_selection);
        stratergiesSelectionButton = findViewById(R.id.tournament_menu_player_stratergies);
        okayButton = findViewById(R.id.tournament_menu_okay_card);
        playerRestrictionPicker = findViewById(R.id.tournament_menu_number_picker);
        playerRestrictionPicker.setMinValue(10);
        playerRestrictionPicker.setMaxValue(50);
        playerRestrictionPicker.setValue(25);
        playerRestrictionPicker.setWrapSelectorWheel(false);

        gameSelectionPicker = findViewById(R.id.tournament_menu_game_number_picker);
        gameSelectionPicker.setMinValue(1);
        gameSelectionPicker.setMaxValue(5);
        gameSelectionPicker.setWrapSelectorWheel(false);

        mapSelectionButton.setOnClickListener(this);
        stratergiesSelectionButton.setOnClickListener(this);
        okayButton.setOnClickListener(this);

        mapArrayList = new ArrayList<>();
        selectedPlayerStratergies = new ArrayList<>();
        selectedMapList = new ArrayList<>();

        mapArrayList = mapReader.getMapList(this.getApplicationContext());

    }

    /**
     * This method initialize elements for alert dialogs
     */
    public void initilizeElements(){
        mapListArray = new String[mapArrayList.size()];
        mapArrayList.toArray(mapListArray);
        checkedStateMapArray = new boolean[mapArrayList.size()];

        for(int i = 0; i< checkedStateMapArray.length; i++){
            checkedStateMapArray[i] = false;
        }


        checkedStateStratergiesArray = new boolean[playerStratergiesArray.length];
        for(int i = 0; i< playerStratergiesArray.length; i++){
            checkedStateStratergiesArray[i] = false;
        }

    }


    /**
     * {@inheritDoc}
     * @param v: Invoking view
     */
    @Override
    public void onClick(View v) {

        if(v == mapSelectionButton){
            new AlertDialog.Builder(this)
                    .setMultiChoiceItems(mapListArray, checkedStateMapArray, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                            if(isChecked){
                                MapVerification mapVerification = new MapVerification();
                                boolean validMap = mapVerification.mapVerification(mapReader.returnGameMapFromFile(TournamentMenuActivity.this, mapListArray[which]));
                                if(validMap) {
                                    checkedStateMapArray[which] = true;
                                } else {
                                    Toast.makeText(TournamentMenuActivity.this, mapListArray[which] + " is invalid", Toast.LENGTH_LONG).show();
                                    checkedStateMapArray[which] = false;
                                    isChecked = false;
                                }
                            }
                            else{
                                checkedStateMapArray[which] = false;
                            }
                        }
                    })
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            addElementsToList(checkedStateMapArray,selectedMapList, mapListArray);

                            mapSelectionButton.setText(selectedMapList
                                    +   " maps selected");

                        }
                    })
                    .setTitle("Select Maps")
                    .setCancelable(false).create().show();
        }
        if(v == stratergiesSelectionButton){

            new AlertDialog.Builder(this)
                    .setMultiChoiceItems(playerStratergiesArray, checkedStateStratergiesArray, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                            if(isChecked){
                                checkedStateStratergiesArray[which] = true;
                            }
                            else{
                                checkedStateStratergiesArray[which] = false;
                            }
                        }
                    })
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            addElementsToList(checkedStateStratergiesArray
                            ,selectedPlayerStratergies,playerStratergiesArray);

                            stratergiesSelectionButton.setText(selectedPlayerStratergies
                            +   " stratergies selected");
                        }
                    })
                    .setTitle("Select Stratergies")
                    .setCancelable(false)
                    .create().show();

        }
        if(v == okayButton){
            int numberPickerValue = playerRestrictionPicker.getValue();
            int gamePickerValue = gameSelectionPicker.getValue();
            if(selectedPlayerStratergies.size() >=2 && selectedMapList.size() >=1){
                final Intent intent = new Intent(TournamentMenuActivity.this,PlayScreenActivity.class);
                intent.putStringArrayListExtra("MAPS",selectedMapList);
                intent.putStringArrayListExtra("PLAYER_STRATERGIES",selectedPlayerStratergies);
                intent.putExtra("NO_OF_GAMES",gamePickerValue);
                intent.putExtra("MAX_TURNS",numberPickerValue);
                intent.putExtra(GamePlayConstants.GAME_MODE, GamePlayConstants.TOURNAMENT_MODE);
                startActivity(intent);
            }
            else{
                Toast.makeText(this, "" +
                        "Please Select at least one stratergy \n" +
                        "and two player stratergies.", Toast.LENGTH_SHORT).show();
            }
        }
    }


    /**
     * This method adds elements to the final selected arrayList
     * @param checkedList : boolean which holds the checked state of multi choice alert dialog
     * @param arrayList : arraylist of elements
     * @param array: holds the element names such as whole maps and stratergies
     */
    public void addElementsToList(boolean[] checkedList,ArrayList<String> arrayList,String[] array){

        arrayList.clear();
        for(int i= 0;i<checkedList.length;i++){
            if(checkedList[i]){
                arrayList.add(array[i]);
            }
        }
    }
}
