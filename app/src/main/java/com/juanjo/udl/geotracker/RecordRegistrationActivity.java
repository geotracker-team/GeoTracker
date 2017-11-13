package com.juanjo.udl.geotracker;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.juanjo.udl.geotracker.Utilities.AppSensor;
import com.juanjo.udl.geotracker.Utilities.Constants;
import com.juanjo.udl.geotracker.Utilities.Constants.FieldTypes;

public class RecordRegistrationActivity extends Activity {

    private static final int FIELD_ADDED_SUCCESSFULLY = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_registration);

        Button btnAddField = findViewById(R.id.btnaddfield);
        btnAddField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecordRegistrationActivity.this, AddFieldActivity.class);
                startActivityForResult(intent, 0);
            }
        });
    }//onCreate

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FIELD_ADDED_SUCCESSFULLY) {
            if (resultCode == RESULT_OK) {

                TextView textFiled = new TextView(RecordRegistrationActivity.this);
                textFiled.setText(data.getStringExtra("title"));

                EditText textInput = new EditText(RecordRegistrationActivity.this);
                textInput.setInputType(data.getIntExtra("inputType", 1));

                FieldTypes type = (FieldTypes) data.getSerializableExtra("type");

                if(type == FieldTypes.TEMPERATURE || type == FieldTypes.HUMIDITY || type == FieldTypes.PRESSURE){
                    AppSensor sensor = new AppSensor(type);
                    textInput.setEnabled(false);
                    textInput.setBackgroundColor(Color.TRANSPARENT);
                    textInput.setText(String.valueOf(sensor.getValue()));
                }

                LinearLayout fieldSet = findViewById(R.id.record_layout);
                fieldSet.addView(textFiled, fieldSet.getChildCount()-2);
                fieldSet.addView(textInput, fieldSet.getChildCount()-2);
            }
        }
    }
}
