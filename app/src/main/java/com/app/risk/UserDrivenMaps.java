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

public class UserDrivenMaps extends AppCompatActivity {
    String continentSelected = "";
    String countrySelected = "";
    int continentValueSelected;
    Spinner continent = null;
    Spinner country = null;
    EditText continentValue = null;
    Button addCountry = null;
    Button connectMap=null;
    Button addCustomValue = null;
    ListView selectedCountryList = null;
    UserDrivenMaps currentobj = null;
    CountryAdaptor countryListAdapter=null;
    ArrayAdapter<String> countryAdapter=null;
    boolean continentFlag=false;
    List<String> continentsList=null;
    List<String> presentcountryList=null;
    ArrayAdapter<String> continentAdapter=null;
    String currentContinent="";
    // AlertDialog builder = null;
    HashMap<Continent, ArrayList<Country>> maps = new HashMap<Continent, ArrayList<Country>>();
    ArrayList<UserDrivenMaps.Item> countryList = new ArrayList<UserDrivenMaps.Item>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        currentobj = this;
        //   builder=new AlertDialog.Builder(this).create();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_driven_maps);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        continent = (Spinner) findViewById(R.id.continent);
        continentsList = new ArrayList(Arrays.asList(getResources().getStringArray(R.array.continent)));
       /* ArrayAdapter<CharSequence> continentAdapter = ArrayAdapter.createFromResource(this,
                R.array.continent, android.R.layout.simple_spinner_item);*/
        continentAdapter = new ArrayAdapter<String> (this,
                android.R.layout.simple_spinner_item,continentsList);
        continentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        continent.setAdapter(continentAdapter);

