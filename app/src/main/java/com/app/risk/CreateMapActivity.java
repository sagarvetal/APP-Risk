package com.app.risk;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.app.risk.model.Continent;
import com.app.risk.model.Country;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class CreateMapActivity extends Activity {

    private ListView lvCountry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_map);
        lvCountry = (ListView) findViewById(R.id.lvCountry);
        ArrayList<CreateMapActivity.Item> countryList = new ArrayList<CreateMapActivity.Item>();

        HashMap<Continent,ArrayList<Country>> userCreatedMapData = new HashMap<Continent,ArrayList<Country>>();
        Continent continent = new Continent();
        continent.setArmyControlValue(34);
        continent.setNameOfContinent("Army");
        ArrayList<Country> arr = new ArrayList<>();

        userCreatedMapData.put(continent,arr);
        Country co = new Country();
        co.setNameOfCountry("sfdfds");
        co.setBelongsToContinent(continent);
        arr.add(co);
        Country c1 = new Country();
        c1.setNameOfCountry("sdfdsf12");
        c1.setBelongsToContinent(continent);
        arr.add(c1);
        Iterator it = userCreatedMapData.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry pair = (Map.Entry)it.next();
            countryList.add(new CreateMapActivity.SectionItem((Continent)pair.getKey()));
            ArrayList<Country> arrCountry = new ArrayList<>();
            arrCountry = (ArrayList<Country>) pair.getValue();
            for (Country obj : arrCountry){
                countryList.add(new CreateMapActivity.EntryItem(obj));
            }
        }

        final CountryAdaptor adapter = new CountryAdaptor(this, countryList);
        lvCountry.setAdapter(adapter);
        lvCountry.setTextFilterEnabled(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int)event.getRawX();
        int y = (int)event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                addButton(x,y);
        }
        return false;
    }

    public void addButton(int x,int y){

        final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0);
        Button btn = new Button(this);

        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(180, 180); // Button width and button height.
        lp.leftMargin = x;
        lp.topMargin = y;

        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });

        btn.setLayoutParams(lp);
        viewGroup.addView(btn);
    }

    public interface Item {
        public boolean isSection();
        public String getTitle();
    }

    public class SectionItem implements CreateMapActivity.Item {
        Continent continent;

        private final String title;

        public SectionItem(Continent continent) {
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

        public EntryItem(Country country) {
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
            }
            return convertView;
        }
    }

}
