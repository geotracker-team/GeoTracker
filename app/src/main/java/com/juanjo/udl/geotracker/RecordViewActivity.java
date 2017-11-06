package com.juanjo.udl.geotracker;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RecordViewActivity extends Activity{

    EditText desid, temid, humid;
    Button btnSaveChanges;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_view);

        desid = findViewById(R.id.desid);
        temid = findViewById(R.id.temid);
        humid = findViewById(R.id.humid);
        btnSaveChanges = findViewById(R.id.btnSaveChanges);
        prepareFields();
    }

    private void prepareFields(){
        //Eliminar els listeners per evitar edicions
        desid.setFocusableInTouchMode(false);
        desid.setFocusable(false);
        desid.setClickable(false);
        temid.setFocusableInTouchMode(false);
        temid.setFocusable(false);
        temid.setClickable(false);
        humid.setFocusableInTouchMode(false);
        humid.setFocusable(false);
        humid.setClickable(false);

        //Afegir onLongClick que habilitara/deshabilitarà l'edició
        desid.setOnLongClickListener(editTextOnLongClickListener());
        temid.setOnLongClickListener(editTextOnLongClickListener());
        humid.setOnLongClickListener(editTextOnLongClickListener());
    }//prepareFields

    private View.OnLongClickListener editTextOnLongClickListener(){
        return new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                EditText et = (EditText) v;

                et.setFocusableInTouchMode(!et.isClickable());
                et.setFocusable(!et.isClickable());
                et.setClickable(!et.isClickable());
                if(et.isClickable()) et.setTextColor(Color.BLACK);
                else et.setTextColor(Color.GRAY);
                btnSaveChanges.setVisibility(View.VISIBLE);

                return true;
            }
        };
    }//editTextOnLongClickListener
}
