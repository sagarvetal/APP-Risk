package com.app.risk.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.app.risk.R;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment {

    TextView textView;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map_view_layout,container,false);

        CardView cardView = view.findViewById(R.id.map_view_layout_cardview);
        textView = view.findViewById(R.id.map_view_layout_textview);
        textView.setText(getArguments().getString("MAP_NAME"));
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(getActivity())
                        .setMessage("Load Map")
                        .setNegativeButton("No",null)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getActivity(), "" + textView.getText().toString(), Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(getActivity(),PlayerSelectionActivity.class);
                                intent.putExtra("MAP_NAME",textView.getText().toString().trim());
                                startActivity(intent);
                            }
                        }).create().show();
            }
        });
        return view;
    }


}
