package com.app.risk.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.risk.R;
import com.app.risk.constants.GamePlayConstants;

import java.util.ArrayList;

/**
 * PlayerSelectionActivity display the player selection screen of the display
 * @author Himanshu Kohli
 * @version 1.0.0
 */
public class PlayerSelectionActivity extends AppCompatActivity {

    private String mapInfo = "";
    private ListView listView;
    private TextView playerDisplay;
    private SeekBar seekBar;
    private FloatingActionButton nextButton;
    private ArrayList<String> playerNames, playerStratergies;
    private String playerName = "", playerStratergy = "";
    private Spinner stratergySelectionSpinner;
    private String[] stratergyHolderArray = {
            GamePlayConstants.HUMAN_STRATEGY,
            GamePlayConstants.CHEATER_STRATEGY,
            GamePlayConstants.AGGRESSIVE_STRATEGY,
            GamePlayConstants.BENEVOLENT_STRATEGY,GamePlayConstants.RANDOM_STRATEGY
    };

    /**
     * This method is the main creation method of the activity
     * @param savedInstanceState: instance of the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_selection);

        setUpData();
        setUpNextButton();
        getMapInfo();
    }

    /**
     * This method gets the map information from the previous
     * activity using an intent
     */
    public void getMapInfo() {
        Intent intent = getIntent();
        mapInfo = intent.getStringExtra("MAP_NAME");
    }


    /**
     * This method initialize the elements of view and listeners
     * such as listview, seekbar and no. of players on display
     * activity using an intent
     */
    public void setUpData() {
        listView = findViewById(R.id.player_selection_listview);
        seekBar = findViewById(R.id.player_selection_seekbar);
        playerDisplay = findViewById(R.id.userselection_textview);

        playerNames = new ArrayList<>();
        playerStratergies = new ArrayList<>();

        playerNames.add("Player 1");
        playerNames.add("Player 2");

        playerStratergies.add(GamePlayConstants.HUMAN_STRATEGY);
        playerStratergies.add(GamePlayConstants.HUMAN_STRATEGY);

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, playerNames);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, View view, final int parentPosition, long id) {

                final View inflaterView = getLayoutInflater().inflate(R.layout.player_selection_option, null);
                new AlertDialog.Builder(PlayerSelectionActivity.this)
                        .setView(inflaterView)
                        .setTitle(R.string.player_name)
                        .setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        EditText editText = inflaterView.findViewById(R.id.player_selection_option_edittext);
                                        playerName = editText.getText().toString().trim();
                                        if (!playerName.equals("")) {
                                            playerNames.set(parentPosition, playerName);
                                            playerStratergies.remove(parentPosition);
                                            playerStratergies.add(parentPosition,playerStratergy);

                                            Log.i("Tester s"," " +playerStratergies);

                                            adapter.notifyDataSetChanged();
                                        }
                                    }
                                })
                        .create().show();
                EditText editText = inflaterView.findViewById(R.id.player_selection_option_edittext);
                editText.setText(playerNames.get(parentPosition));

                stratergySelectionSpinner = inflaterView.findViewById(R.id.player_selection_spinner);
                stratergySelectionSpinner.setAdapter(new ArrayAdapter<String>(PlayerSelectionActivity.this,android.R.layout.simple_spinner_dropdown_item,stratergyHolderArray));

                stratergySelectionSpinner.setSelection(getSelectionIndex(playerStratergies.get(parentPosition)));
                stratergySelectionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        playerStratergy = stratergyHolderArray[position];
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                playerDisplay.setText("" + progress);
                int display = Integer.parseInt(playerDisplay.getText().toString().trim());
                if (display > playerNames.size()) {
                    for (int i = playerNames.size(); i < display; i++) {
                        playerNames.add(getString(R.string.player_string) + " " +(i + 1));
                        playerStratergies.add(GamePlayConstants.HUMAN_STRATEGY);
                    }
                    adapter.notifyDataSetChanged();
                } else if (display < playerNames.size()) {
                    for (int i = playerNames.size(); i > display; i--) {
                        playerNames.remove(playerNames.size() - 1);
                        playerStratergies.remove(playerStratergies.size() - 1);
                    }
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    public int getSelectionIndex(String stringValue){
        for(int i= 0;i < stratergyHolderArray.length;i++){
            if(stringValue.equals(stratergyHolderArray[i])){
                return i;
            }
        }
        return 0;
    }


    /**
     * This method sets up the button which connects one activity to another
     * activity using an intent
     */

    public void setUpNextButton() {
        nextButton = findViewById(R.id.player_selection_next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(PlayerSelectionActivity.this)
                        .setTitle(R.string.alert_string)
                        .setMessage("Are You sure?")
                        .setNegativeButton("No", null)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(PlayerSelectionActivity.this, PlayScreenActivity.class);
                                intent.putStringArrayListExtra("PLAYER_INFO", playerNames);
                                intent.putStringArrayListExtra("STRATERGY_INFO",playerStratergies);
                                intent.putExtra("MAP_NAME", mapInfo);
                                intent.putExtra("PLAY_TYPE","SINGLE");
                                startActivity(intent);
                            }
                        }).create().show();
            }
        });
    }
}