        continent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                continentValue.setText("");
                continentValue.setFocusable(true);
                continentSelected = continent.getSelectedItem().toString();
                Set<Continent> listOfContinents = maps.keySet();
                boolean found=false;
                for(Continent recordContinent:listOfContinents) {
                    if (continentSelected.toString().trim().equalsIgnoreCase(recordContinent.getNameOfContinent())) {
                        continentValue.setText(Integer.toString(recordContinent.getArmyControlValue()));
                        // Toast.makeText(UserDrivenMaps.this, "asia", Toast.LENGTH_SHORT).show();
                        found=true;
                        continentValue.setFocusable(false);
                    }
                }
                if(!found)
                {

                    continentValue.setFocusable(true);
                    continentValue.setFocusableInTouchMode(true);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {


            }
        });

        country = (Spinner) findViewById(R.id.country);
        presentcountryList = new ArrayList(Arrays.asList(getResources().getStringArray(R.array.country)));
        countryAdapter = new ArrayAdapter<String> (this,
                android.R.layout.simple_spinner_item,presentcountryList);
       /* countryAdapter = ArrayAdapter.createFromResource(this,
                R.array.country, android.R.layout.simple_spinner_item);*/
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        country.setAdapter(countryAdapter);
        country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                countrySelected = country.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {


            }
        });

        continentValue = (EditText) findViewById(R.id.continentValue);
        continentValue.clearFocus();
        //continentValue.setShowSoftInputOnFocus(true);

       /* continentValue.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                continentValue.post(new Runnable() {
                    @Override
                    public void run() {
                        InputMethodManager imm = (InputMethodManager) UserDrivenMaps.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(continentValue, InputMethodManager.SHOW_IMPLICIT);
                    }
                });
            }
        });*/
        // continentValue.requestFocus();


        selectedCountryList = (ListView) findViewById(R.id.selectedCountries);


        countryListAdapter = new CountryAdaptor(this, countryList);
        selectedCountryList.setAdapter(countryListAdapter);
        selectedCountryList.setTextFilterEnabled(true);

        addCountry = (Button) findViewById(R.id.addCountry);
        addCountry.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (continentSelected.trim().equalsIgnoreCase("")) {
                    AlertDialog builder = new AlertDialog.Builder(UserDrivenMaps.this).create();
                    builder.setTitle("Continent");
                    builder.setMessage("Please Enter Continent.");
                    builder.setButton(AlertDialog.BUTTON_NEUTRAL, "ok",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //UserDrivenMaps.th.dimiss();
                                }
                            });
                    // AlertDialog dialog = builder.create();
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
                                    // builder.dismiss();
                                }
                            });
                    // AlertDialog dialog = builder.create();
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
                                    //UserDrivenMaps.th.dimiss();
                                }
                            });
                    // AlertDialog dialog = builder.create();
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
                                            //builder.dismiss();
                                        }
                                    });
                            // AlertDialog dialog = builder.create();
                            builder.show();
                        }
                    }
                }
                if (maps != null && maps.containsKey(new Continent(continentSelected))) {
                    Country addNewCountry = new Country(countrySelected, new Continent(continentSelected, continentValueSelected));//check this place
                    maps.get(new Continent(continentSelected, continentValueSelected)).add(addNewCountry);
                    int countryPosition=countryAdapter.getPosition(countrySelected);
                    presentcountryList.remove(countrySelected);
                    //countryAdapter.remove(countrySelected);
                    if(continentSelected.equalsIgnoreCase(currentContinent)) {
                        countryList.add(new EntryItem(countrySelected));
                        countryListAdapter.notifyDataSetChanged();
                        country.setSelection(countryPosition);
                        countrySelected=country.getSelectedItem().toString();
                    }
                    else
                    {
                        System.out.println("::::::::::::::::::::::::::resultof contains:::::::::::::::::"+countryList.contains(new SectionItem(continentSelected)));
                        int i=0;
                        for(UserDrivenMaps.Item entry:countryList)
                        {
                            if(entry instanceof SectionItem)
                            {
                                if(((SectionItem)entry).getTitle().equalsIgnoreCase(continentSelected))
                                {
                                    break;
                                }
                            }
                            i=i+1;
                        }
                        countryList.add(i+1,new EntryItem(countrySelected));
                        countryListAdapter.notifyDataSetChanged();
                        country.setSelection(countryPosition);
                        countrySelected=country.getSelectedItem().toString();
                        int position=countryList.lastIndexOf(new SectionItem(continentSelected));
                        System.out.println("::::::::::::::::::::::::::index of continent:::::::::::::::::"+position);

                    }
                    countryAdapter.notifyDataSetChanged();
                    continentValue.setFocusable(false);
                    //countryList.notify();
                    // countryListAdapter.notifyDataSetChanged();
                } else {
                    Country addNewCountry = new Country(countrySelected, new Continent(continentSelected, continentValueSelected));
                    ArrayList<Country> adjacentCountry = new ArrayList<Country>();
                    int countryPosition=countryAdapter.getPosition(countrySelected);
                    adjacentCountry.add(addNewCountry);
                    maps.put(new Continent(continentSelected, continentValueSelected), adjacentCountry);
                    presentcountryList.remove(countrySelected.trim().toString());
                    countryList.add(new SectionItem(continentSelected));
                    //countryAdapter.remove(countrySelected);
                    countryList.add(new EntryItem(countrySelected));
                    currentContinent=continentSelected;
                    //countryList.notify();
                    countryAdapter.notifyDataSetChanged();
                    countryListAdapter.notifyDataSetChanged();
                    country.setSelection(countryPosition);
                    countrySelected=country.getSelectedItem().toString();
                    continentValue.setFocusable(false);
                }


            }

        });
        addCustomValue = (Button) findViewById(R.id.addCustomValue);
        addCustomValue.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view)
            {
//                new AlertDialog.Builder( SweetAlertDialog.NORMAL_TYPE))
//                        .setTitleText("Map Editor")
//                        .setContentText("Select Option")
//                        .setConfirmText("Create Map")
//                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                            @Override
//                            public void onClick(SweetAlertDialog sDialog) {
//                                sDialog.dismissWithAnimation();
//                                Intent intent = new Intent(getActivity(), MapEditorActivity.class);
//                                getActivity().startActivity(intent);
//                            }
//                        })
//                        .setCancelText("Load Map")
//                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                            @Override
//                            public void onClick(SweetAlertDialog sweetAlertDialog) {
//                                sweetAlertDialog.dismissWithAnimation();
//                               // Bundle bundle = new Bundle();
//                                //bundle.putString(SyncStateContract.Constants.KEY_FROM, Constants.VALUE_FROM_EDIT_MAP);
//                                Intent intent = new Intent(getActivity(), MapSelectionAndInitializationActivity.class);
//                                //intent.putExtras(bundle);
//                                getActivity().startActivity(intent);
//                            }
//                        })
//                        .show();

                final View inflaterView = getLayoutInflater().inflate(R.layout.custom_values_layout,null);
                new AlertDialog.Builder(UserDrivenMaps.this)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                RadioGroup radioGroup = inflaterView.findViewById(R.id.custom_value_radiogroup);
                                      /*  radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                            @Override
                                            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                                                Toast.makeText(UserDrivenMaps.this, "" + i+" is Checked", Toast.LENGTH_SHORT).show();
                                                if(i==R.id.ContinentRadio)
                                                {
                                                    continentFlag=true;
                                                } else if(i==R.id.CountryRadio)
                                                {
                                                    continentFlag=false;
                                                }
                                                //Toast.makeText(UserDrivenMaps.this, "" + i, Toast.LENGTH_SHORT).show();
                                            }
                                        });*/
                                int radioButtonID = radioGroup.getCheckedRadioButtonId();

                                if(radioButtonID==R.id.ContinentRadio)
                                {
                                    continentFlag=true;
                                } else if(radioButtonID==R.id.CountryRadio)
                                {
                                    continentFlag=false;
                                }

                                EditText editText = inflaterView.findViewById(R.id.custom_value_edittext);
                                String s = editText.getText().toString().trim();

                                if(continentFlag)
                                {
                                    continentsList.add(s.trim().toString());
                                    continentAdapter.notifyDataSetChanged();
                                }
                                else
                                {
                                    presentcountryList.add(s.trim().toString());
                                    countryAdapter.notifyDataSetChanged();
                                }
                                Toast.makeText(UserDrivenMaps.this, "" + s+" is added", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Cancel",null)
                        .setView(inflaterView)
                        .create().show();


            }
        });


        connectMap = (Button) findViewById(R.id.addNeighbours);
        connectMap.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                Intent userMapConnect = new Intent(UserDrivenMaps.this, CreateMapActivity.class);
                userMapConnect.putExtra("maps", maps);
                startActivity(userMapConnect);
            }
        });
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
