package com.app.risk.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.app.risk.view.CreateMapActivity;
import com.app.risk.R;

import java.util.ArrayList;
/**
 * This class is used add the item of string to the list
 * @author Urvi
 * @version 1.0.0
 */
public class CountryAdaptor extends BaseAdapter {

    private Context context;
    private ArrayList<CreateMapActivity.Item> item;
    private ArrayList<CreateMapActivity.Item> orignalItem;
    public ArrayList<Integer> arrCountryAdded = new ArrayList<>();
    /**
     * This default constructor used to create country adaptor object
     *
     */
    public CountryAdaptor()
    {
        super();
    }
    /**
     * This parameterized constructor used to create country adaptor object
     *
     */
    public CountryAdaptor(Context context, ArrayList<CreateMapActivity.Item> item)
    {
        this.context = context;
        this.item = item;
    }
    /**
     * The method gets the size of the list
     *
     */
    @Override
    public int getCount()
    {
        return item.size();
    }
    /**
     * This method returns the item in the list based on the position
     * @param position of item in the list
     * @return item of the Object type
     */
    @Override
    public Object getItem(int position)
    {
        return item.get(position);
    }
    /**
     * This method returns the position of the item
     * @param position of the item in the list
     * @return postion of item in the list
     */
    @Override
    public long getItemId(int position)
    {
        return position;
    }
    /**
     * This displays the items in the view
     * @param position of the item in the list
     * @param convertView view to display
     * @param parent parent view of the current view
     *
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (item.get(position).isSection()) {
            convertView = inflater.inflate(R.layout.layout_section, parent, false);
            TextView tvSectionTitle = (TextView) convertView.findViewById(R.id.tvSectionTitle);
            tvSectionTitle.setText(((CreateMapActivity.SectionItem) item.get(position)).getTitle());
        } else {
            convertView = inflater.inflate(R.layout.layout_item, parent, false);
            TextView tvItemTitle = (TextView) convertView.findViewById(R.id.tvItemTitle);
            tvItemTitle.setText(((CreateMapActivity.EntryItem) item.get(position)).getTitle());
            if (arrCountryAdded.contains(position)) {
                tvItemTitle.setAlpha(0.2f);
            }
        }
        return convertView;
    }

}