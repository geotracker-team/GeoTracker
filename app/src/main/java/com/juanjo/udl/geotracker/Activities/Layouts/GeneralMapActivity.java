package com.juanjo.udl.geotracker.Activities.Layouts;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.ui.IconGenerator;
import com.juanjo.udl.geotracker.Activities.GlobalActivity.GlobalMapActivity;
import com.juanjo.udl.geotracker.JSONObjects.JSONRecord;
import com.juanjo.udl.geotracker.R;
import com.juanjo.udl.geotracker.Utilities.Constants;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

public class GeneralMapActivity extends GlobalMapActivity implements OnMapReadyCallback {

    private TextView txtLat, txtLon;
    private List<JSONRecord> records;
    private boolean followGPS = true, first = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_map);

        if (savedInstanceState != null) {
            followGPS = savedInstanceState.getBoolean("followGPS");
            if(followGPS) showToast(getString(R.string.txtFollowGPSOn), Toast.LENGTH_SHORT);
            else showToast(getString(R.string.txtFollowGPSOff), Toast.LENGTH_SHORT);
        }//Restore saved data

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);

        txtLat = findViewById(R.id.txtLat);
        txtLon = findViewById(R.id.txtLon);
    }//onCreate

    @Override
    public void onResume(){
        super.onResume();
        if(!first) {
            try {
                fillMap();
            } catch (Exception ex){
                processException(ex);
            }
        }//If it's not the first time, the map is loaded so we can refresh
    }//onResume

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean("followGPS", followGPS);//Save data
        super.onSaveInstanceState(savedInstanceState);
    }//onSaveInstanceState

    private void fillMap() throws IOException, JSONException {
        showDialog();
        loadData();
        addRecordsToMap();
        dismissDialog();
    }//fillMap

    private void loadData() throws IOException, JSONException {
        if(records!= null) records.clear();
        records = Constants.AuxiliarFunctions.getLocalSavedJsonRecords(this);
    }//loadData

    private void addRecordsToMap() {
        mMap.clear();
        for (JSONRecord r : records) {
            Bitmap icon;
            String title = r.getDescription();
            IconGenerator iconGenerator = new IconGenerator(this);
            iconGenerator.setTextAppearance(android.R.style.TextAppearance_Holo_Widget_ActionBar_Title_Inverse);
            iconGenerator.setColor(Color.BLUE);
            icon = iconGenerator.makeIcon(title);

            Marker m = addMarkerToMap(new LatLng(r.getLatitude(), r.getLongitude()), r.getDescription(), icon);
            m.setTag(r);
            m.setZIndex(1);//Set a weight of 1 to set priority over current location marker
        }
    }//addRecordsToMap

    //MAPS
    @Override
    public void onMapReady(GoogleMap map) {
        super.onMapReady(map);

        try {
            fillMap();
            first = false;
        } catch (Exception e) {
            processException(e);
        }

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if(!marker.equals(mCurrLocationMarker)){
                    JSONRecord record = (JSONRecord) marker.getTag();
                    if(record != null){
                        Intent it = new Intent(GeneralMapActivity.this, RecordViewActivity.class);
                        it.putExtra("record", record);
                        startActivity(it);
                    }//If exist the intent
                    else showToast(getString(R.string.txtError), Toast.LENGTH_SHORT);

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
            if(followGPS) moveCamera(position); //Move the position if follow GPS is enabled
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
        MenuItem item = menu.findItem(R.id.menuFollowGPS);
        if(item != null){
            if(followGPS) {
                item.setIcon(R.drawable.ic_media_pause_dark);
            } else {
                item.setIcon(R.drawable.ic_media_play_dark);
            }
        }
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
            case R.id.menuFollowGPS:
                followGPS = !followGPS; //Change follow state
                if(followGPS) {
                    Toast.makeText(getApplicationContext(), getString(R.string.txtFollowGPSOn),
                            Toast.LENGTH_SHORT).show();
                    item.setIcon(R.drawable.ic_media_pause_dark);
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.txtFollowGPSOff),
                            Toast.LENGTH_SHORT).show();
                    item.setIcon(R.drawable.ic_media_play_dark);
                }
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
