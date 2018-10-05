package com.app.risk;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class CreateMapActivity extends Activity {
    private ListView lvCountry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_map);
        lvCountry = (ListView) findViewById(R.id.lvCountry);
        ArrayList<CreateMapActivity.Item> countryList = new ArrayList<CreateMapActivity.Item>();
        // Header
        countryList.add(new CreateMapActivity.SectionItem("Asia"));
        // State Name
        countryList.add(new CreateMapActivity.EntryItem("India"));
        countryList.add(new CreateMapActivity.EntryItem("China"));
        countryList.add(new CreateMapActivity.EntryItem("Hong Kong"));
        countryList.add(new CreateMapActivity.EntryItem("Nepal"));

        // Header
        countryList.add(new CreateMapActivity.SectionItem("North Asia"));
        // State Name
        countryList.add(new CreateMapActivity.EntryItem("Belarus"));
        countryList.add(new CreateMapActivity.EntryItem("Moldova"));
        countryList.add(new CreateMapActivity.EntryItem("Russian Federation"));
        countryList.add(new CreateMapActivity.EntryItem("Ukraine"));

        // Header
        countryList.add(new CreateMapActivity.SectionItem("North America"));
        // State Name
        countryList.add(new CreateMapActivity.EntryItem("Canada"));
        countryList.add(new CreateMapActivity.EntryItem("Saint Pierre and Miquelon"));
        countryList.add(new CreateMapActivity.EntryItem("United States"));

        // Header
        countryList.add(new CreateMapActivity.SectionItem("North & Central America"));
        // State Name
        countryList.add(new CreateMapActivity.EntryItem("Caribbean Islands"));
        countryList.add(new CreateMapActivity.EntryItem("Anguilla"));
        countryList.add(new CreateMapActivity.EntryItem("Antigua and Barbuda"));
        countryList.add(new CreateMapActivity.EntryItem("Aruba"));
        final CountryAdaptor adapter = new CountryAdaptor(this, countryList);
        lvCountry.setAdapter(adapter);
        lvCountry.setTextFilterEnabled(true);
    }

    public interface Item {
        public boolean isSection();
        public String getTitle();
    }

    public class SectionItem implements CreateMapActivity.Item {
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
    }

    public class EntryItem implements CreateMapActivity.Item {
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
