package com.app.risk.view;

import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.NumberPicker;

import com.app.risk.R;

public class TournamentMenuActivity extends AppCompatActivity implements View.OnClickListener {


    private CardView mapSelectionCard,stratergiesSelectionCard,okayCard;
    private NumberPicker playerRestrictionPicker;

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
    }


    @Override
    public void onClick(View v) {

        if(v == mapSelectionCard){

            new AlertDialog.Builder(this)
                    .setMultiChoiceItems(null,null,null)
                    .setPositiveButton("Ok",null)
                    .setCancelable(false)
                    .create().show();

        }
        if(v == stratergiesSelectionCard){

            new AlertDialog.Builder(this)
                    .setMultiChoiceItems(null,null,null)
                    .setPositiveButton("Ok",null)
                    .setCancelable(false)
                    .create().show();

        }
        if(v == okayCard){

        }
    }
}
