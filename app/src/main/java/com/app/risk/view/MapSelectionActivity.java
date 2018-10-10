package com.app.risk.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.app.risk.R;

import java.util.ArrayList;

public class MapSelectionActivity extends AppCompatActivity {

    private ArrayList<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_selection);

        setUpDisplay();
    }



    public void setUpDisplay(){

        final ListView listView = findViewById(R.id.map_selection_listview);

        list = new ArrayList<>();
        for(int i=0;i<10;i++){
            list.add("Map " + i);
        }
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                new AlertDialog.Builder(MapSelectionActivity.this)
                        .setMessage("Load Map")
                        .setNegativeButton("No",null)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(MapSelectionActivity.this, "" + list.get(position), Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(MapSelectionActivity.this,PlayerSelectionActivity.class);
                                intent.putExtra("MAP_INFO",list.get(position));
                                startActivity(intent);
                            }
                        }).create().show();

               }
        });
    }

}
