package com.juanjo.udl.geotracker.Activities.Layouts;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.juanjo.udl.geotracker.Activities.GlobalActivity.GlobalAppCompatActivity;
import com.juanjo.udl.geotracker.JSONObjects.JSONProject;
import com.juanjo.udl.geotracker.JSONObjects.JSONRecord;
import com.juanjo.udl.geotracker.R;
import com.juanjo.udl.geotracker.Utilities.AdditionalField;
import com.juanjo.udl.geotracker.Utilities.Constants.FieldTypes;

import java.util.Calendar;
import java.util.HashMap;

public class RecordRegistrationActivity extends GlobalAppCompatActivity implements SensorEventListener {

    private static final int FIELD_ADDED_SUCCESSFULLY = 0;
    private HashMap<String, AdditionalField> additionalFieldHash = new HashMap<>();
    private SensorManager sensorManager;
    private EditText projId, description, creator, latitude, longitude;
    private Double lat, lon;
    private JSONProject project;
    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_registration);

        findViewById(R.id.desid).requestFocus();
        mapView = findViewById(R.id.mapView);
        projId = findViewById(R.id.projId);
        description = findViewById(R.id.desid);
        creator = findViewById(R.id.creatorId);
        latitude = findViewById(R.id.latid);
        longitude = findViewById(R.id.lenid);

        Intent it = getIntent();
        if(it != null){
            lat = it.getDoubleExtra("latitude",0);
            lon = it.getDoubleExtra("longitude",0);
            project = (JSONProject) it.getSerializableExtra("project");
            latitude.setText(String.valueOf(lat));
            longitude.setText(String.valueOf(lon));
            projId.setText(project.getDescription());
        }//If the intent exists

        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                LatLng position = new LatLng(lat, lon);
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(position));
                googleMap.moveCamera(CameraUpdateFactory.zoomTo(19)); //Initial zoom
                googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID); //Hibrid Map
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(position);
                googleMap.addMarker(markerOptions);
            }
        });

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

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

                if(!description.getText().toString().isEmpty()){
                    saveJsonFile(v);
                    finish();
                }
                else
                    description.setError(getString(R.string.txtNoFields));
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
                additionalFieldHash.put(title, additionalField);

                switch (type) {
                    case TEXT:
                        textInput.setInputType(InputType.TYPE_CLASS_TEXT);
                        break;
                    case NUMERIC:
                        textInput.setInputType(InputType.TYPE_CLASS_NUMBER);
                        break;
                    default:
                        createNewSensor(type, title);
                }

                LinearLayout fieldSet = findViewById(R.id.record_layout);
                fieldSet.addView(textFiled, fieldSet.getChildCount()-2);
                fieldSet.addView(textInput, fieldSet.getChildCount()-2);
            }
        }
    }  // onActivityResult

    private void createNewSensor(FieldTypes type, String title){
        boolean initialized;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        switch (type){
            case TEMPERATURE:
                initialized = (sharedPreferences.getBoolean("tempS", false)
                        && initializeSensor(Sensor.TYPE_AMBIENT_TEMPERATURE));
                break;
            case HUMIDITY:
                initialized = (sharedPreferences.getBoolean("humS", false)
                        && initializeSensor(Sensor.TYPE_RELATIVE_HUMIDITY));
                break;
            case PRESSURE:
                initialized = (sharedPreferences.getBoolean("pressS", false)
                        && initializeSensor(Sensor.TYPE_PRESSURE));
                break;
            default:
                return;
        }
        if (!initialized){
            additionalFieldHash.get(title).getContent().setText("n/a");
            additionalFieldHash.get(title).getContent().setInputType(InputType.TYPE_CLASS_NUMBER);
        }
        else{
            additionalFieldHash.get(title).getContent().setEnabled(false);
            additionalFieldHash.get(title).getContent().setBackgroundColor(Color.TRANSPARENT);
        }

    }  // createNewSensor

    private boolean initializeSensor(int type){
        if(sensorManager != null){
            Sensor sensor = sensorManager.getDefaultSensor(type);
            if(sensor != null){
                sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
                return true;
            }
        }
        return false;
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
        mapView.onPause();
    }

    @Override
    public void onResume(){
        super.onResume();
        for(AdditionalField a : additionalFieldHash.values()){
            createNewSensor(a.getType(), a.getName());
        }
        mapView.onResume();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        setSensorFieldValues(event);
        Log.d("Sensor: ", String.valueOf(event.values[0]));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    private void setSensorFieldValues(SensorEvent event){
        switch (event.sensor.getType()){
            case Sensor.TYPE_AMBIENT_TEMPERATURE:
                additionalFieldHash.get(getResources().getString(R.string.temperature)).getContent().setText(String.valueOf(event.values[0]));
                break;
            case Sensor.TYPE_RELATIVE_HUMIDITY:
                additionalFieldHash.get(getResources().getString(R.string.humidity)).getContent().setText(String.valueOf(event.values[0]));
                break;
            case Sensor.TYPE_PRESSURE:
                additionalFieldHash.get(getResources().getString(R.string.pressure)).getContent().setText(String.valueOf(event.values[0]));
                break;
        }
    }  // setSensorFieldValues

    private void saveJsonFile(View v){
        JSONRecord jsonRecord;
        try {
            jsonRecord = new JSONRecord(v.getContext(),
                    description.getText().toString(),
                    Calendar.getInstance().getTime().toString(),
                    creator.getText().toString(),
                    Double.valueOf(latitude.getText().toString()),
                    Double.valueOf(longitude.getText().toString()),
                    project
            );

            for(AdditionalField a : additionalFieldHash.values()){
                jsonRecord.addNewField(a.getName(), a.getType(), a.getContent().getText().toString());
            }

            jsonRecord.save();
            showToast(getString(R.string.txtRecordSaved), Toast.LENGTH_SHORT);
        } catch (Exception e) {
            processException(e);
        }
    }  // saveJsonFile

}
