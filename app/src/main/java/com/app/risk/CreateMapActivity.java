package com.app.risk;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.app.risk.model.Continent;
import com.app.risk.model.Country;
import com.app.risk.model.GameMap;
import com.app.risk.utility.CountryAdaptor;
import com.app.risk.utility.MapVerification;
import com.app.risk.utility.MapWriter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Creates Map and saves map after verification
 *
 */

public class CreateMapActivity extends Activity  {

    public static final int RADIUS = 100;
    private boolean isEditMode = false;

    private ListView listCountry;
    private ArrayList<CreateMapActivity.Item> countryList = new ArrayList<CreateMapActivity.Item>();
    private CountryAdaptor countryAdaptor;
    private ArrayList<GameMap> arrCountriesRepresentationOnGraph = new ArrayList<GameMap>();
    private ArrayList<Integer> arrCountryAdded = new ArrayList<>();
    private int indexOfToButton = -1;
    private int indexOfFromButton = -1;
    private int totalCountries=0;
    private int totalCountriesAddedInGraph = 0;
    private SurfaceView surfaceView;
    private Canvas canvas;
    private HashMap<Continent,ArrayList<Country>> userCreatedMapData = new HashMap<Continent,ArrayList<Country>>();
    private int currentIndexCountrySelected;

    private Context context;

//save the context recievied via constructor in a local variable

