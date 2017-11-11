package com.juanjo.udl.geotracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;


public class AddFieldActivity extends Activity implements AdapterView.OnItemSelectedListener {

    private int inputType = InputType.TYPE_CLASS_TEXT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_field);

        String[] typesString = getResources().getStringArray(R.array.field_types);

        Spinner spinner = findViewById(R.id.fieldtype);
        ArrayAdapter<String>adapter = new ArrayAdapter<>(AddFieldActivity.this,
                android.R.layout.simple_spinner_dropdown_item, typesString);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        Button btnCreateField = findViewById(R.id.btncreatefield);
        btnCreateField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = findViewById(R.id.fildetitle);
                String titleValue = editText.getText().toString();

                Intent intent = new Intent();
                intent.putExtra("title", titleValue);
                intent.putExtra("type", inputType);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }//onCreate

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        switch (position) {
            case 0: //Plain text
                inputType = InputType.TYPE_CLASS_TEXT;
                break;
            case 1: //Numeric
                inputType = InputType.TYPE_CLASS_NUMBER;
                break;
            case 2: //Temperature
                inputType = InputType.TYPE_CLASS_TEXT; //Pending to change
                break;
            case 3: //Humidity
                inputType = InputType.TYPE_CLASS_TEXT; //Pending to change
                break;
            case 4: //Pressure
                inputType = InputType.TYPE_CLASS_TEXT; //Pending to change
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        inputType = InputType.TYPE_CLASS_TEXT;
    }
}
