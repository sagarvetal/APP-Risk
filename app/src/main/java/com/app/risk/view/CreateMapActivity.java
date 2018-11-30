package com.app.risk.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.app.risk.R;
import com.app.risk.controller.MapDriverController;
import com.app.risk.model.Continent;
import com.app.risk.model.Country;
import com.app.risk.model.GameMap;
import com.app.risk.adapters.CountryAdaptor;
import com.app.risk.utility.MapVerification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Creates and Edits  Map and saves map after verification
 */

public class CreateMapActivity extends Activity {

    /**
     * Radius of circle
     */
    public static final int RADIUS = 65;

    /**
     * Checks if is edit mode true
     */
    private boolean isEditMode = false;

    /**
     * Defines object to listview  to display country
     */
    private ListView listCountry;
    /**
     * country list with item object
     */
    private ArrayList<CreateMapActivity.Item> countryList = new ArrayList<CreateMapActivity.Item>();
    /**
     * adaptor object which manages list of countries
     */
    private CountryAdaptor countryAdaptor;
    /**
     * list of countries as graph object
     */
    private ArrayList<GameMap> arrCountriesRepresentationOnGraph = new ArrayList<GameMap>();
    /**
     * List of countries added in map
     */
    private ArrayList<Integer> arrCountryAdded = new ArrayList<>();
    /**
     * Index of to button which stores the toCountry in arrCountriesRepresentationOnGraph
     */
    private int indexOfToButton = -1;
    /**
     * Index of from button which stores the fromCountry in arrCountriesRepresentationOnGraph
     */
    private int indexOfFromButton = -1;
    /**
     * number of total countries
     */
    private int totalCountries = 0;
    /**
     * count of number of countries added in graph
     */
    private int totalCountriesAddedInGraph = 0;
    /**
     * surfaceview object which holds canvas
     */
    private SurfaceView surfaceView;
    /**
     * canvas object which manages rendering of countries
     */
    private Canvas canvas;
    /**
     * List of coutries associated with each country
     */
    private HashMap<Continent, ArrayList<Country>> userCreatedMapData = new HashMap<Continent, ArrayList<Country>>();
    /**
     * Index of country currently selected
     */
    private int currentIndexCountrySelected;
    /**
     * width of screen size for canvas
     */
    private float width;
    /**
     * height of screen size for canvas
     */
    private float height;
    /**
     * width calculated from min and max x xcoordinate from map loaded
     */
    private  float mWidth;
    /**
     * height calculated from min and max y  y coordinate from map loaded
     */
    private float mHeight;
    /**
     * File name of map loaded
     */
    private String fileName;

