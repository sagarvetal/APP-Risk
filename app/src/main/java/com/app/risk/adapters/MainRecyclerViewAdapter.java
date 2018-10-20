package com.app.risk.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.app.risk.view.EditMap;
import com.app.risk.view.UserDrivenMapsActivity;
import com.app.risk.view.MapSelectionActivity;
import com.app.risk.R;

import java.io.IOException;
import java.util.ArrayList;

/**
 * MainRecyclerView Adapter which iterates the recyclerview multiple times
 * @author Himanshu Kohli
 * @version 1.0.0
 */
public class MainRecyclerViewAdapter extends RecyclerView.Adapter<MainRecyclerViewAdapter.CardViewHolder> {

    private ArrayList<String> cardArrayList;
    private Context invokingActivity;


    /**
     * Parameterized constructor of the adapter
     * @param cardArrayList : initializes the arraylist of the menu
     * @param invokingActivity: The context of the activity
     */
    public MainRecyclerViewAdapter(ArrayList<String> cardArrayList, Context invokingActivity) {
        this.cardArrayList = cardArrayList;
        this.invokingActivity = invokingActivity;
    }


    /**
     * {@inheritDoc}
     *
     * @param parent : parent of the class
     * @param viewType
     * @return: Object of the cardview holder
     */
    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_screen_cardview,parent,false);
        final CardViewHolder cardViewHolder = new CardViewHolder(view);
        return cardViewHolder;
    }

    /**
     * {@inheritDoc}
     * Bind View to data
     * @param holder: CardHolder class object
     * @param position: position of the adapter
     */
    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        holder.textView.setText(cardArrayList.get(position));
    }

    /**
     * Gets the item count of the adapter
     * @return: gets the item count of adapter
     */
    @Override
    public int getItemCount() {
        return cardArrayList.size();
    }

    /**
     * CardViewHolder inner class of the recyclerview
     * Holds the referrence to the elements of the card
     */
    public class CardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private CardView cardView;
        private TextView textView;
        private View layoutView;

        /**
         * Constructor of the CardViewHolder class
         * @param itemView : holds the view
         */
        public CardViewHolder(final View itemView) {
            super(itemView);

            layoutView = itemView;
            cardView = (CardView)itemView.findViewById(R.id.main_cardview_card);
            textView = (TextView)itemView.findViewById(R.id.main_cardview_textview);

            cardView.setOnClickListener(this);

        }

        /**
         * {@inheritDoc}
         * Click listner method
         * @param v: view of the
         */
        @Override
        public void onClick(View v) {

            if(v == cardView){

                switch(cardArrayList.get(getAdapterPosition())){

                    case "Play":
                        invokingActivity.startActivity(new Intent(invokingActivity.getApplicationContext(), MapSelectionActivity.class));
                        break;
                    case "Create Map":
                        final Intent userMapCreate = new Intent(invokingActivity.getApplicationContext(), UserDrivenMapsActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putBoolean("edit Mode",false);
                        userMapCreate.putExtras(bundle);
                        invokingActivity.startActivity(userMapCreate);
                        break;
                    case "Edit Map":
                        //Intent editMap = new Intent(invokingActivity.getApplicationContext(), EditMap.class);
                        //invokingActivity.startActivity(editMap);
                        break;
                    case "Help":
                        Toast.makeText(invokingActivity, "" + cardArrayList.get(getAdapterPosition()), Toast.LENGTH_SHORT).show();
                        break;
                    case "Setting":
                        Toast.makeText(invokingActivity, "" + cardArrayList.get(getAdapterPosition()), Toast.LENGTH_SHORT).show();
                        break;
                    case "Exit":
                        ((Activity)invokingActivity).finishAffinity();
                            System.exit(0);
                        break;
                }

            }
        }
    }


}
