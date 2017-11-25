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

import org.json.JSONException;
import org.json.JSONObject;

public class
RecordViewActivity extends Activity{

    // private HashMap<String, AdditionalField> additionalFieldHash = new HashMap<>();
    private EditText description;
    private TextView latitude, longitude, date, user;
    private Button btnSaveChanges;
    private JSONRecord jsonRecord;
    private JSONObject otherFields;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_view);

//        project = findViewById(R.id.projId);
        user = findViewById(R.id.userId);
        date = findViewById(R.id.dateId);
        latitude = findViewById(R.id.latid);
        longitude = findViewById(R.id.lenid);
        description = findViewById(R.id.desid);

        prepareDefaultFields();
       // prepareExtraFields();

        btnSaveChanges = findViewById(R.id.btnSaveChanges);
        btnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    saveChanges();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent in = new Intent(RecordViewActivity.this, GeneralMapActivity.class);
                startActivity(in);
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
        if(intent != null){
            jsonRecord = (JSONRecord) intent.getSerializableExtra("record");
            Log.d("json: ", jsonRecord.toString());
            user.setText(jsonRecord.getUsername());
            date.setText(jsonRecord.getDate());
            longitude.setText(String.valueOf(jsonRecord.getLongitude()));
            latitude.setText(String.valueOf(jsonRecord.getLatitude()));

            description.setText(jsonRecord.getDescription());
//            description.setFocusableInTouchMode(false);
            description.setFocusable(false);
            description.setClickable(false);
            description.setOnLongClickListener(editTextOnLongClickListener());
        }
    }

    private void prepareExtraFields(){
        otherFields = jsonRecord.getOtherFields();
        Log.d("Other field", otherFields.toString());

        LinearLayout fieldSet = findViewById(R.id.view_record_layout_id);

//        while (otherFields.keys().hasNext()){
//            Log.d("Extra field", otherFields.keys().next());

            TextView fieldName = new TextView(RecordViewActivity.this);
            EditText fieldValue = new EditText(RecordViewActivity.this);

            fieldName.setText("Tile test");
            fieldValue.setText("Value test");
            fieldValue.setBackgroundColor(Color.TRANSPARENT);
            fieldValue.setTextColor(Color.GRAY);
//            fieldValue.setFocusableInTouchMode(false);
            fieldValue.setFocusable(false);
            fieldValue.setClickable(false);
            fieldValue.setTag(fieldName.getText());
            fieldValue.setOnLongClickListener(editTextOnLongClickListener());

            fieldSet.addView(fieldName, fieldSet.getChildCount()-1);
            fieldSet.addView(fieldValue, fieldSet.getChildCount()-1);
//        }

    }

    private void saveChanges() throws JSONException {
//        while (otherFields.keys().hasNext()){

//            otherFields.put(otherFields.keys().next(), )


        jsonRecord.putValues();
        jsonRecord.save();
    }
}
