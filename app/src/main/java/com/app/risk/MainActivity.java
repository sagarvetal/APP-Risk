package com.app.risk;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button createUserMap = (Button) findViewById(R.id.createUserMap);
        createUserMap.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                Intent userMapCreate = new Intent(MainActivity.this, UserDrivenMaps.class);
                startActivity(userMapCreate);
            }
        });
    }
}
