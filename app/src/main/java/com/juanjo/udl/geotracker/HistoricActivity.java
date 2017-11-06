package com.juanjo.udl.geotracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;

public class HistoricActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historic);

        ArrayList<Register> registers = new ArrayList<Register>();

        registers.add(new Register("01/10/2017","David","EPS project ",  new Double(41.6109), new Double(0.6419), "Track point 1"));
        registers.add(new Register("02/10/2017","David","EPS project ",  new Double(41.6112), new Double(0.6421), "Track point 2"));
        registers.add(new Register("02/10/2017","David","EPS project ",  new Double(41.6118), new Double(0.6432), "Track point 3"));
        registers.add(new Register("05/10/2017","Xavier","EPS project ",  new Double(41.6100), new Double(0.6441), "Track point 4"));
        registers.add(new Register("12/10/2017","David","ETSEA project ",  new Double(41.6058), new Double(0.6403), "Track point 5"));

        RegisterAdapter itemsAdapter = new RegisterAdapter(this, registers);
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(itemsAdapter);



    }
}
