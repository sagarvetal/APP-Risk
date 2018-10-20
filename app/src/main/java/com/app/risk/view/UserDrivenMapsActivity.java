package com.app.risk.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import com.app.risk.R;
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
 * @author Akhila Chilukuri
 * @version 1.0.0
 */
public class UserDrivenMapsActivity extends AppCompatActivity implements View.OnClickListener {
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
    UserDrivenMapsActivity currentobj = null;
    CountryAdaptor countryListAdapter = null;
    ArrayAdapter<String> countryAdapter = null;
    boolean continentFlag = false;
    List<String> continentsList = null;
    List<String> presentcountryList = null;
    ArrayAdapter<String> continentAdapter = null;
    String currentContinent = "";
    HashMap<Continent, ArrayList<Country>> maps = new HashMap<Continent, ArrayList<Country>>();
    ArrayList<UserDrivenMapsActivity.Item> countryList = new ArrayList<UserDrivenMapsActivity.Item>();
    private  String fileName;
    Bundle sendBundle = new Bundle();
    Boolean editMode = false;

    /**
     * This method is invoked when the activity is created
     * This method initializes the item on the view with the ArrayAdapters.
     * sets the observer for each item on the View.
     * {@inheritDoc}
     *
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

        addCustomValue = (Button) findViewById(R.id.addCustomValue);
        addCustomValue.setOnClickListener(this);
        connectMap = (Button) findViewById(R.id.addNeighbours);
        connectMap.setOnClickListener(this);


        selectedCountryList = (ListView) findViewById(R.id.selectedCountries);
        countryListAdapter = new CountryAdaptor(this, countryList);
        selectedCountryList.setAdapter(countryListAdapter);
        selectedCountryList.setTextFilterEnabled(true);

        selectedCountryList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (countryList.get(i).isSection()) {

                } else {
                    Continent continent = null;
                    String continentName = null;
                    for (int j = 0; j < i; j++) {
                        if (countryList.get(j).isSection()) {
                            continentName = countryList.get(j).getTitle();
                        }
                    }
                    Country country = new Country(countryList.get(i).getTitle().toString());
                    presentcountryList.add(countryList.get(i).getTitle().toString());
                    countryList.remove(i);
                    continent = new Continent(continentName);
                    ArrayList<Country> presentCountry = maps.get(continent);
                    presentCountry.remove(country);
                    countryListAdapter.notifyDataSetChanged();
                    countryAdapter.notifyDataSetChanged();
                }
                return true;
            }

        });

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        editMode = bundle.getBoolean("edit Mode");
        if (editMode) {
            setTheListView(bundle);
            fileName = intent.getExtras().getString("fileName");

            intent.putExtra("fileName", fileName);
            sendBundle.putBoolean("isEditMode", true);
            sendBundle.putSerializable("arrGameData", intent.getSerializableExtra("arrGameData"));
        }
    }

    /**
     * This method is invoked in the edit mode of the Map
     * populates the values of the list with countries and continents in the Map
     *
     * @param bundle holds the key value pair of the object sent from the previous activity
     */
    private void setTheListView(Bundle bundle) {
        maps = (HashMap<Continent, ArrayList<Country>>) bundle.getSerializable("maps");
        Set<Continent> continentSet = maps.keySet();
        for (Continent c : continentSet) {
            ArrayList<Country> countries = maps.get(c);
            countryList.add(new SectionItem(c.getNameOfContinent()));
            for (Country country : countries) {
                countryList.add(new EntryItem(country.getNameOfCountry()));
                if (presentcountryList.contains(country.getNameOfCountry())) {
                    presentcountryList.remove(country.getNameOfCountry());
                }
            }
            if (!continentsList.contains(c.getNameOfContinent())) {
                continentsList.add(c.getNameOfContinent());
            }
        }
        countryListAdapter.notifyDataSetChanged();
        countryAdapter.notifyDataSetChanged();
        continentAdapter.notifyDataSetChanged();

    }

