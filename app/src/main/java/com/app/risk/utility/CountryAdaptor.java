package com.app.risk.utility;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.app.risk.CreateMapActivity;
import com.app.risk.R;

import java.util.ArrayList;

public class CountryAdaptor extends BaseAdapter {

    private Context context;
    private ArrayList<CreateMapActivity.Item> item;
    private ArrayList<CreateMapActivity.Item> orignalItem;
    public ArrayList<Integer> arrCountryAdded = new ArrayList<>();

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