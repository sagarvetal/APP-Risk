package com.app.risk.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.app.risk.R;
import com.app.risk.controller.MapDriverController;
import com.app.risk.model.Continent;
import com.app.risk.model.Country;
import com.app.risk.model.GameMap;
import com.app.risk.utility.MapReader;
import com.app.risk.view.UserDrivenMapsActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * MainScreenActivity display the main screen of the display
 *
 * @author Akhila Chilukuri
 * @version 1.0.0
 */
public class EditMap extends AppCompatActivity {
    /**
     * listView holds the list of all the countries and continents to be displayed.
     */
    private ListView listView;
    /**
     * maps stores the list of countries for each continent in hashmap.
     */
    HashMap<Continent, ArrayList<Country>> maps = new HashMap<Continent, ArrayList<Country>>();

    /**
    * The instance of MapReader to read the map.
    */
    MapReader mapReader = new MapReader();

    /**
     * mapList stores content fo the map read.
     */
    ArrayList<String> mapList = null;
  
    /**
     * listOfGameMap stores the map information in the arraylist of the game map object
     */
    ArrayList<GameMap> listOfGameMap = new ArrayList<GameMap>();
  
    /**
     * {@inheritDoc}
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_map_layout);
        listView = findViewById(R.id.edit_map_listview);

        mapList= mapReader.getMapList(getApplicationContext());
        listView.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, mapList));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listOfGameMap = new ArrayList<>();

                String fileName = mapList.get(i);
                List<GameMap> listOfGameMapList = MapDriverController.getInstance().readmap(getApplicationContext(),fileName);

                listOfGameMap.addAll(listOfGameMapList);
                maps = convertIntoHashMap(listOfGameMap);
                Intent editMap = new Intent(EditMap.this, UserDrivenMapsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putBoolean("edit Mode", true);
                bundle.putSerializable("maps", maps);
                bundle.putSerializable("arrGameData", listOfGameMap);
                editMap.putExtra("fileName", fileName);
                editMap.putExtras(bundle);
                startActivity(editMap);
            }
        });


    }


    /**
     * {@inheritDoc}
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub

        switch (requestCode) {

            case 7:
                if (resultCode == RESULT_OK) {

                    String PathHolder = data.getData().getPath();

                    System.out.println("::::::::::::::::::::::my path:::::::::::::::::" + PathHolder);

                    List<GameMap> listOfGameMapList = mapReader.returnGameMapFromFile(getApplicationContext(),PathHolder);
                    System.out.println("::::::::::::::::::GAME MAP LIST::::::::::::::" + (new MapReader()).getMapList(getApplicationContext()));
                    System.out.println(":::::::::::::::::::::GAME LIST SIZE::::::::::::::::::::::::::" + listOfGameMapList.size());

                    listOfGameMap.addAll(listOfGameMapList);
                    System.out.println(":::::::::::::::::::::GAME LIST SIZE::::::::::::::::::::::::::" + listOfGameMap.size());
                    maps = convertIntoHashMap(listOfGameMap);
                    Intent editMap = new Intent(EditMap.this, UserDrivenMapsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("edit Mode", true);
                    bundle.putSerializable("maps", maps);
                    bundle.putSerializable("arrGameData", listOfGameMap);

                    editMap.putExtras(bundle);
                    startActivity(editMap);


                }
                break;

        }
    }

    /**
     * This method convert the gamemap arraylist to hashmap
     *
     * @param listOfGameMap list of gamemap
     * @return hashmap of the gamemap list
     */
    private HashMap<Continent, ArrayList<Country>> convertIntoHashMap(List<GameMap> listOfGameMap) {
        HashMap<Continent, ArrayList<Country>> userMaps = new HashMap<Continent, ArrayList<Country>>();
        for (GameMap gm : listOfGameMap) {
            Country country = gm.getFromCountry();
            Continent continent = country.getBelongsToContinent();
            if (userMaps.containsKey(continent)) {
                ArrayList<Country> userCountry = userMaps.get(continent);
                userCountry.add(country);
            } else {
                ArrayList<Country> newUserMap = new ArrayList<Country>();
                newUserMap.add(country);
                userMaps.put(continent, newUserMap);
            }

        }
        return userMaps;
    }
}
