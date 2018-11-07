package com.app.risk.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.app.risk.R;
import com.app.risk.model.Player;

import java.util.HashMap;
import java.util.List;

public class PlayerStateAdapter extends BaseAdapter {
    HashMap<Integer, Player> playerList;
    private final Context context;

    public PlayerStateAdapter(HashMap<Integer, Player> playerList, Context context){

        this.playerList = playerList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return playerList.size();
    }

    @Override
    public Player getItem(int position) {
        return playerList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view =convertView;
        if(convertView==null){
           view =  LayoutInflater.from(context).inflate(R.layout.list_row_player_state,parent,false);
        }

        TextView textView1 = view.findViewById(R.id.txt_player_data1);
        TextView textView2 = view.findViewById(R.id.txt_player_data2);
        TextView textView3 = view.findViewById(R.id.txt_player_data3);

        return view;
    }
}
