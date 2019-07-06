package com.app.risk.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.app.risk.R;
import com.app.risk.model.GamePlay;
import com.app.risk.model.Player;

import java.util.List;

/**
 * This is class to manage state of players
 * @author Akshita Angara
 * @version 1.0.0
 */
public class PlayerStateAdapter extends BaseAdapter {

    /**
     * playerList: List of the players
     */

    List<Player> playerList;
    /**
     * context: Instance of the invoking activity
     */
    private final Context context;
    /**
     * gamePlay: To manage the state and retrieve data
     */
    GamePlay gamePlay;

    /**
     * This is parametrized constructor
     * @param playerList - array of player
     * @param context - activity context
     */
    public PlayerStateAdapter(List<Player> playerList, GamePlay gamePlay, Context context){

        this.playerList = playerList;
        this.context = context;
        this.gamePlay = gamePlay;
    }

    /**
     * Returns count of list
     * {@inheritDoc}
     * @return
     */
    @Override
    public int getCount() {
        return playerList.size();
    }

    /**
     * Returns current item
     * {@inheritDoc}
     * @param position index position
     * @return
     */
    @Override
    public Player getItem(int position) {
        return playerList.get(position);
    }

    /**
     * Returns item at position
     * {@inheritDoc}
     * @param position index position
     * @return
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view =convertView;
        if(convertView==null){
           view =  LayoutInflater.from(context).inflate(R.layout.list_row_player_state,parent,false);
        }
        Player player =  playerList.get(position);
        TextView textView1 = view.findViewById(R.id.txt_player_data1);
        textView1.setText("Player : " + player.getName());
        TextView textView2 = view.findViewById(R.id.txt_player_data2);
        textView2.setText("Continents : " + Integer.toString(player.getContinentsOwnedByPlayer(gamePlay)));
        TextView textView3 = view.findViewById(R.id.txt_player_data3);
        textView3.setText("Percentage Of Map Owned : " + Float.toString(player.getPercentageOfMapOwnedByPlayer(gamePlay)));
        TextView textView4 = view.findViewById(R.id.txt_player_data4);
        textView4.setText("Armies : " + Integer.toString(player.getNoOfArmies()));

        return view;
    }
}
