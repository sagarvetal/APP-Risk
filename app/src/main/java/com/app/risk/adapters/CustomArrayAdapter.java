package com.app.risk.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import com.app.risk.R;
import com.app.risk.model.GamePlay;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Adapter class to display the list of countries in edit map
 *
 * @author Urvi
 * @version 1.0.0
 */

public class CustomArrayAdapter extends ArrayAdapter{

    /**
     * gameplay : To manage the state and retrieve data
     * objects: name of the countries
     */
    private GamePlay gamePlay;
    private ArrayList<String> objects;

    /**
     * This the paramertized constructor
     * @param context
     * @param resource
     * @param textViewResourceId
     * @param objects
     * @param gamePlay
     */
    public CustomArrayAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull ArrayList<String> objects, GamePlay gamePlay) {
        super(context, resource, textViewResourceId, objects);
        this.gamePlay = gamePlay;
        this.objects = objects;
    }


    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view =  super.getView(position, convertView, parent);
        TextView textView = view.findViewById(R.id.adapter_textview_text);
        int colorCode = gamePlay.getCountries().get(objects.get(position)).getPlayer().getColorCode();
        textView.setTextColor(colorCode);
        return view;
    }
}
