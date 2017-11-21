package com.juanjo.udl.geotracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.juanjo.udl.geotracker.GlobalActivity.GlobalMapActivity;
import com.juanjo.udl.geotracker.JSONObjects.JSONRecord;
import com.juanjo.udl.geotracker.Utilities.Constants;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

public class GeneralMapActivity extends GlobalMapActivity implements OnMapReadyCallback {

    private TextView txtLat, txtLon;
    private List<JSONRecord> records;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_map);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);

        txtLat = findViewById(R.id.txtLat);
        txtLon = findViewById(R.id.txtLon);

    }//onCreate

    private void fillMap() throws IOException, JSONException {
        loadData();
        addRecordsToMap();
    }//fillMap

    private void loadData() throws IOException, JSONException {
        if(records!= null) records.clear();
        records = Constants.AuxiliarFunctions.getLocalSavedJsonRecords(this);
    }//loadData

    private void addRecordsToMap() {
        mMap.clear();
        for (JSONRecord r : records) {
            Marker m = addMarkerToMap(new LatLng(r.getLatitude(), r.getLongitude()), r.getDescription());
        }
    }//addRecordsToMap

    //MAPS
    @Override
    public void onMapReady(GoogleMap map) {
        super.onMapReady(map);

        try {
            fillMap();
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if(!marker.equals(mCurrLocationMarker)){
                    JSONRecord record = (JSONRecord) marker.getTag();
                    Intent it = new Intent(GeneralMapActivity.this, RecordViewActivity.class);
                    it.putExtra("record", record);
                    startActivity(it);
                }
                return true;
            }
        });
    }//onMapReady

    @Override
    protected void displayLocation(){
        super.displayLocation();
        if(mLastLocation != null){
            LatLng position = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            moveCamera(position);
            if(mCurrLocationMarker != null) mCurrLocationMarker.remove();
            mCurrLocationMarker = addMarkerToMap(position, getString(R.string.txtCurrLocation));

            txtLat.setText(String.valueOf(position.latitude));
            txtLon.setText(String.valueOf(position.longitude));
        }
    }//displayLocation

    //MENU
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }//onCreateOptionsMenu

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Intent intent;
        switch (item.getItemId()){
            case R.id.menu_add_record:
                intent = new Intent(this, RecordRegistrationActivity.class);
                intent.putExtra("latitude", mLastLocation.getLatitude());
                intent.putExtra("longitude", mLastLocation.getLongitude());
                startActivity(intent);
                return true;
            case R.id.menu_history:
                intent = new Intent(this, HistoricActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_options:
                intent = new Intent(this, OptionsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }//onOptionsItemSelected
}
