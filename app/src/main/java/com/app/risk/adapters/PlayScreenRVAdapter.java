package com.app.risk.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.risk.R;

import java.util.ArrayList;

public class PlayScreenRVAdapter extends RecyclerView.Adapter<PlayScreenRVAdapter.PlayScreenViewHolder> {


    private ArrayList<String> pList;
    private Context context;
    public PlayScreenRVAdapter(Context context,ArrayList<String> pList) {
        this.pList = pList;
        this.context = context;
    }


    @NonNull
    @Override
    public PlayScreenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.play_screen_card,parent,false);
        final PlayScreenViewHolder adapter = new PlayScreenViewHolder(view);
        return adapter;
    }

    @Override
    public void onBindViewHolder(@NonNull PlayScreenViewHolder holder, int position) {
        holder.countryName.setText(pList.get(position));

    }

    @Override
    public int getItemCount() {
        return pList.size();
    }

    public class PlayScreenViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private CardView cardView;
        private TextView countryName,armies,continent;
        private ListView adjacentCountries;
        private View view;

        public PlayScreenViewHolder(View itemView) {
            super(itemView);
            view = itemView;

            cardView = itemView.findViewById(R.id.play_screen_card_carview);
            countryName = itemView.findViewById(R.id.play_screen_card_country_name);
            armies = itemView.findViewById(R.id.play_screen_card_armies);
            continent = itemView.findViewById(R.id.play_screen_card_continent);
            adjacentCountries = itemView.findViewById(R.id.play_screen_card_neighbours);

            ArrayList<String> arrayList = new ArrayList<>();
            for(int i=1;i<6;i++){
                arrayList.add("neighbour "+ i);
            }

            ArrayAdapter adapter = new ArrayAdapter(context,android.R.layout.simple_list_item_1,arrayList);
            adjacentCountries.setAdapter(adapter);
            cardView.setOnClickListener(this);


            ViewGroup.LayoutParams layoutParams = adjacentCountries.getLayoutParams();
            layoutParams.height = arrayList.size() * 173;
            adjacentCountries.setLayoutParams(layoutParams);
            adjacentCountries.requestLayout();

        }


        @Override
        public void onClick(View v) {

            if(v == cardView){
                Toast.makeText(context, "" + getAdapterPosition(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
