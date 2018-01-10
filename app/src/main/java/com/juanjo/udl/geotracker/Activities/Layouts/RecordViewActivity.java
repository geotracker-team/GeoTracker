package com.juanjo.udl.geotracker.Activities.Layouts;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;
import com.juanjo.udl.geotracker.Activities.GlobalActivity.GlobalAppCompatActivity;
import com.juanjo.udl.geotracker.JSONObjects.JSONRecord;
import com.juanjo.udl.geotracker.R;
import com.juanjo.udl.geotracker.Utilities.AdditionalField;
import com.juanjo.udl.geotracker.Utilities.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class RecordViewActivity extends GlobalAppCompatActivity {

    private HashMap<String, AdditionalField> additionalFieldHash = new HashMap<>();
    private EditText description;
    private TextView latitude, longitude, date, user, projId;
    private Button btnSaveChanges;
    private JSONRecord jsonRecord;
    private boolean locked = true;
    private ArrayList<EditText> editTextsList = new ArrayList<>();
    private Double lat, lon;
    private MapView mapView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_view);

        user = findViewById(R.id.idUser);
        date = findViewById(R.id.dateId);
        projId = findViewById(R.id.projId);
        latitude = findViewById(R.id.latid);
        longitude = findViewById(R.id.lenid);
        description = findViewById(R.id.desid);
        mapView = findViewById(R.id.mapView);

        Intent intent = getIntent();
        if(intent != null && intent.hasExtra("record")) {
            jsonRecord = (JSONRecord) intent.getSerializableExtra("record");
            lat = jsonRecord.getLatitude();
            lon = jsonRecord.getLongitude();
        } else {
            showToast(getString(R.string.txtError), Toast.LENGTH_SHORT);
            finish();
        }//If there is an error kill the view

        try {
            prepareDefaultFields();
            prepareExtraFields();
            setEditableFields();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        btnSaveChanges = findViewById(R.id.btnSaveChanges);
        btnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(!description.getText().toString().isEmpty()){
                        saveChanges();
                        finish();
                    }
                    else description.setError(getString(R.string.txtNoFields));
                } catch (JSONException e) {
                    processException(e);
                }
            }
        });

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                LatLng position = new LatLng(lat, lon);
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(position));
                googleMap.moveCamera(CameraUpdateFactory.zoomTo(19)); //Initial zoom
                googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID); //Hibrid Map

                String title = jsonRecord.getDescription();
                IconGenerator iconGenerator = new IconGenerator(RecordViewActivity.this);
                iconGenerator.setTextAppearance(android.R.style.TextAppearance_Holo_Widget_ActionBar_Title_Inverse);
                iconGenerator.setColor(Color.BLUE);
                Bitmap icon = iconGenerator.makeIcon(title);

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(position);
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
                googleMap.addMarker(markerOptions);
            }
        });
    }//onCreate

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }//onPause

    @Override
    public void onResume(){
        super.onResume();
        mapView.onResume();
    }//onResume

    //MENU
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.record_menu, menu);
        return true;
    }//onCreateOptionsMenu

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menu_lock:
                locked = !locked;
                setEditableFields();
                btnSaveChanges.setVisibility(View.VISIBLE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }//onOptionsItemSelected

    private void prepareDefaultFields(){
        user.setText(jsonRecord.getUserName());
        date.setText(jsonRecord.getDate());
        longitude.setText(String.valueOf(jsonRecord.getLongitude()));
        latitude.setText(String.valueOf(jsonRecord.getLatitude()));
        projId.setText(jsonRecord.getProjectName());
        jsonRecord.getOtherFields();
        description.setText(jsonRecord.getDescription());
        editTextsList.add(description);
    }//prepareDefaultFields

    private void prepareExtraFields() throws JSONException {
        HashMap<String, Object> otherFields = (HashMap<String, Object>) jsonRecord.getOtherFields();
        LinearLayout fieldSet = findViewById(R.id.view_record_layout_id);

        for(String key : otherFields.keySet()){
            // set up of the visual components for each field, for the moment only display a test field
            TextView fieldName = new TextView(RecordViewActivity.this);
            EditText fieldValue = new EditText(RecordViewActivity.this);

            fieldName.setText(key);
            fieldValue.setText(((HashMap<String, String>) otherFields.get(key)).values().iterator().next());

            editTextsList.add(fieldValue);

            AdditionalField extraField = new AdditionalField(key,
                    Constants.FieldTypes.valueOf(((HashMap<String, String>) otherFields.get(key)).keySet().iterator().next()), fieldValue);
            additionalFieldHash.put(key, extraField);  //add the info to the map, in order to retrieve it later

            fieldSet.addView(fieldName, fieldSet.getChildCount()-1);
            fieldSet.addView(fieldValue, fieldSet.getChildCount()-1);
        }
    }//prepareExtraFields

    private void setEditableFields() {
       for (EditText et : editTextsList){
           et.setBackgroundColor(Color.TRANSPARENT);
           et.setFocusableInTouchMode(!locked);
           et.setFocusable(!locked);
           et.setClickable(!locked);
           et.setTextColor((locked ? Color.GRAY : Color.BLACK));
       }
    }//setFields

    private void saveChanges() throws JSONException {
        jsonRecord.setDescription(description.getText().toString());

        for(AdditionalField extra : additionalFieldHash.values()){
            JSONObject values = new JSONObject();
            values.put(String.valueOf(extra.getType()), extra.getContent().getText());
            jsonRecord.setField(extra.getName(), values);
        }

        jsonRecord.setContext(this);  // Set the current context to avoid possible errors
        jsonRecord.putValues();
        jsonRecord.setEdited(true);
        jsonRecord.save();
        showToast(getString(R.string.txtRecordSaved), Toast.LENGTH_SHORT);
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }
}