    public CreateMapActivity(Context context){
        this.context=context;
    }
    public CreateMapActivity(){
        super();
    }
    @SuppressLint("ClickableViewAccessibility")
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_map);
        listCountry = (ListView) findViewById(R.id.lvCountry);
        surfaceView = (SurfaceView) findViewById(R.id.surface);

        surfaceView.getHolder().addCallback(surfaceCallback);

        isEditMode = (Boolean)getIntent().getExtras().getBoolean("isEditMode");

        if(isEditMode){
            Intent intent = this.getIntent();
            Bundle bundle = intent.getExtras();
            arrCountriesRepresentationOnGraph =  (ArrayList<GameMap>)bundle.getSerializable("arrGameData");
            userCreatedMapData = (HashMap<Continent,ArrayList<Country>>)bundle.getSerializable("maps");
        }else{
            userCreatedMapData = (HashMap<Continent,ArrayList<Country>>) getIntent().getSerializableExtra("maps");
        }

        prepareDataForList();

        setListItemListener();

        setSurfaceViewListener();

        setAddButtonListener();

        if (isEditMode){
            handleEditMode();
        }

    }

    private SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            canvas = surfaceView.getHolder().lockCanvas();
            canvas.drawColor( Color.WHITE);
            surfaceView.getHolder().unlockCanvasAndPost(canvas);
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


    public void handleEditMode(){

        for (Item item : countryList){
            if (item instanceof EntryItem){
                handleTapOnListView(countryList.indexOf(item));
            }
        }
        renderMap();

    }
    /**
     * Gets add button and handles on click listener
     */
    public void setAddButtonListener(){

        FloatingActionButton addButton = findViewById(R.id.done);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(totalCountries == totalCountriesAddedInGraph) {
                    handleAddMapAction();
                }else{
                    showToast("Add All Countries First");
                }
            }
        });
    }

    /**
     *Gets list view and handles onclick listener
     */
    public void setListItemListener(){
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
     *Gets surface view and handles onClick Listener
     */
    public void setSurfaceViewListener(){
        surfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                handleTouchOnSurfaceView(event);
                return true;
            }
        });
    }

    /**
     *Prepares country list data as per hashmap of countries recieved
     */
    public void prepareDataForList(){
        int colorIndex = 0;
        final Iterator it = userCreatedMapData.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry pair = (Map.Entry)it.next();
            countryList.add(new CreateMapActivity.SectionItem((Continent)pair.getKey()));
            ArrayList<Country> arrCountry = new ArrayList<>();
            arrCountry = (ArrayList<Country>) pair.getValue();
            totalCountries = totalCountries + arrCountry.size();
            for (Country obj : arrCountry){
                EntryItem item = new CreateMapActivity.EntryItem(obj);
                item.color = getColors(colorIndex);
                countryList.add(item);
            }
            colorIndex++;
        }
    }

    /**
     *Handles actions when surface view is tapped
     * @param event event object recieved from click listener
     */
    public void handleTouchOnSurfaceView(MotionEvent event){
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            if (totalCountriesAddedInGraph < totalCountries){
                if (currentIndexCountrySelected!=-1){
                    GameMap map = arrCountriesRepresentationOnGraph.get(findIndexOfObjectarrCountriesRepresentationGraph(currentIndexCountrySelected));
                    map.setCoordinateX(event.getX());
                    map.setCoordinateY(event.getY());
                    arrCountriesRepresentationOnGraph.set(findIndexOfObjectarrCountriesRepresentationGraph(currentIndexCountrySelected),map);
                    renderMap();
                }
            }else{
                for (GameMap map : arrCountriesRepresentationOnGraph){
                    if ((isPointInCircle(event.getX(),event.getY(),map.getCoordinateX(),map.getCoordinateY()))){
                        if(indexOfToButton == -1){
                            indexOfToButton = arrCountriesRepresentationOnGraph.indexOf(map);
                            renderMap();
                            showToast("To Country Selected");
                            break;
                        }else{
                            indexOfFromButton = arrCountriesRepresentationOnGraph.indexOf(map);
                            if(indexOfToButton == indexOfFromButton) {
                                if (isEditMode){
                                    removeConnection(map);
                                }else{
                                    showToast("To Country and From Country cannot be same");
                                }
                                break;
                            }else{
                                GameMap toCountryMap = arrCountriesRepresentationOnGraph.get(indexOfToButton);
                                GameMap fromCountryMap = arrCountriesRepresentationOnGraph.get(indexOfFromButton);
                                if ((fromCountryMap.getConnectedToCountries().contains(toCountryMap) || toCountryMap.getConnectedToCountries().contains(fromCountryMap)) == false) {
                                    ArrayList<GameMap> arrConnectedFromCountry = fromCountryMap.getConnectedToCountries();
                                    ArrayList<GameMap> arrConnectedToCountry = toCountryMap.getConnectedToCountries();
                                    arrConnectedFromCountry.add(toCountryMap);
                                    arrConnectedToCountry.add(fromCountryMap);
                                    toCountryMap.setConnectedToCountries(arrConnectedToCountry);
                                    fromCountryMap.setConnectedToCountries(arrConnectedFromCountry);
                                    arrCountriesRepresentationOnGraph.set(indexOfToButton,toCountryMap);
                                    arrCountriesRepresentationOnGraph.set(indexOfFromButton,fromCountryMap);
                                    renderMap();
                                    showToast("From Country Selected");
                                    indexOfToButton = -1;
                                    break;
                                }else{
                                    showToast("Countries Already Connected");
                                }
                            }

                        }
                    }
                }
            }
        }
    }

    /**
     * Handles events when add map is tapped, saves map if verification succeeds and removes connection if failed
     *
     */
    public void handleAddMapAction(){

        MapVerification mapVerification = new MapVerification();
        if (mapVerification.mapVerification(arrCountriesRepresentationOnGraph) == true){
            final EditText edittext = new EditText(CreateMapActivity.this);
            AlertDialog.Builder alert = new AlertDialog.Builder(CreateMapActivity.this);
            alert.setMessage("");
            alert.setTitle("Enter Map name");
            alert.setView(edittext);

            alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    String mapName = edittext.getText().toString();
                    handleMapVerificationSucced(mapName);
                }
            });

            alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // what ever you want to do with No option.
                }
            });

            alert.show();

        }else {
            Toast.makeText(CreateMapActivity.this,
                    "Verification Failed", Toast.LENGTH_LONG).show();
        }
    }

    public void handleMapVerificationSucced(String filename){
            MapWriter mapWriter = new MapWriter();
            mapWriter.writeGameMapToFile(CreateMapActivity.this,filename,arrCountriesRepresentationOnGraph);
    }

    /**
     * adds data in Gamemap object when user selects listview
     * @param position position of object in list
     */
    public void handleTapOnListView(int position){
        if (!arrCountryAdded.contains(position)){
            Item item = (Item)countryAdaptor.getItem(position);
            if (item instanceof EntryItem){
                GameMap map = new GameMap();
                map.setContinentColor(((EntryItem) item).color);
                map.setFromCountry(((EntryItem) item).country);
                map.setIndexOfCountryInList(position);
                currentIndexCountrySelected = position;
                if (isEditMode == false){
                    arrCountriesRepresentationOnGraph.add(map);
                }else{
                    int indexInarrCountriesRepresentation = findIndexInarrCountriesRepresentationOnGraphFromCountry(((EntryItem) item).country);
                    GameMap map1 = arrCountriesRepresentationOnGraph.get(indexInarrCountriesRepresentation);
                    if (indexInarrCountriesRepresentation != -1){
                        arrCountriesRepresentationOnGraph.set(indexInarrCountriesRepresentation,map1);
                    }
                }
                arrCountryAdded.add(position);
                listCountry.invalidateViews();
            }
        }
    }
    public  int findIndexInarrCountriesRepresentationOnGraphFromCountry(Country country){

        for (GameMap map : arrCountriesRepresentationOnGraph ){
            if (map.getFromCountry().equals(country)){
                return arrCountriesRepresentationOnGraph.indexOf(map);
            }
        }
        return  -1;
    }
    /**
     *Finds index of game map object according to index of object according to countrylist
     * @param index - index of object in countrylist
     * @return index of same object in arrCountriesRepresentationOnGraph
     */
    public int findIndexOfObjectarrCountriesRepresentationGraph(int index){
        for (GameMap map: arrCountriesRepresentationOnGraph){
            if (map.getIndexOfCountryInList() == index){
                return arrCountriesRepresentationOnGraph.indexOf(map);
            }
        }
        return -1;


        //Comment
    }

    /**
     * Detects if user tapped in cirlce
     * @param xTouched - x coordinate where user tapped
     * @param yTouched - y coordinate where user tapped
     * @param xCountry - x coordinate of country created by user
     * @param yCountry - y coordinate of country created by user
     * @return whether tapped point belongs to country or not.
     */
    public boolean isPointInCircle(float xTouched,float yTouched,float xCountry,float yCountry){
        return Math.sqrt((xCountry-xTouched)*(xCountry-xTouched)+(yCountry-yTouched)*(yCountry-yTouched)) <= RADIUS;
    }

    public void removeConnection(GameMap map){

        canvas = surfaceView.getHolder().lockCanvas();
        canvas.drawColor(0, PorterDuff.Mode.CLEAR);
        canvas.drawColor( Color.WHITE);

        map.setConnectedToCountries(new ArrayList<GameMap>());

        for (GameMap neighbourCountries : map.getConnectedToCountries() ){
            map.setConnectedToCountries(new ArrayList<GameMap>());
            for (GameMap nieghbourCountry : map.getConnectedToCountries()  ){
                nieghbourCountry.setConnectedToCountries(new ArrayList<GameMap>());
            }
        }

        surfaceView.getHolder().unlockCanvasAndPost(canvas);

        renderMap();

    }

    /**
     *Creates Map
     */
    public void renderMap(){
        canvas = surfaceView.getHolder().lockCanvas();
        canvas.drawColor( Color.WHITE);
        Paint connectionLine = new Paint();
        connectionLine.setColor(Color.WHITE);
        connectionLine.setStrokeWidth(10);
        for (GameMap map : arrCountriesRepresentationOnGraph ){
            Paint paint = new Paint();
            paint.setColor(map.getContinentColor());
//            canvas.drawText(map.getFromCountry().getNameOfCountry().substring(0,2),map.getCoordinateX()-10,map.getCoordinateY()-10,connectionLine);
            canvas.drawCircle(map.getCoordinateX(),map.getCoordinateY(),RADIUS,paint);
            for (GameMap nieghbourCountry : map.getConnectedToCountries()  ){
                canvas.drawLine(map.getCoordinateX(),map.getCoordinateY(),nieghbourCountry.getCoordinateX(),nieghbourCountry.getCoordinateY(),connectionLine);
            }
        }
        surfaceView.getHolder().unlockCanvasAndPost(canvas);
        totalCountriesAddedInGraph = totalCountriesAddedInGraph + 1;
        currentIndexCountrySelected = -1;
    }

    /**
     * Sends color according to index
     * @param index index of continent
     * @return color
     */
    public int getColors(int index){
        String[] allColors = this.getResources().getStringArray(R.array.colors);
        return Color.parseColor(allColors[index]);
    }

    public void showToast(String msg){
        Toast.makeText(CreateMapActivity.this, msg,
                Toast.LENGTH_LONG).show();
    }
    /**
     *
     */
    public interface Item {
        public boolean isSection();
        public String getTitle();
    }

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

