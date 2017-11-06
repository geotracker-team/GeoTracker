package com.juanjo.udl.geotracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Button btHistory = (Button) findViewById(R.id.bthistory);
        btHistory.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             Intent intent = new Intent(v.getContext(), HistoricActivity.class);
                                             startActivity(intent);
                                         }
                                     }
        );

        Button btMap = findViewById(R.id.btmap);
        btMap.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, GeneralMapActivity.class);
                startActivity(intent);
            }
        });

        Button btOptions = (Button) findViewById(R.id.btoptions);
        btOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, OptionsActivity.class);
                startActivity(intent);
            }
        });

    }
}
