package com.app.risk;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.risk.model.Continent;
import com.app.risk.model.Country;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static java.sql.DriverManager.println;
/**
 * This Activity class takes the user given country,continent
 * and continent value .Shows the user the list of countries,continent
 * selected by the user and binds them into a single hashMap
 * and sends it another activity where it connects the countries
 *
 * @author      Akhila Chilukuri
 * @version     1.0.0
 */
public class UserDrivenMaps extends AppCompatActivity implements View.OnClickListener {
    String continentSelected = "";
    String countrySelected = "";
    int continentValueSelected;
    Spinner continent = null;
    Spinner country = null;
    EditText continentValue = null;
    Button addCountry = null;
    Button connectMap = null;
    Button addCustomValue = null;
    ListView selectedCountryList = null;
    UserDrivenMaps currentobj = null;
    CountryAdaptor countryListAdapter = null;
    ArrayAdapter<String> countryAdapter = null;
    boolean continentFlag = false;
    List<String> continentsList = null;
    List<String> presentcountryList = null;
    ArrayAdapter<String> continentAdapter = null;
    String currentContinent = "";
    HashMap<Continent, ArrayList<Country>> maps = new HashMap<Continent, ArrayList<Country>>();
    ArrayList<UserDrivenMaps.Item> countryList = new ArrayList<UserDrivenMaps.Item>();

