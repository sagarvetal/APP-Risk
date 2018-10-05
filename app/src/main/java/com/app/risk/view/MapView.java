package com.app.risk.view;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.app.risk.MainActivity;import java.util.ArrayList;

public class MapView extends AppCompatActivity {
    private ListView lvCountry;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view);

        lvCountry = (ListView) findViewById(R.id.lvCountry);

        ArrayList<Item> countryList = new ArrayList<MainActivity.MapView.Item>();
        // Header
        countryList.add(new SectionItem("Asia"));
        // State Name
        countryList.add(new EntryItem("India"));
        countryList.add(new EntryItem("China"));
        countryList.add(new EntryItem("Hong Kong"));
        countryList.add(new EntryItem("Nepal"));

        // Header
        countryList.add(new SectionItem("North Asia"));
        // State Name
        countryList.add(new EntryItem("Belarus"));
        countryList.add(new EntryItem("Moldova"));
        countryList.add(new EntryItem("Russian Federation"));
        countryList.add(new EntryItem("Ukraine"));

        // Header
        countryList.add(new SectionItem("North America"));
        // State Name
        countryList.add(new EntryItem("Canada"));
        countryList.add(new EntryItem("Saint Pierre and Miquelon"));
        countryList.add(new EntryItem("United States"));

        // Header
        countryList.add(new SectionItem("North & Central America"));
        // State Name
        countryList.add(new EntryItem("Caribbean Islands"));
        countryList.add(new EntryItem("Anguilla"));
        countryList.add(new EntryItem("Antigua and Barbuda"));
        countryList.add(new EntryItem("Aruba"));

    }

    public interface Item {
        public boolean isSection();
        public String getTitle();
    }

    public class SectionItem implements Item {
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
    public class EntryItem implements Item {
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
        private ArrayList<Item> item;
        private ArrayList<Item> orignalItem;

        public CountryAdaptor(){
            super();
        }

        public CountryAdaptor(Context context,ArrayList<Item> item){
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
                tvSectionTitle.setText(((SectionItem) item.get(position)).getTitle());
            }else{
                convertView = inflater.inflate(R.layout.layout_item, parent, false);
                TextView tvItemTitle = (TextView) convertView.findViewById(R.id.tvItemTitle);
                tvItemTitle.setText(((EntryItem) item.get(position)).getTitle());
            }
            return convertView;
        }
    }

}

