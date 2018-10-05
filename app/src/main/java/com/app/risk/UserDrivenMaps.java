package com.app.risk;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.app.risk.model.Continent;
import com.app.risk.model.Country;

import java.util.ArrayList;
import java.util.HashMap;

public class UserDrivenMaps extends AppCompatActivity {
String continentSelected="";
String countrySelected="";
int continentValueSelected;
Spinner continent=null;
Spinner country=null;
EditText continentValue=null;
Button addCountry=null;
HashMap<Continent,ArrayList<Country>> maps=new HashMap<Continent,ArrayList<Country>>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_driven_maps);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        continent=(Spinner)findViewById(R.id.continent);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.continent, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        continent.setAdapter(adapter);

        continent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                continentSelected=continent.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {


            }
        });

        country=(Spinner)findViewById(R.id.country);

        country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                countrySelected=country.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {


            }
        });



        addCountry=(Button)findViewById(R.id.addCountry);
        addCountry.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                continentValue=(EditText)findViewById(R.id.continentValue);
                continentValueSelected = Integer.parseInt(continentValue.getText().toString());
                if(maps!=null&&maps.containsKey(new Continent(continentSelected)))
                {
               // Country country=new Country();
                }

            }

        });

    }

}
