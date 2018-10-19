package com.app.risk.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.risk.R;

import java.util.ArrayList;

public class PlayerSelectionActivity extends AppCompatActivity {

    private String mapInfo = "";
    private ListView listView;
    private TextView playerDisplay;
    private SeekBar seekBar;
    private FloatingActionButton nextButton;
    private ArrayList<String> playerNames;
    private String playerName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_selection);

        setUpData();
        setUpNextButton();
        getMapInfo();
    }

    /*
     * This method gets the map information from the previous
     * activity using an intent
     */
    public void getMapInfo() {
        Intent intent = getIntent();
        mapInfo = intent.getStringExtra("MAP_NAME");
        Toast.makeText(this, "" + mapInfo, Toast.LENGTH_SHORT).show();
    }


    /*
     * This method initialize the elements of the view
     * such as listview, seekbar and no. of players on display
     * activity using an intent
     */
    public void setUpData() {
        listView = findViewById(R.id.player_selection_listview);
        seekBar = findViewById(R.id.player_selection_seekbar);
        playerDisplay = findViewById(R.id.userselection_textview);

        playerNames = new ArrayList<>();

        playerNames.add("Player 1");
        playerNames.add("Player 2");

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, playerNames);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                final View inflaterView = getLayoutInflater().inflate(R.layout.player_selection_option, null);
                new AlertDialog.Builder(PlayerSelectionActivity.this)
                        .setView(inflaterView)
                        .setTitle("Player Name")
                        .setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        EditText editText = inflaterView.findViewById(R.id.player_selection_option_edittext);
                                        playerName = editText.getText().toString().trim();
                                        if (!playerName.equals("")) {
                                            playerNames.set(position, playerName);
                                            adapter.notifyDataSetChanged();
                                        }
                                    }
                                })
                        .create().show();

                EditText editText = inflaterView.findViewById(R.id.player_selection_option_edittext);
                editText.setText(playerNames.get(position));
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                playerDisplay.setText("" + progress);
                int display = Integer.parseInt(playerDisplay.getText().toString().trim());
                if (display > playerNames.size()) {
                    for (int i = playerNames.size(); i < display; i++) {
                        playerNames.add("Player " + (i + 1));
                    }
                    adapter.notifyDataSetChanged();
                } else if (display < playerNames.size()) {
                    for (int i = playerNames.size(); i > display; i--) {
                        playerNames.remove(playerNames.size() - 1);
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

    /*
     * This method sets up the button which connects one activity to another
     * activity using an intent
     */

    public void setUpNextButton() {
        nextButton = findViewById(R.id.player_selection_next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(PlayerSelectionActivity.this)
                        .setTitle("Alert")
                        .setMessage("Are You sure?")
                        .setNegativeButton("No", null)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(PlayerSelectionActivity.this, PlayScreenActivity.class);
                                intent.putStringArrayListExtra("PLAYER_INFO", playerNames);
                                intent.putExtra("MAP_NAME", mapInfo);
                                startActivity(intent);
                            }
                        }).create().show();
            }
        });
    }
}
