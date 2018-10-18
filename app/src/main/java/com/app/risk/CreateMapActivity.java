/**
 *
 */
package com.app.risk;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.risk.model.Continent;
import com.app.risk.model.Country;
import com.app.risk.model.GameMap;
import com.app.risk.utility.MapVerification;
import com.app.risk.utility.WriteGameMapToFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class CreateMapActivity extends Activity {

    public static final int RADIUS = 100;

    private ListView lvCountry;
    ArrayList<CreateMapActivity.Item> countryList = new ArrayList<CreateMapActivity.Item>();
    ArrayList<GameMap> arrCountriesRepresentationOnGraph = new ArrayList<GameMap>();
    ArrayList<Integer> arrCountryAdded = new ArrayList<>();
    int indexOfToButton = -1;
    int indexOfFromButton = -1;
    int totalCountries=0;
    int totalCountriesAddedInGraph = 0;
    SurfaceView surfaceView;
    Canvas canvas;
    public HashMap<Continent,ArrayList<Country>> userCreatedMapData = new HashMap<Continent,ArrayList<Country>>();


    int currentIndexCountrySelected;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_map);
        lvCountry = (ListView) findViewById(R.id.lvCountry);
        surfaceView = (SurfaceView) findViewById(R.id.surface);
        userCreatedMapData = (HashMap<Continent,ArrayList<Country>>) getIntent().getSerializableExtra("maps");

        final Iterator it = userCreatedMapData.entrySet().iterator();
        int colorIndex = 0;
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
        final CountryAdaptor adapter = new CountryAdaptor(this, countryList);
        lvCountry.setAdapter(adapter);
        lvCountry.setTextFilterEnabled(true);
        //Test
        lvCountry.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!arrCountryAdded.contains(position)){
                    Item item = (Item)adapter.getItem(position);
                    if (item instanceof EntryItem){
                        GameMap map = new GameMap();
                        map.setContinentColor(((EntryItem) item).color);
                        map.setFromCountry(((EntryItem) item).country);
                        map.setIndexOfCountryInList(position);
                        currentIndexCountrySelected = position;
                        arrCountriesRepresentationOnGraph.add(map);
                        arrCountryAdded.add(position);
                        lvCountry.invalidateViews();
                    }
                }
            }
        });
        surfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    if (totalCountriesAddedInGraph < totalCountries){
                        GameMap map = arrCountriesRepresentationOnGraph.get(findIndexOfObject(currentIndexCountrySelected));
                        map.setCoordinateX(event.getX());
                        map.setCoordinateY(event.getY());
                        arrCountriesRepresentationOnGraph.set(findIndexOfObject(currentIndexCountrySelected),map);
                        renderMap();
                    }else{
                        for (GameMap map : arrCountriesRepresentationOnGraph){
                            if ((isPointInCircle(event.getX(),event.getY(),map.getCoordinateX(),map.getCoordinateY()))){
                                if(indexOfToButton == -1){
                                    indexOfToButton = arrCountriesRepresentationOnGraph.indexOf(map);
                                    renderMap();
                                    break;
                                }else{
                                    indexOfFromButton = arrCountriesRepresentationOnGraph.indexOf(map);
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
                                        indexOfToButton = -1;
                                        break;
                                    }

                                }
                            }
                        }
                    }
                }
                return true;
            }
        });
        FloatingActionButton fab = findViewById(R.id.done);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MapVerification mapVerification = new MapVerification();
                for (GameMap map : arrCountriesRepresentationOnGraph){
                    Log.d(TAG, "onClick: " +map.getFromCountry().getNameOfCountry());
                }
                if (mapVerification.mapVerification(arrCountriesRepresentationOnGraph) == true){
                    final EditText edittext = new EditText(CreateMapActivity.this);
                    AlertDialog.Builder alert = new AlertDialog.Builder(CreateMapActivity.this);
                    alert.setMessage("");
                    alert.setTitle("Enter Map name");
                    alert.setView(edittext);

                    alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            String mapName = edittext.getText().toString();
                            WriteGameMapToFile writeGameMapToFile = new WriteGameMapToFile();
                            writeGameMapToFile.writeGameMapToFile(CreateMapActivity.this,mapName,arrCountriesRepresentationOnGraph);
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
        });
    }

    /**
     *
     * @param index
     * @return
     */
    public int findIndexOfObject(int index){
        for (GameMap map: arrCountriesRepresentationOnGraph){
            if (map.getIndexOfCountryInList() == index){
                return arrCountriesRepresentationOnGraph.indexOf(map);
            }
        }
        return -1;
    }

    /**
     *
     * @param xTouched
     * @param yTouched
     * @param xCountry
     * @param yCountry
     * @return
     */
    public boolean isPointInCircle(float xTouched,float yTouched,float xCountry,float yCountry){
        double centerX = xCountry + RADIUS;
        double centerY = yCountry + RADIUS;
        double distanceX = xTouched - centerX;
        double distanceY = yTouched - centerY;
        return Math.sqrt((xCountry-xTouched)*(xCountry-xTouched)+(yCountry-yTouched)*(yCountry-yTouched)) <= RADIUS;
    }

    public void renderMap(){
        Paint connectionLine = new Paint();
        connectionLine.setColor(Color.BLACK);
        connectionLine.setStrokeWidth(10);
        canvas = surfaceView.getHolder().lockCanvas();


        for (GameMap map : arrCountriesRepresentationOnGraph ){
            Paint paint = new Paint();
            paint.setColor(map.getContinentColor());
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
     *
     * @param index
     * @return
     */
    public int getColors(int index){
        String[] allColors = this.getResources().getStringArray(R.array.colors);
        return Color.parseColor(allColors[index]);
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

    public class CountryAdaptor extends BaseAdapter {

        private Context context;
        private ArrayList<CreateMapActivity.Item> item;
        private ArrayList<CreateMapActivity.Item> orignalItem;
        public CountryAdaptor(){
            super();
        }
        public CountryAdaptor(Context context,ArrayList<CreateMapActivity.Item> item){
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
        public View getView(int position, View convertView , ViewGroup parent){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (item.get(position).isSection()){
                convertView = inflater.inflate(R.layout.layout_section,parent,false);
                TextView tvSectionTitle = (TextView) convertView.findViewById(R.id.tvSectionTitle);
                tvSectionTitle.setText(((CreateMapActivity.SectionItem) item.get(position)).getTitle());
            }else{
                convertView = inflater.inflate(R.layout.layout_item, parent, false);
                TextView tvItemTitle = (TextView) convertView.findViewById(R.id.tvItemTitle);
                tvItemTitle.setText(((CreateMapActivity.EntryItem) item.get(position)).getTitle());
                if (arrCountryAdded.contains(position)){
                   tvItemTitle.setAlpha(0.2f);
                }
            }
            return convertView;
        }
    }


}
