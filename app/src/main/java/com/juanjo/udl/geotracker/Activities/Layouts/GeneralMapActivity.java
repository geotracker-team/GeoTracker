package com.juanjo.udl.geotracker.Activities.Layouts;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.maps.android.ui.IconGenerator;
import com.juanjo.udl.geotracker.Activities.GlobalActivity.GlobalMapActivity;
import com.juanjo.udl.geotracker.JSONObjects.JSONProject;
import com.juanjo.udl.geotracker.JSONObjects.JSONRecord;
import com.juanjo.udl.geotracker.JSONObjects.JSONUser;
import com.juanjo.udl.geotracker.R;
import com.juanjo.udl.geotracker.Utilities.Constants;
import com.juanjo.udl.geotracker.Utilities.DataHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.List;

public class GeneralMapActivity extends GlobalMapActivity implements OnMapReadyCallback {

    private TextView txtLat, txtLon;
    private List<JSONRecord> records;
    private JSONProject project;
    private JSONUser user;
    private boolean followGPS = true, first = true;
    private double mapLatitude, mapLongitude;
    private float mapZoom;
    private static final int EDIT = 01, NEW = 00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_general_map);

        Intent it = getIntent();
        if(it != null){
            project = (JSONProject) it.getSerializableExtra("project");
            user = (JSONUser) it.getSerializableExtra("user");
            setActionBartTitle(project.getName());
        }

        if (savedInstanceState != null) {
            followGPS = savedInstanceState.getBoolean("followGPS");
            first = savedInstanceState.getBoolean("first");
            mapZoom = savedInstanceState.getFloat("mapZoom");
            mapLatitude = savedInstanceState.getDouble("mapLatitude");
            mapLongitude = savedInstanceState.getDouble("mapLongitude");
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
        if(!first && mMap != null) {
            try {
                fillMap(first);
            } catch (Exception e) {
                processException(e);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean("followGPS", followGPS);//Save data
        savedInstanceState.putBoolean("first", first);
        savedInstanceState.putFloat("mapZoom", mMap.getCameraPosition().zoom);
        savedInstanceState.putDouble("mapLatitude", mMap.getCameraPosition().target.latitude);
        savedInstanceState.putDouble("mapLongitude", mMap.getCameraPosition().target.longitude);
        super.onSaveInstanceState(savedInstanceState);
    }//onSaveInstanceState

    private void sendNewRecordToServer(final JSONRecord newRecord){
        DataHandler h = new DataHandler(this){
            @Override
            protected void isOk(Object obj) throws Exception {
                int newRecordId = (int) obj;
                newRecord.setIdRecord(newRecordId);
                newRecord.setSync(true);
                newRecord.save();
            }
        };
        if(isConnected()) {
            try {
                dataManagement.addRecord(user.getName(), user.getPass(), newRecord, h);
            } catch (Exception e) {
                processException(e);
            }
        }
    }//sendNewRecordToServer

    private void sendEditedRecordToServer(final JSONRecord editedRecord) {
        DataHandler h = new DataHandler(this){
            @Override
            protected void isOk(Object obj) throws Exception {
                editedRecord.setSync(true);
                editedRecord.setEdited(false);
                editedRecord.save();
                showToast((String) obj, Toast.LENGTH_SHORT);
            }
        };
        if(isConnected()) {
            try {
                dataManagement.editRecord(user.getName(), user.getPass(), editedRecord, h);
            } catch (Exception e) {
                processException(e);
            }
        }
    }//sendEditedRecordToServer

    private void fillMap(boolean isRefresh) throws IOException, JSONException, InterruptedException {
        showDialog();
        mMap.clear();
        if(isRefresh) loadServerData();
        else processData();
    }//fillMap

    private void loadServerData() throws IOException, JSONException, InterruptedException {
        DataHandler getServer = new DataHandler(this){
            @Override
            protected void isOk(Object obj) throws Exception {
                readServerData(obj);
                processData();
            }
        };
        if(isConnected()){
            sendPendingRecords();
            dataManagement.getRecordsOfProject(user.getName(), user.getPass(), project.getId(), getServer);
        }//If there are connection, load from the server
        else processData(); //read offline
    }//loadData

    private void processData() throws IOException, JSONException, InterruptedException {
        if(records!= null) records.clear();
        synchronized (this){
            wait(1500);
        }
        records = Constants.AuxiliarFunctions.getLocalSavedJsonRecords(this, project.getId());
        addRecordsToMap();
        dismissDialog();
    }//processData

    private void sendPendingRecords() throws IOException, JSONException {
        for(JSONRecord r : Constants.AuxiliarFunctions.getLocalSavedJsonRecords(this, project.getId())){
            if(!r.isSync()) sendNewRecordToServer(r);
            else if(r.isEdited()) sendEditedRecordToServer(r);
        }//send not synched records to the server
    }//sendPendingRecords

    private void readServerData(Object obj) throws JSONException, IOException {
        if(obj instanceof JSONArray){
            JSONArray records = (JSONArray) obj;
            for(int i = 0; i < records.length(); i++){
                JsonObject tmp = new Gson().fromJson(records.get(i).toString(), JsonObject.class);
                JSONRecord record = new JSONRecord(this, tmp);
                record.save();
            }//Save the records
        } else {
            processException(new Exception((String)obj));
        }//If there is not a JSONArray process it as an error
    }//readServerData

    private void addRecordsToMap() {
        if(records.size() == 0) return;
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
            fillMap(first);
            if(!first){
                LatLng position = new LatLng(mapLatitude, mapLongitude);
                moveCamera(position);
                CameraUpdate zoom = CameraUpdateFactory.zoomTo(mapZoom);
                mMap.moveCamera(zoom);
            }//Reset the map to the old camera position
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
                        startActivityForResult(it, EDIT);
                    }//If exist the intent
                    else showToast(getString(R.string.txtError), Toast.LENGTH_SHORT);
                }
                return true;
            }
        });

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(20);//Add 20ms of vibration to improve app feedback
                Intent it = new Intent(GeneralMapActivity.this, RecordRegistrationActivity.class);
                it.putExtra("latitude", latLng.latitude);
                it.putExtra("longitude", latLng.longitude);
                it.putExtra("project", project);
                it.putExtra("user", user);
                startActivityForResult(it, NEW);
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
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.map_menu, menu);
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
                intent.putExtra("project", project);
                intent.putExtra("user", user);
                startActivityForResult(intent, NEW);
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }//onOptionsItemSelected
}
