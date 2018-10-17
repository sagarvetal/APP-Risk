package com.app.risk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void sendMessage(View view) {
        // Do something in response to button click
        Intent myIntent = new Intent(this, CreateMapActivity.class);
        startActivity(myIntent);
    }

}
