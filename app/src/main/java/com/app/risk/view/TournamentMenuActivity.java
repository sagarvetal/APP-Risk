package com.app.risk.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.NumberPicker;

import com.app.risk.R;

import java.util.ArrayList;

public class TournamentMenuActivity extends AppCompatActivity implements View.OnClickListener {


    private CardView mapSelectionCard,stratergiesSelectionCard,okayCard;
    private NumberPicker playerRestrictionPicker;

    private ArrayList<String> mapArrayList;
    private String[] playerStratergies = {"Aggressive","Benevolent"
                                    ,"Random","Cheater"};

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

        for(int i=0;i<5;i++){
            mapArrayList.add("Map " + (i+1));
        }
    }


    @Override
    public void onClick(View v) {

        if(v == mapSelectionCard){

            new AlertDialog.Builder(this)
                    .setMultiChoiceItems((CharSequence[]) mapArrayList.toArray(), null, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                            if(isChecked){

                            }
                            else{

                            }
                        }
                    })
                    .setPositiveButton("Ok",null)
                    .setCancelable(false)
                    .create().show();

        }
        if(v == stratergiesSelectionCard){

            new AlertDialog.Builder(this)
                    .setMultiChoiceItems(playerStratergies, null, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                            if(isChecked){

                            }
                            else{

                            }
                        }
                    })
                    .setPositiveButton("Ok",null)
                    .setCancelable(false)
                    .create().show();

        }
        if(v == okayCard){
            // start Activity
        }
    }
}
