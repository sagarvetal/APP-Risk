package com.app.risk.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.app.risk.R;
import com.app.risk.constants.GamePlayConstants;
import com.app.risk.model.GamePlay;

import java.util.ArrayList;

public class TournamentMenuActivity extends AppCompatActivity implements View.OnClickListener {


    private CardView mapSelectionCard,stratergiesSelectionCard,okayCard;
    private NumberPicker playerRestrictionPicker;

    private ArrayList<String> mapArrayList;
    private ArrayList<String> selectedPlayerStratergies,selectedMapList;
    private String[] playerStratergies = {GamePlayConstants.AGGRESSIVE_STRATEGY,
            GamePlayConstants.BENEVOLENT_STRATEGY,GamePlayConstants.RANDOM_STRATEGY,
            GamePlayConstants.CHEATER_STRATEGY};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tournament_menu);

        initializeId();

    }

    public void initializeId(){

        mapSelectionCard = findViewById(R.id.tournament_menu_map_selection);
        stratergiesSelectionCard = findViewById(R.id.tournament_menu_player_stratergies);
        okayCard = findViewById(R.id.tournament_menu_okay_card);
        playerRestrictionPicker = findViewById(R.id.tournament_menu_number_picker);
        playerRestrictionPicker.setMinValue(10);
        playerRestrictionPicker.setMaxValue(50);
        playerRestrictionPicker.setValue(25);
        playerRestrictionPicker.setWrapSelectorWheel(false);

        mapSelectionCard.setOnClickListener(this);
        stratergiesSelectionCard.setOnClickListener(this);
        okayCard.setOnClickListener(this);

        mapArrayList = new ArrayList<>();
        selectedPlayerStratergies = new ArrayList<>();
        selectedMapList = new ArrayList<>();
        for(int i=0;i<5;i++){
            mapArrayList.add("Map " + (i+1));
        }
    }

    AlertDialog.Builder alertDialog;
    AlertDialog alertDialogMain;

    public void createAlertDialog(){

        final String[] mapArray = new String[mapArrayList.size()];
        mapArrayList.toArray(mapArray);
        Boolean[] checkedState = new Boolean[mapArrayList.size()];

        alertDialog = new AlertDialog.Builder(this);
                alertDialog.setMultiChoiceItems(mapArray, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if(isChecked){
                            selectedMapList.add(mapArray[which]);
                        }
                        else{
                            selectedMapList.remove(mapArray[which]);
                        }
                    }
                });
                alertDialog.setPositiveButton("Ok",null);
                alertDialog.setTitle("Select Maps");
                alertDialog.setCancelable(false);
                alertDialogMain = alertDialog.create();
    }

    @Override
    public void onClick(View v) {

        if(v == mapSelectionCard){
            alertDialogMain.show();
        }
        if(v == stratergiesSelectionCard){

            new AlertDialog.Builder(this)
                    .setMultiChoiceItems(playerStratergies, null, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                            if(isChecked){
                                selectedPlayerStratergies.add(playerStratergies[which]);
                            }
                            else{
                                selectedPlayerStratergies.remove(playerStratergies[which]);
                            }
                        }
                    })
                    .setPositiveButton("Ok",null)
                    .setTitle("Select Stratergies")
                    .setCancelable(false)
                    .create().show();

        }
        if(v == okayCard){
            int numberPickerValue = playerRestrictionPicker.getValue();

            Toast.makeText(this, ""
                    + numberPickerValue + "\n"
                    + selectedMapList.toString() + "\n"
                    + selectedPlayerStratergies.toString()+ "\n", Toast.LENGTH_SHORT).show();


        }
    }
}