    /**
     * This method is invoked when the activity is created
     * This method initializes the item on the view with the ArrayAdapters.
     * sets the observer for each item on the View.
     * {@inheritDoc}
     * @param savedInstanceState saves the current instance
     */
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_driven_maps);
        continent = (Spinner) findViewById(R.id.continent);
        continentsList = new ArrayList(Arrays.asList(getResources().getStringArray(R.array.continent)));
        continentAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, continentsList);
        continentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        continent.setAdapter(continentAdapter);
        continent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                continentValue.setText("");
                continentValue.setFocusable(true);
                continentSelected = continent.getSelectedItem().toString();
                Set<Continent> listOfContinents = maps.keySet();
                boolean found = false;
                for (Continent recordContinent : listOfContinents) {
                    if (continentSelected.toString().trim().equalsIgnoreCase(recordContinent.getNameOfContinent())) {
                        continentValue.setText(Integer.toString(recordContinent.getArmyControlValue()));
                        found = true;
                        continentValue.setFocusable(false);
                    }
                }
                if (!found) {
                    continentValue.setFocusable(true);
                    continentValue.setFocusableInTouchMode(true);
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {


            }
        });

        country = (Spinner) findViewById(R.id.country);
        presentcountryList = new ArrayList(Arrays.asList(getResources().getStringArray(R.array.country)));
        countryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, presentcountryList);
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        country.setAdapter(countryAdapter);
        country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                countrySelected = country.getSelectedItem().toString();
            }

            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        continentValue = (EditText) findViewById(R.id.continentValue);
        continentValue.clearFocus();

        addCountry = (Button) findViewById(R.id.addCountry);
        addCountry.setOnClickListener(this);
        selectedCountryList = (ListView) findViewById(R.id.selectedCountries);
        countryListAdapter = new CountryAdaptor(this, countryList);
        selectedCountryList.setAdapter(countryListAdapter);
        selectedCountryList.setTextFilterEnabled(true);

        addCustomValue = (Button) findViewById(R.id.addCustomValue);
        addCustomValue.setOnClickListener(this);
        connectMap = (Button) findViewById(R.id.addNeighbours);
        connectMap.setOnClickListener(this);
    }
    /**
     * This method acts as a Listener for the buttons addCountry,addCustomValue,connect
     * {@inheritDoc}
     * @param v The view on which the click is done, that object of the view is called.
     */
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addCountry:
                addSelectedCountry();
                break;
            case R.id.addCustomValue:
                addUserCustomValue();
                break;
            case R.id.addNeighbours:
                connectCountries();
                break;
        }
    }
  
    public void addSelectedCountry() {
        if (continentSelected.trim().equalsIgnoreCase("")) {
            AlertDialog builder = new AlertDialog.Builder(UserDrivenMaps.this).create();
            builder.setTitle("Continent");
            builder.setMessage("Please Enter Continent.");
            builder.setButton(AlertDialog.BUTTON_NEUTRAL, "ok",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
            builder.show();
        }
        if (countrySelected.equalsIgnoreCase("")) {
            AlertDialog builder = new AlertDialog.Builder(UserDrivenMaps.this).create();
            builder.setTitle("Country");
            builder.setMessage("Please Enter Country.");
            builder.setButton(AlertDialog.BUTTON_NEUTRAL, "ok",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
            builder.show();
        }
        if (continentValue.getText().toString().trim().equalsIgnoreCase("") || continentValue.getText().toString().trim().length() == 0) {
            println("hello:::::::::::::::::::::::::::::::::in dialog box");
            AlertDialog builder = new AlertDialog.Builder(UserDrivenMaps.this).create();
            builder.setTitle("Continent Value");
            builder.setMessage("Please Enter Continent Value.");
            builder.setButton(AlertDialog.BUTTON_NEUTRAL, "ok",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
            builder.show();
        }
        println("hello:::::::::::::::::::::::::::::::::after dialog box");
        continentValueSelected = Integer.parseInt(continentValue.getText().toString());
        for (Continent checkContinent : maps.keySet()) {
            ArrayList<Country> checkCountry = maps.get(checkContinent);
            for (Country eachCountry : checkCountry) {
                if (eachCountry.getNameOfCountry().equalsIgnoreCase(countrySelected)) {
                    AlertDialog builder = new AlertDialog.Builder(UserDrivenMaps.this).create();
                    builder.setTitle("Country");
                    builder.setMessage("Country is already Selected");
                    builder.setButton(AlertDialog.BUTTON_NEUTRAL, "ok",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });
                    builder.show();
                }
            }
        }
        if (maps != null && maps.containsKey(new Continent(continentSelected))) {
            Country addNewCountry = new Country(countrySelected, new Continent(continentSelected, continentValueSelected));//check this place
            maps.get(new Continent(continentSelected, continentValueSelected)).add(addNewCountry);
            int countryPosition = countryAdapter.getPosition(countrySelected);
            int countryCount = countryAdapter.getCount();
            presentcountryList.remove(countrySelected);
            if (continentSelected.equalsIgnoreCase(currentContinent)) {
                countryList.add(new EntryItem(countrySelected));
                countryListAdapter.notifyDataSetChanged();
                if (countryCount - 1 == countryPosition)
                    country.setSelection(countryPosition - 1);
                else
                    country.setSelection(countryPosition);
                countrySelected = country.getSelectedItem().toString();
            } else {
                System.out.println("::::::::::::::::::::::::::resultof contains:::::::::::::::::" + countryList.contains(new SectionItem(continentSelected)));
                int i = 0;
                for (UserDrivenMaps.Item entry : countryList) {
                    if (entry instanceof SectionItem) {
                        if (((SectionItem) entry).getTitle().equalsIgnoreCase(continentSelected)) {
                            break;
                        }
                    }
                    i = i + 1;
                }
                countryList.add(i + 1, new EntryItem(countrySelected));
                countryListAdapter.notifyDataSetChanged();
                if (countryCount - 1 == countryPosition)
                    country.setSelection(countryPosition - 1);
                else
                    country.setSelection(countryPosition);
                countrySelected = country.getSelectedItem().toString();
                int position = countryList.lastIndexOf(new SectionItem(continentSelected));
                System.out.println("::::::::::::::::::::::::::index of continent:::::::::::::::::" + position);

            }
            countryAdapter.notifyDataSetChanged();
            continentValue.setFocusable(false);
        } else {
            Country addNewCountry = new Country(countrySelected, new Continent(continentSelected, continentValueSelected));
            ArrayList<Country> adjacentCountry = new ArrayList<Country>();
            int countryPosition = countryAdapter.getPosition(countrySelected);
            int countryCount = countryAdapter.getCount();
            adjacentCountry.add(addNewCountry);
            maps.put(new Continent(continentSelected, continentValueSelected), adjacentCountry);
            presentcountryList.remove(countrySelected.trim().toString());
            countryList.add(new SectionItem(continentSelected));
            countryList.add(new EntryItem(countrySelected));
            currentContinent = continentSelected;
            countryAdapter.notifyDataSetChanged();
            countryListAdapter.notifyDataSetChanged();
            if (countryCount - 1 == countryPosition)
                country.setSelection(countryPosition - 1);
            else
                country.setSelection(countryPosition);
            countrySelected = country.getSelectedItem().toString();
            continentValue.setFocusable(false);
        }

    }

    public void addUserCustomValue() {
        final View inflaterView = getLayoutInflater().inflate(R.layout.custom_values_layout, null);
        new AlertDialog.Builder(UserDrivenMaps.this)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        RadioGroup radioGroup = inflaterView.findViewById(R.id.custom_value_radiogroup);
                        int radioButtonID = radioGroup.getCheckedRadioButtonId();

                        if (radioButtonID == R.id.ContinentRadio) {
                            continentFlag = true;
                        } else if (radioButtonID == R.id.CountryRadio) {
                            continentFlag = false;
                        }

                        EditText editText = inflaterView.findViewById(R.id.custom_value_edittext);
                        String s = editText.getText().toString().trim();

                        if (continentFlag) {
                            continentsList.add(s.trim().toString());
                            continentAdapter.notifyDataSetChanged();
                        } else {
                            presentcountryList.add(s.trim().toString());
                            countryAdapter.notifyDataSetChanged();
                        }
                        Toast.makeText(UserDrivenMaps.this, "" + s + " is added", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .setView(inflaterView)
                .create().show();
    }

    public void connectCountries() {
        Intent userMapConnect = new Intent(UserDrivenMaps.this, CreateMapActivity.class);
        userMapConnect.putExtra("maps", maps);
        startActivity(userMapConnect);
    }

    public interface Item {
        public boolean isSection();

        public String getTitle();
    }


    public class SectionItem implements UserDrivenMaps.Item {
        private final String title;

        public SectionItem(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

        @Override
        public boolean isSection() {
            return true;
        }

        public int hashCode() {

            return title.hashCode();
        }

        public boolean equals(Object obj) {
            if (obj instanceof SectionItem) {
                if (this.title.equalsIgnoreCase(((SectionItem) obj).title)) ;
                {
                    return true;
                }
            }
            return false;
        }


    }

    public class EntryItem implements UserDrivenMaps.Item {
        public final String title;

        public EntryItem(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

        @Override
        public boolean isSection() {
            return false;
        }

        public int hashCode() {

            return title.hashCode();
        }

        public boolean equals(Object obj) {
            if (obj instanceof EntryItem) {
                if (this.title.equalsIgnoreCase(((EntryItem) obj).title)) ;
                {
                    return true;
                }
            }
            return false;
        }
    }

    public class CountryAdaptor extends BaseAdapter {

        private Context context;
        private ArrayList<UserDrivenMaps.Item> item;
        private ArrayList<UserDrivenMaps.Item> orignalItem;

        public CountryAdaptor() {
            super();
        }

        public CountryAdaptor(Context context, ArrayList<UserDrivenMaps.Item> item) {
            this.context = context;
            this.item = item;
        }

        @Override
        public int getCount() {
            return item.size();
        }

        @Override
        public Object getItem(int position) {
            return item.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (item.get(position).isSection()) {
                convertView = inflater.inflate(R.layout.layout_section, parent, false);
                TextView tvSectionTitle = (TextView) convertView.findViewById(R.id.tvSectionTitle);
                tvSectionTitle.setText(((UserDrivenMaps.SectionItem) item.get(position)).getTitle());
            } else {
                convertView = inflater.inflate(R.layout.layout_item, parent, false);
                TextView tvItemTitle = (TextView) convertView.findViewById(R.id.tvItemTitle);
                tvItemTitle.setText(((UserDrivenMaps.EntryItem) item.get(position)).getTitle());
            }
            return convertView;
        }
    }

}