    /**
     * This method acts as a Listener for the buttons addCountry,addCustomValue,connect
     * {@inheritDoc}
     *
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

    /**
     * This method is invoked when addCountry button is pressed.
     * This adds the country,continent and continent value to the HashMap
     */
    public void addSelectedCountry() {
        if (continentSelected.trim().equalsIgnoreCase("")) {
            AlertDialog builder = new AlertDialog.Builder(UserDrivenMapsActivity.this).create();
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
            AlertDialog builder = new AlertDialog.Builder(UserDrivenMapsActivity.this).create();
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
            AlertDialog builder = new AlertDialog.Builder(UserDrivenMapsActivity.this).create();
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
                    AlertDialog builder = new AlertDialog.Builder(UserDrivenMapsActivity.this).create();
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
                for (UserDrivenMapsActivity.Item entry : countryList) {
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

    /**
     * This method is invoked when addCustomValue is pressed
     * This method allows the user to add custom country and continent value.
     */
    public void addUserCustomValue() {
        final View inflaterView = getLayoutInflater().inflate(R.layout.custom_values_layout, null);
        new AlertDialog.Builder(UserDrivenMapsActivity.this)
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
                        Toast.makeText(UserDrivenMapsActivity.this, "" + s + " is added", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .setView(inflaterView)
                .create().show();
    }

    /**
     * This method is invoked when connect button is pressed
     * This method allows the user to redirect to another activity where countries are connected.
     */
    public void connectCountries() {
        Intent userMapConnect = new Intent(UserDrivenMapsActivity.this, CreateMapActivity.class);
        userMapConnect.putExtra("maps", maps);

        if (editMode) {
            userMapConnect.putExtras(sendBundle);
            userMapConnect.putExtra("fileName",fileName);
        }
        startActivity(userMapConnect);
    }

    /**
     * custom item interface which represents the object in the list
     */
    public interface Item {
        /**
         * List item which is section
         */
        public boolean isSection();

        /**
         * Title of the List item
         */
        public String getTitle();
    }

    /**
     * class which manages the section of the list
     */
    public class SectionItem implements UserDrivenMapsActivity.Item {
        private final String title;

        /**
         * Contstructor to set the title of the list
         *
         * @param title of the Section of type string
         */
        public SectionItem(String title) {
            this.title = title;
        }

        /**
         * getter to get the list title
         *
         * @return title of the list
         */
        public String getTitle() {
            return title;
        }

        /**
         * checks whether the list item is a section
         *
         * @return true if the list is section else false is returned
         */
        public boolean isSection() {
            return true;
        }

        /**
         * computes the hashcode of the list item
         *
         * @return hashcode of the title
         */
        public int hashCode() {

            return title.hashCode();
        }

        /**
         * checks whether the two list are equal or not
         *
         * @return true if both are equal or return false
         */
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

    /**
     * class which manges the entry of the list
     */
    public class EntryItem implements UserDrivenMapsActivity.Item {
        public final String title;

        /**
         * Contructor to create the entry with given title
         *
         * @param title of the list
         */
        public EntryItem(String title) {
            this.title = title;
        }

        /**
         * getter to get the list title
         */
        public String getTitle() {
            return title;
        }

        /**
         * checks whether the list item is a section
         *
         * @return true if the list is section else false is returned
         */
        public boolean isSection() {
            return false;
        }

        /**
         * computes the hashcode of the list
         *
         * @return hashcode of the title
         */
        public int hashCode() {

            return title.hashCode();
        }

        /**
         * checks whether the two list are equal or not
         *
         * @return true if both are equal or return false
         */
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

    /**
     * Custom adapter to store the values of the items in the list view
     */
    public class CountryAdaptor extends BaseAdapter {

        private Context context;
        private ArrayList<UserDrivenMapsActivity.Item> item;
        private ArrayList<UserDrivenMapsActivity.Item> orignalItem;

        /**
         * non parameterized Contructor of Custom adapter to create its instance
         */
        public CountryAdaptor() {
            super();
        }

        /**
         * parameterized Contructor of Custom adapter to create its instance
         *
         * @param context of the Activity
         * @param item    ArrayList which stores the items in the list
         */
        public CountryAdaptor(Context context, ArrayList<UserDrivenMapsActivity.Item> item) {
            this.context = context;
            this.item = item;
        }

        /**
         * gets of the items in the list
         *
         * @return item size is returned
         */
        public int getCount() {
            return item.size();
        }

        /**
         * gets the item at the given postion
         *
         * @param position of item in the list is returned
         * @return Object of the itel is returned
         */
        public Object getItem(int position) {
            return item.get(position);
        }

        /**
         * gets the item ID at a given postion
         *
         * @param position of item in the list
         * @return Id of the item
         */
        public long getItemId(int position) {
            return position;
        }

        /**
         * puts the list of items on the view
         *
         * @param position    of the item
         * @param convertView view of the item
         * @param parent      of the item
         */
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (item.get(position).isSection()) {
                convertView = inflater.inflate(R.layout.layout_section, parent, false);
                TextView tvSectionTitle = (TextView) convertView.findViewById(R.id.tvSectionTitle);
                tvSectionTitle.setText(((UserDrivenMapsActivity.SectionItem) item.get(position)).getTitle());
            } else {
                convertView = inflater.inflate(R.layout.layout_item, parent, false);
                TextView tvItemTitle = (TextView) convertView.findViewById(R.id.tvItemTitle);
                tvItemTitle.setText(((UserDrivenMapsActivity.EntryItem) item.get(position)).getTitle());
            }
            return convertView;
        }
    }

}