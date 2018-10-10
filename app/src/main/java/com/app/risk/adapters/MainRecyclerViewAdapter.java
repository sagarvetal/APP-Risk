package com.app.risk.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.app.risk.view.MapSelectionActivity;
import com.app.risk.view.PlaceArmiesActivity;
import com.app.risk.R;

import java.util.ArrayList;

public class MainRecyclerViewAdapter extends RecyclerView.Adapter<MainRecyclerViewAdapter.CardViewHolder> {

    private ArrayList<String> cardArrayList;
    private Context invokingActivity;

    public MainRecyclerViewAdapter(ArrayList<String> cardArrayList, Context invokingActivity) {
        this.cardArrayList = cardArrayList;
        this.invokingActivity = invokingActivity;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_screen_cardview,parent,false);
        final CardViewHolder cardViewHolder = new CardViewHolder(view);
        return cardViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        holder.textView.setText(cardArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return cardArrayList.size();
    }


    public class CardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private CardView cardView;
        private TextView textView;
        private View layoutView;

        public CardViewHolder(final View itemView) {
            super(itemView);

            layoutView = itemView;
            cardView = (CardView)itemView.findViewById(R.id.main_cardview_card);
            textView = (TextView)itemView.findViewById(R.id.main_cardview_textview);

            cardView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            if(v == cardView){

                switch(cardArrayList.get(getAdapterPosition())){

                    case "Play":
                        invokingActivity.startActivity(new Intent(invokingActivity.getApplicationContext(), MapSelectionActivity.class));
                        break;
                    case "Create Map":
                        Toast.makeText(invokingActivity, "" + cardArrayList.get(getAdapterPosition()), Toast.LENGTH_SHORT).show();
                        break;
                    case "Edit Map":
                        Toast.makeText(invokingActivity, "" + cardArrayList.get(getAdapterPosition()), Toast.LENGTH_SHORT).show();
                        break;
                    case "Help":
                        Toast.makeText(invokingActivity, "" + cardArrayList.get(getAdapterPosition()), Toast.LENGTH_SHORT).show();
                        break;
                    case "Setting":
                        Toast.makeText(invokingActivity, "" + cardArrayList.get(getAdapterPosition()), Toast.LENGTH_SHORT).show();
                        break;
                    case "Exit":
                        Toast.makeText(invokingActivity, "" + cardArrayList.get(getAdapterPosition()), Toast.LENGTH_SHORT).show();
                        invokingActivity.startActivity(new Intent(invokingActivity.getApplicationContext(), PlaceArmiesActivity.class));
                        break;
                }

            }
        }
    }


}
