package com.app.risk.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.app.risk.R;
import com.app.risk.model.Continent;
import com.app.risk.model.Country;
import com.app.risk.model.GameMap;
import com.app.risk.utility.MapReader;
import com.app.risk.view.UserDrivenMaps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EditMap extends AppCompatActivity {
    Intent intent ;
    private ListView listView;
    HashMap<Continent, ArrayList<Country>> maps = new HashMap<Continent, ArrayList<Country>>();
    ArrayList<String> mapList=null;
    ArrayList<GameMap> listOfGameMap=new ArrayList<GameMap>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_map_layout);
        /*Continent asia=new Continent("Asia",1);
        ArrayList<Country> FirstContinent=new ArrayList<Country>();
        FirstContinent.add(new Country("India",asia));
        FirstContinent.add(new Country("Malayasia",asia));
        FirstContinent.add(new Country("SriLanka",asia));
        Continent Africa=new Continent("Africa",1);
        ArrayList<Country> SecondContinent=new ArrayList<Country>();
        SecondContinent.add(new Country("gana",Africa));
        SecondContinent.add(new Country("peru",Africa));
        SecondContinent.add(new Country("mexico",Africa));
        maps.put(asia,FirstContinent);
        maps.put(Africa,SecondContinent);*/
      /*  intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*//*");
        startActivityForResult(intent, 7);*/
        listView = findViewById(R.id.edit_map_listview);
        mapList=MapReader.getMapList(getApplicationContext());
        // final ArrayList<String> sample = new ArrayList();
       /*
        for (int i=0;i<20;i++){
            sample.add("sample " + i);
        }*/
        listView.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,mapList));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String fileName=mapList.get(i);
                List<GameMap> listOfGameMapList=MapReader.returnGameMapFromFile(getApplicationContext(),fileName);
                listOfGameMap.addAll(listOfGameMapList);
                maps=convertIntoHashMap(listOfGameMap);
                Intent editMap = new Intent(EditMap.this, UserDrivenMaps.class);
                Bundle bundle = new Bundle();
                bundle.putBoolean("edit Mode",true);
                bundle.putSerializable("maps",maps);
                bundle.putSerializable("arrGameData",listOfGameMap);
                editMap.putExtras(bundle);
                startActivity(editMap);

                // Toast.makeText(EditMap.this, "" + sample.get(i), Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    protected void  onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub

        switch(requestCode){

            case 7:
                if(resultCode==RESULT_OK){

                    String PathHolder = data.getData().getPath();
                    //ReadGameMapFromFile readGameMap=new ReadGameMapFromFile();

                    //  List<GameMap> listOfGameMap=readGameMap.readGameMapFromFile(getApplicationContext(),PathHolder);
                    //  maps=convertIntoHashMap(listOfGameMap);

                    System.out.println("::::::::::::::::::::::my path:::::::::::::::::"+PathHolder);

                    List<GameMap> listOfGameMapList=MapReader.returnGameMapFromFile(getApplicationContext(),PathHolder);
                    System.out.println("::::::::::::::::::GAME MAP LIST::::::::::::::"+MapReader.getMapList(getApplicationContext()));
                    System.out.println(":::::::::::::::::::::GAME LIST SIZE::::::::::::::::::::::::::"+listOfGameMapList.size());
                    listOfGameMap.addAll(listOfGameMapList);
                    System.out.println(":::::::::::::::::::::GAME LIST SIZE::::::::::::::::::::::::::"+listOfGameMap.size());
                    maps=convertIntoHashMap(listOfGameMap);
                    Intent editMap = new Intent(EditMap.this, UserDrivenMaps.class);
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("edit Mode",true);
                    bundle.putSerializable("maps",maps);
                    bundle.putSerializable("arrGameData",listOfGameMap);
                    editMap.putExtras(bundle);
                    startActivity(editMap);


                }
                break;

        }
    }

    private HashMap<Continent, ArrayList<Country>> convertIntoHashMap(List<GameMap> listOfGameMap) {
        HashMap<Continent, ArrayList<Country>> userMaps = new HashMap<Continent, ArrayList<Country>>();
        for(GameMap gm:listOfGameMap)
        {
            Country country=gm.getFromCountry();
            Continent continent=country.getBelongsToContinent();
            if(userMaps.containsKey(continent))
            {
                ArrayList<Country> userCountry=userMaps.get(continent);
                userCountry.add(country);
            }
            else
            {
                ArrayList<Country> newUserMap=new ArrayList<Country>();
                newUserMap.add(country);
                userMaps.put(continent,newUserMap);
            }

        }
        return userMaps;
    }
}