    @SuppressLint("ClickableViewAccessibility")
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_map);
        listCountry = (ListView) findViewById(R.id.lvCountry);
        surfaceView = (SurfaceView) findViewById(R.id.surface);

        surfaceView.getHolder().addCallback(surfaceCallback);

        isEditMode = (Boolean) getIntent().getExtras().getBoolean("isEditMode");

        if (isEditMode) {
            Intent intent = this.getIntent();
            Bundle bundle = intent.getExtras();
            arrCountriesRepresentationOnGraph = (ArrayList<GameMap>) bundle.getSerializable("arrGameData");
            userCreatedMapData = (HashMap<Continent, ArrayList<Country>>) bundle.getSerializable("maps");
            fileName = bundle.getString("fileName");
        } else {
            userCreatedMapData = (HashMap<Continent, ArrayList<Country>>) getIntent().getSerializableExtra("maps");
        }

        prepareDataForList();

        setListItemListener();

        setSurfaceViewListener();

        setAddButtonListener();

        if (isEditMode) {

            findMinMaxCoordinates();

            handleEditMode();

            if (isMappingRequired()){
                performMapping();
            }

        }

    }

    public boolean isMappingRequired(){
        boolean isMappingRequired = false;
        for (int i = 0; i < arrCountriesRepresentationOnGraph.size() ; i++){
            if (arrCountriesRepresentationOnGraph.get(i).getCoordinateX()<=300){
                isMappingRequired = true;
                return isMappingRequired;
            }
        }
        return isMappingRequired;
    }

    public void performMapping(){

        for (int i = 0 ; i < arrCountriesRepresentationOnGraph.size() ; i++){
            GameMap map = arrCountriesRepresentationOnGraph.get(i);
            map.setCoordinateX(getXCordinate(map.getCoordinateX()));
            map.setCoordinateY(getYCordinate(map.getCoordinateY()));
            for (GameMap neighbouringCountry : map.getConnectedToCountries()){
                neighbouringCountry.setCoordinateX(getXCordinate(neighbouringCountry.getCoordinateX()));
                neighbouringCountry.setCoordinateY(getYCordinate(neighbouringCountry.getCoordinateY()));
            }
        }
    }

    /**
     * Find min and max co ordinates from array
     */

    public void findMinMaxCoordinates(){
        float minX,minY,maxX,maxY;
        float maxScreenX,maxScreenY;
        minX = arrCountriesRepresentationOnGraph.get(0).getCoordinateX();
        minY = arrCountriesRepresentationOnGraph.get(0).getCoordinateY();
        maxX = arrCountriesRepresentationOnGraph.get(0).getCoordinateX();
        maxY = arrCountriesRepresentationOnGraph.get(0).getCoordinateY();

        Display display = getWindowManager().getDefaultDisplay();

        Point size = new Point();
        display.getSize(size);
        maxScreenX = size.x;
        maxScreenY = size.y;

        for (int i = 1; i < arrCountriesRepresentationOnGraph.size() ;i++){

            if (minX > arrCountriesRepresentationOnGraph.get(i).getCoordinateX()){
                minX = arrCountriesRepresentationOnGraph.get(i).getCoordinateX();
            }

            if (minY > arrCountriesRepresentationOnGraph.get(i).getCoordinateY()){
                minY = arrCountriesRepresentationOnGraph.get(i).getCoordinateY();
            }

            if (arrCountriesRepresentationOnGraph.get(i).getCoordinateX() > maxX){
                maxX = arrCountriesRepresentationOnGraph.get(i).getCoordinateX();
            }

            if (arrCountriesRepresentationOnGraph.get(i).getCoordinateY()> maxY){
                maxY = arrCountriesRepresentationOnGraph.get(i).getCoordinateY();
            }

        }


        mWidth = maxX - minX + 4000;
        mHeight = maxY - minY + 3500;
        width = maxScreenX;
        height = maxScreenY;

    }

    /**
     * gets mapped x xoordinate according to ratio
     * @param xCoordinate - old xcoordinate
     * @return mapped coordinate
     */
    public float getXCordinate(float xCoordinate){
        float percent_width = Float.valueOf(xCoordinate) / width;
        return percent_width * mWidth;
    }

    /**
     * gets mapped y coordinate according to ratiox
     * @param yCoordinate - old y coordinate
     * @return mapped coordinate
     */
    public float getYCordinate(float yCoordinate){
        float percent_height = Float.valueOf(yCoordinate) / height;
        return percent_height * mHeight;
    }

    private SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            canvas = surfaceView.getHolder().lockCanvas();
            canvas.drawColor(Color.WHITE);
            surfaceView.getHolder().unlockCanvasAndPost(canvas);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    CreateMapActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (isEditMode){
                                renderMap();
                            }
                        }
                    });
                }
            }, 1);

        }

        /**
         * Initializing the surface destroy
         * @param holder : this parameter holds the surface
         */
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            holder.removeCallback(surfaceCallback);
        }

        /**
         * Initializing the change in surface
         * @param holder : parameter for holding the surface
         * @param format : parameter for defining the format
         * @param width : parameter for defining width of the surface
         * @param height : parameter for defining the height of the surface
         */
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        }
    };

    /**
     * Method to manage view while the map is being edited
     */
    public void handleEditMode() {

        for (Item item : countryList) {
            if (item instanceof EntryItem) {
                if (findIndexInarrCountriesRepresentationOnGraphFromCountry(((EntryItem) item).country)!= -1){
                    handleTapOnListView(countryList.indexOf(item));
                    totalCountriesAddedInGraph = totalCountriesAddedInGraph + 1;
                }
            }
        }

    }

    /**
     * Gets add button and handles on click listener
     */
    public void setAddButtonListener() {

        FloatingActionButton addButton = findViewById(R.id.done);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (totalCountries == totalCountriesAddedInGraph) {
                    handleAddMapAction();
                } else {
                    showToast(getString(R.string.add_all_countries));
                }
            }
        });
    }

    /**
     * Gets list view and handles onclick listener
     */
    public void setListItemListener() {
        countryAdaptor = new CountryAdaptor(this, countryList);
        countryAdaptor.arrCountryAdded = arrCountryAdded;
        listCountry.setAdapter(countryAdaptor);
        listCountry.setTextFilterEnabled(true);
        listCountry.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                handleTapOnListView(position);
            }
        });
    }

    /**
     * Gets surface view and handles onClick Listener
     */
    public void setSurfaceViewListener() {
        surfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                handleTouchOnSurfaceView(event);
                return true;
            }
        });
    }

    /**
     * Prepares country list data as per hashmap of countries recieved
     */
    public void prepareDataForList() {
        int colorIndex = 0;
        final Iterator it = userCreatedMapData.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            countryList.add(new CreateMapActivity.SectionItem((Continent) pair.getKey()));
            ArrayList<Country> arrCountry = new ArrayList<>();
            arrCountry = (ArrayList<Country>) pair.getValue();
            totalCountries = totalCountries + arrCountry.size();
            for (Country obj : arrCountry) {
                EntryItem item = new CreateMapActivity.EntryItem(obj);
                item.color = getColors(colorIndex);
                countryList.add(item);
            }
            colorIndex++;
        }
    }

    /**
     * Handles actions when surface view is tapped
     *
     * @param event event object recieved from click listener
     */
    public void handleTouchOnSurfaceView(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (totalCountriesAddedInGraph < totalCountries) {
                if (currentIndexCountrySelected != -1) {
                    GameMap map = arrCountriesRepresentationOnGraph.get(findIndexOfObjectarrCountriesRepresentationGraph(currentIndexCountrySelected));
                    map.setCoordinateX(event.getX());
                    map.setCoordinateY(event.getY());
                    arrCountriesRepresentationOnGraph.set(findIndexOfObjectarrCountriesRepresentationGraph(currentIndexCountrySelected), map);
                    renderMap();
                    totalCountriesAddedInGraph = totalCountriesAddedInGraph + 1;
                }
            } else {
                for (GameMap map : arrCountriesRepresentationOnGraph) {
                    if ((isPointInCircle(event.getX(), event.getY(), map.getCoordinateX(), map.getCoordinateY()))) {
                        if (indexOfToButton == -1) {
                            indexOfToButton = arrCountriesRepresentationOnGraph.indexOf(map);
                            renderMap();
                            showToast(getString(R.string.to_country_selected));
                            break;
                        } else {
                            indexOfFromButton = arrCountriesRepresentationOnGraph.indexOf(map);
                            if (indexOfToButton == indexOfFromButton) {
                                showToast(getString(R.string.to_from_same));
                                break;
                            } else {
                                GameMap toCountryMap = arrCountriesRepresentationOnGraph.get(indexOfToButton);
                                GameMap fromCountryMap = arrCountriesRepresentationOnGraph.get(indexOfFromButton);
                                if ((isCountryConnected(fromCountryMap.getConnectedToCountries(),toCountryMap) || isCountryConnected(toCountryMap.getConnectedToCountries(),fromCountryMap)) == false) {
                                    ArrayList<GameMap> arrConnectedFromCountry = fromCountryMap.getConnectedToCountries();
                                    ArrayList<GameMap> arrConnectedToCountry = toCountryMap.getConnectedToCountries();
                                    arrConnectedFromCountry.add(toCountryMap);
                                    arrConnectedToCountry.add(fromCountryMap);
                                    toCountryMap.setConnectedToCountries(arrConnectedToCountry);
                                    fromCountryMap.setConnectedToCountries(arrConnectedFromCountry);
                                    arrCountriesRepresentationOnGraph.set(indexOfToButton, toCountryMap);
                                    arrCountriesRepresentationOnGraph.set(indexOfFromButton, fromCountryMap);
                                    renderMap();
                                    showToast(getString(R.string.from_country_selected));
                                    indexOfToButton = -1;
                                    break;
                                } else {
                                    if (isEditMode){
                                        removeConnection(toCountryMap,fromCountryMap);
                                        indexOfFromButton = -1;
                                        indexOfToButton = -1;
                                    }else{
                                        showToast(getString(R.string.countries_connected));
                                    }
                                }
                            }

                        }
                    }
                }
            }
        }
    }

    /**
     * Check if a country is connected
     * @param arrGame List of game map objects
     * @param map game map
     * @return true if country is connected, false otherwise
     */
    public boolean isCountryConnected(ArrayList<GameMap> arrGame,GameMap map){
        for (GameMap map1 : arrGame ){
            if (map1.getFromCountry().getNameOfCountry().equals(map.getFromCountry().getNameOfCountry())){
                return true;
            }
        }
        return false;
    }
    /**
     * Handles events when add map is tapped, saves map if verification succeeds and removes connection if failed
     */
    public void handleAddMapAction() {

        MapVerification mapVerification = new MapVerification();
        if (mapVerification.mapVerification(arrCountriesRepresentationOnGraph) == true) {
            if (isEditMode){
                handleMapVerificationSucced(fileName);
                showToast(getString(R.string.edit_file_saved));
            }else{
                final EditText edittext = new EditText(CreateMapActivity.this);
                AlertDialog.Builder alert = new AlertDialog.Builder(CreateMapActivity.this);
                alert.setMessage("");
                alert.setTitle("Enter Map name");
                alert.setView(edittext);
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String mapName = edittext.getText().toString();
                        if (mapName.trim() != "") {
                            handleMapVerificationSucced(mapName);
                        }
                    }
                });

                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // what ever you want to do with No option.
                    }
                });

                alert.show();
            }


        } else {
            Toast.makeText(CreateMapActivity.this,
                    "Verification Failed", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Saves verified map on success
     *
     * @param filename filename passed by user
     */

    public void handleMapVerificationSucced(String filename) {
        MapDriverController mapDriverController = new MapDriverController();
        mapDriverController.writeMap(CreateMapActivity.this, filename, arrCountriesRepresentationOnGraph);

    }

    /**
     * adds data in Gamemap object when user selects listview
     *
     * @param position position of object in list
     */
    public void handleTapOnListView(int position) {
        if (!arrCountryAdded.contains(position)) {
            Item item = (Item) countryAdaptor.getItem(position);
            if (item instanceof EntryItem) {
                GameMap map = new GameMap();
                map.setContinentColor(((EntryItem) item).color);
                map.setFromCountry(((EntryItem) item).country);
                map.setIndexOfCountryInList(position);
                currentIndexCountrySelected = position;
                if (isEditMode == false) {
                    arrCountriesRepresentationOnGraph.add(map);
                } else {
                    int indexInarrCountriesRepresentation = findIndexInarrCountriesRepresentationOnGraphFromCountry(((EntryItem) item).country);
                    if (indexInarrCountriesRepresentation == -1){
                        arrCountriesRepresentationOnGraph.add(map);
                    }else{
                        GameMap map1 = arrCountriesRepresentationOnGraph.get(indexInarrCountriesRepresentation);
                        map1.setIndexOfCountryInList(position);
                        map1.setContinentColor(((EntryItem) item).color);
                        if (indexInarrCountriesRepresentation != -1) {
                            arrCountriesRepresentationOnGraph.set(indexInarrCountriesRepresentation, map1);
                        }
                    }
                }
                arrCountryAdded.add(position);
                listCountry.invalidateViews();
            }
        }
    }

    /**
     * Find index of object in array of game map object recieved according to country
     * @param country country object
     * @return index object
     */
    public int findIndexInarrCountriesRepresentationOnGraphFromCountry(Country country) {

        for (GameMap map : arrCountriesRepresentationOnGraph) {
            if (map.getFromCountry().getNameOfCountry().equals(country.getNameOfCountry())) {
                return arrCountriesRepresentationOnGraph.indexOf(map);
            }
        }
        return -1;
    }

    /**
     * Finds index of game map object according to index of object according to countrylist
     *
     * @param index - index of object in countrylist
     * @return index of same object in arrCountriesRepresentationOnGraph
     */
    public int findIndexOfObjectarrCountriesRepresentationGraph(int index) {
        for (GameMap map : arrCountriesRepresentationOnGraph) {
            if (map.getIndexOfCountryInList() == index) {
                return arrCountriesRepresentationOnGraph.indexOf(map);
            }
        }
        return -1;
    }

    /**
     * Detects if user tapped in cirlce
     *
     * @param xTouched - x coordinate where user tapped
     * @param yTouched - y coordinate where user tapped
     * @param xCountry - x coordinate of country created by user
     * @param yCountry - y coordinate of country created by user
     * @return whether tapped point belongs to country or not.
     */
    public boolean isPointInCircle(float xTouched, float yTouched, float xCountry, float yCountry) {
        return Math.sqrt((xCountry - xTouched) * (xCountry - xTouched) + (yCountry - yTouched) * (yCountry - yTouched)) <= RADIUS;
    }

    /**
     * Removes connection btw countries
     * @param toCountry
     * @param fromCountry
     */

    public void removeConnection(GameMap toCountry,GameMap fromCountry) {

        canvas = surfaceView.getHolder().lockCanvas();
        canvas.drawColor(0, PorterDuff.Mode.CLEAR);
        canvas.drawColor(Color.WHITE);

        int indexToCountry = findIndexInarrCountriesRepresentationOnGraphFromCountry(toCountry.getFromCountry());
        int indexFromCountry = findIndexInarrCountriesRepresentationOnGraphFromCountry(fromCountry.getFromCountry());


        removeNieghbour(toCountry,fromCountry);
        removeNieghbour(fromCountry,toCountry);

        arrCountriesRepresentationOnGraph.set(indexToCountry,toCountry);
        arrCountriesRepresentationOnGraph.set(indexFromCountry,fromCountry);

        surfaceView.getHolder().unlockCanvasAndPost(canvas);

        renderMap();

    }

    /**
     * Removes Connection btw countries
     * @param toCountry
     * @param fromCountry
     */
    public void removeNieghbour(GameMap toCountry,GameMap fromCountry){

        Iterator<GameMap> itr = toCountry.getConnectedToCountries().iterator();
        while (itr.hasNext()){
            GameMap map = itr.next();
            if(map.getFromCountry().getNameOfCountry().equals(fromCountry.getFromCountry().getNameOfCountry())){
                Iterator<GameMap> itrN = map.getConnectedToCountries().iterator();
                while (itrN.hasNext()) {
                    GameMap mapN = itrN.next();
                    if(mapN.getFromCountry().getNameOfCountry().equals(toCountry.getFromCountry().getNameOfCountry())) {
                        itrN.remove();
                    }
                }
                itr.remove();
            }
        }

    }




    /**
     * Creates Map
     */
    public void renderMap() {
        canvas = surfaceView.getHolder().lockCanvas();
        canvas.drawColor(Color.WHITE);
        Paint connectionLine = new Paint();
        Paint text = new Paint();
        text.setTextSize(40);
        text.setColor(Color.BLACK);
        connectionLine.setTextSize(40);
        connectionLine.setColor(Color.YELLOW);
        connectionLine.setStrokeWidth(10);
        for (GameMap map : arrCountriesRepresentationOnGraph) {
            Paint paint = new Paint();
            paint.setColor(map.getContinentColor());

            drawCircle(map.getCoordinateX(),map.getCoordinateY(),RADIUS,paint);

            for (GameMap nieghbourCountry : map.getConnectedToCountries()) {
                drawLine(map.getCoordinateX(),map.getCoordinateY(),nieghbourCountry.getCoordinateX(),nieghbourCountry.getCoordinateY(),connectionLine);
            }

            drawText(map.getFromCountry().getNameOfCountry().substring(0,2),map.getCoordinateX()-20,map.getCoordinateY(),text);
        }
        surfaceView.getHolder().unlockCanvasAndPost(canvas);
        currentIndexCountrySelected = -1;
    }

    public void drawLine(float x,float y,float neighbourX,float neighbourY,Paint line){
        canvas.drawLine(x,y,neighbourX,neighbourY, line);
    }

    public void drawText(String str,float x,float y,Paint text){
        canvas.drawText(str,x,y,text);
    }

    public void drawCircle(float x,float y,int radius,Paint paint){
        canvas.drawCircle(x,y,radius,paint);
    }

    /**
     * Sends color according to index
     *
     * @param index index of continent
     * @return color
     */
    public int getColors(int index) {
        String[] allColors = this.getResources().getStringArray(R.array.colors);
        return Color.parseColor(allColors[index]);
    }

    /**
     * Method to create and show toast
     * @param msg message to be displayed on toast
     */
    public void showToast(String msg) {
        Toast.makeText(CreateMapActivity.this, msg,
                Toast.LENGTH_LONG).show();
    }

    /**
     * Interface to define Item
     */
    public interface Item {
        public boolean isSection();

        public String getTitle();
    }

    /**
     * Class that defines section item for listview
     */
    public class SectionItem implements CreateMapActivity.Item {
        Continent continent;
        private final String title;

        public SectionItem(Continent continent) {
            this.continent = continent;
            this.title = continent.getNameOfContinent();
        }

        public String getTitle() {
            return title;
        }

        @Override
        public boolean isSection() {
            return true;
        }
    }

    /**
     * Class defines Entry  for listview
     */
    public class EntryItem implements CreateMapActivity.Item {
        Country country;
        public final String title;
        int color;

        public EntryItem(Country country) {
            this.country = country;
            this.title = country.getNameOfCountry();
        }

        public String getTitle() {
            return title;
        }

        @Override
        public boolean isSection() {
            return false;
        }
    }

}



