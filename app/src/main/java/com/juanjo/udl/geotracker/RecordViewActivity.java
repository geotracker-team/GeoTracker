package com.juanjo.udl.geotracker;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.juanjo.udl.geotracker.JSONObjects.JSONRecord;
import com.juanjo.udl.geotracker.Utilities.AdditionalField;
import com.juanjo.udl.geotracker.Utilities.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class
RecordViewActivity extends Activity{

    private HashMap<String, AdditionalField> additionalFieldHash = new HashMap<>();
    private EditText description;
    private TextView latitude, longitude, date, user;
    private Button btnSaveChanges;
    private JSONRecord jsonRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_view);

        user = findViewById(R.id.userId);
        date = findViewById(R.id.dateId);
        latitude = findViewById(R.id.latid);
        longitude = findViewById(R.id.lenid);
        description = findViewById(R.id.desid);

        prepareDefaultFields();
        try {
            prepareExtraFields();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        btnSaveChanges = findViewById(R.id.btnSaveChanges);
        btnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(!description.getText().toString().equals("")){
                        saveChanges();
                        finish();
                    }
                    else
                        description.setError("the field can't be null");


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

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

    private void prepareDefaultFields(){
        Intent intent = getIntent();
        if(intent != null && intent.hasExtra("record")){
            jsonRecord = (JSONRecord) intent.getSerializableExtra("record");
            Log.d("json: ", jsonRecord.toString());
            user.setText(jsonRecord.getUserName());
            date.setText(jsonRecord.getDate());
            longitude.setText(String.valueOf(jsonRecord.getLongitude()));
            latitude.setText(String.valueOf(jsonRecord.getLatitude()));
            jsonRecord.getOtherFields();
            description.setText(jsonRecord.getDescription());
            description.setFocusable(false);
            description.setClickable(false);
            description.setOnLongClickListener(editTextOnLongClickListener());
        }
    }

    private void prepareExtraFields() throws JSONException {
        HashMap<String, Object> otherFields = (HashMap<String, Object>) jsonRecord.getOtherFields();
        LinearLayout fieldSet = findViewById(R.id.view_record_layout_id);
        for(String key : otherFields.keySet()){
            // set up of the visual components for each field, for the moment only display a test field
            TextView fieldName = new TextView(RecordViewActivity.this);
            EditText fieldValue = new EditText(RecordViewActivity.this);

            fieldName.setText(key);
            fieldValue.setText(((HashMap<String, String>) otherFields.get(key)).values().iterator().next());
            fieldValue.setBackgroundColor(Color.TRANSPARENT);
            fieldValue.setTextColor(Color.GRAY);
            fieldValue.setFocusable(false);
            fieldValue.setClickable(false);
            fieldValue.setOnLongClickListener(editTextOnLongClickListener());

            AdditionalField extraField = new AdditionalField(key,
                    Constants.FieldTypes.valueOf(((HashMap<String, String>) otherFields.get(key)).keySet().iterator().next()), fieldValue);
            additionalFieldHash.put(key, extraField);  //add the info to the map, in order to retrieve it later

            fieldSet.addView(fieldName, fieldSet.getChildCount()-1);
            fieldSet.addView(fieldValue, fieldSet.getChildCount()-1);
        }

    }

    private void saveChanges() throws JSONException {
        jsonRecord.setDescription(description.getText().toString());

        for(AdditionalField extra : additionalFieldHash.values()){
            JSONObject values = new JSONObject();
            values.put(String.valueOf(extra.getType()), extra.getContent().getText());
            jsonRecord.setField(extra.getName(), values);
        }

        jsonRecord.setContext(this);  // Set the current context to avoid possible errors
        jsonRecord.putValues();
        jsonRecord.save();
    }
}
