package com.juanjo.udl.geotracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Button btSend = (Button) findViewById(R.id.btsend);
        btSend.setOnClickListener( new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent();
//                                            Intent intent = new Intent(v.getContext(), SendRecordActivity.class );
                                            startActivity(intent);                                        }
                                    }
        );

        Button btHistory = (Button) findViewById(R.id.bthistory);
        btHistory.setOnClickListener( new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           Intent intent = new Intent();
//                                            Intent intent = new Intent(v.getContext(), SendRecordActivity.class );
                                           startActivity(intent);                                        }
                                   }
        );

        Button btMap = (Button) findViewById(R.id.btmap);
        btMap.setOnClickListener( new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              Intent intent = new Intent();
//                                            Intent intent = new Intent(v.getContext(), SendRecordActivity.class );
                                              startActivity(intent);                                        }
                                      }
        );

        Button btOptions = (Button) findViewById(R.id.btoptions);
        btOptions.setOnClickListener( new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              Intent intent = new Intent();
//                                            Intent intent = new Intent(v.getContext(), SendRecordActivity.class );
                                              startActivity(intent);                                        }
                                      }
        );

    }
}
