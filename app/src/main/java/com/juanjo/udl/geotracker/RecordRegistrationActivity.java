package com.juanjo.udl.geotracker;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.juanjo.udl.geotracker.JSONObjects.JSONRecord;
import com.juanjo.udl.geotracker.Utilities.AdditionalField;
import com.juanjo.udl.geotracker.Utilities.AppSensor;
import com.juanjo.udl.geotracker.Utilities.Constants.FieldTypes;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RecordRegistrationActivity extends Activity {

    private static final int FIELD_ADDED_SUCCESSFULLY = 0;
    private List<AdditionalField> newFieldsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_registration);

        Button btnAddField = findViewById(R.id.btnAddFieldId);
        btnAddField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecordRegistrationActivity.this, AddFieldActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        Button btnSend = findViewById(R.id.btnSendId);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveJsonFile(v);

                Intent in = new Intent(RecordRegistrationActivity.this, GeneralMapActivity.class);
                startActivity(in);
            }
        });

    }//onCreate

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FIELD_ADDED_SUCCESSFULLY) {
            if (resultCode == RESULT_OK) {

                String title = data.getStringExtra("title");
                TextView textFiled = new TextView(RecordRegistrationActivity.this);
                textFiled.setText(title);

                FieldTypes type = (FieldTypes) data.getSerializableExtra("type");
                EditText textInput = new EditText(RecordRegistrationActivity.this);

                AdditionalField additionalField = new AdditionalField(title, type, textInput);
                newFieldsList.add(additionalField);

                switch (type){
                    case TEXT:
                        textInput.setInputType(InputType.TYPE_CLASS_TEXT);
                        break;
                    case NUMERIC:
                    textInput.setInputType(InputType.TYPE_CLASS_NUMBER);
                        break;
                    default:
                        textInput.setText(getSensorValue(type));
                        textInput.setEnabled(false);
                        textInput.setBackgroundColor(Color.TRANSPARENT);
                }

                LinearLayout fieldSet = findViewById(R.id.record_layout);
                fieldSet.addView(textFiled, fieldSet.getChildCount()-2);
                fieldSet.addView(textInput, fieldSet.getChildCount()-2);
            }
        }
    }

    private String getSensorValue(FieldTypes type){
        AppSensor sensor;
        switch (type){
            case TEMPERATURE:
                sensor = new AppSensor(Sensor.TYPE_AMBIENT_TEMPERATURE, this);
                break;
            case HUMIDITY:
                sensor = new AppSensor(Sensor.TYPE_RELATIVE_HUMIDITY, this);
                break;
            case PRESSURE:
                sensor = new AppSensor(Sensor.TYPE_PRESSURE, this);
                break;
            default:
                sensor = new AppSensor(Sensor.TYPE_AMBIENT_TEMPERATURE, this);
        }
        return  String.valueOf(sensor.getValue());
    }

    private void saveJsonFile(View v){
        EditText description = findViewById(R.id.desid);
        EditText date = findViewById(R.id.dateId);
        EditText creator = findViewById(R.id.creatorId);
        EditText latitude = findViewById(R.id.latid);
        EditText longitude = findViewById(R.id.lenid);
        JSONRecord jsonRecord;
        try {
            jsonRecord = new JSONRecord(v.getContext(),
                    description.getText().toString(),
                    date.getText().toString(),
                    creator.getText().toString(),
                    Double.valueOf(latitude.getText().toString()),
                    Double.valueOf(longitude.getText().toString()));

            for(AdditionalField a : newFieldsList){
                Log.d("New field: ", a.getName() + " " + a.getType().toString() + " " + a.getContent().getText());
                jsonRecord.addNewField(a.getName(), a.getType(), a.getContent().getText().toString());
            }

            jsonRecord.save();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
