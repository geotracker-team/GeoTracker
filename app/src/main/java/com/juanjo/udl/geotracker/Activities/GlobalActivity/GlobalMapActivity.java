package com.juanjo.udl.geotracker.Activities.GlobalActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class GlobalMapActivity extends GlobalAppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    protected GoogleApiClient mGoogleApiClient;
    protected GoogleMap mMap;
    protected Marker mCurrLocationMarker;

    protected Location mLastLocation;
    protected LocationRequest mLocationRequest;
    protected boolean mRequestLocationUpdates = true;
    protected int UPDATE_INTERVAL = 5000, FASTEST_INTERVAL = 1000, DISPLACEMENT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (checkPlayServices()) {
            buildGoogleApiClient();
            createLocationRequest();
        }//Check and start location updates
    }//onCreate

    //GOOGLE API
    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) mGoogleApiClient.connect();//If exists connect
    }//onStart

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient != null) mGoogleApiClient.disconnect();//If exists disconnect
    }//onStop

    protected boolean checkPlayServices() {
        boolean ret = true;

        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if (result != ConnectionResult.SUCCESS) {
            ret = false;
            if (googleAPI.isUserResolvableError(result))
                googleAPI.getErrorDialog(this, result, 9000).show();
        }//If it's not active, make user correct it

        return ret;
    }//checkPlayServices

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }//buildGoogleApiClient

    public void onConnected(@Nullable Bundle bundle) {
        if (mRequestLocationUpdates) startLocationUpdates();
        displayLocation();
    }//onConnected

    @Override
    public void onConnectionSuspended(int i) {
    }//onConnectionSuspended

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }//onConnectionFailed

    //LOCATION
    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient != null) stopLocationUpdates(); //Stop location updates onPause to prevent battery consume
    }//onPause

    @Override
    public void onResume() {
        super.onResume();
        if (checkPlayServices()) {
            if (mGoogleApiClient != null && mGoogleApiClient.isConnected() && mRequestLocationUpdates) {
                startLocationUpdates();
            }
        }//If play services is on and we want location updates, start it
    }//onResume

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }//createLocationRequest

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            return;
        }//Check permissions
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }//startLocationUpdates

    protected void stopLocationUpdates() {
        if (mGoogleApiClient.isConnected())
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }//stopLocationUpdates

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location; //Update position
        displayLocation(); //Show it
    }//onLocationChanged

    protected void displayLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }//Check permissions
        else
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient); //Get position
    }//displayLocation

    //MAPS
    @Override
    public void onMapReady(GoogleMap map) {
        if (mMap == null) mMap = map;
        mMap.moveCamera(CameraUpdateFactory.zoomTo(19)); //Initial zoom
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID); //Hibrid Map
        mMap.getUiSettings().setZoomControlsEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }//Check permissions
        else mMap.setMyLocationEnabled(true);
    }//onMapReady

    protected Marker addMarkerToMap(LatLng position, String title){
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(position);
        markerOptions.title(title);

        return mMap.addMarker(markerOptions); //Add marker to map
    }//addMarkerToMap

    protected Marker addMarkerToMap(LatLng position, String title , Bitmap icon){
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(position);
        markerOptions.title(title);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));

        return mMap.addMarker(markerOptions);  //Add marker to map
    }//addMarkerToMap

    protected void moveCamera(LatLng position){
        CameraUpdate camera = CameraUpdateFactory.newLatLng(position); //Move the camera position
        mMap.moveCamera(camera);
    }//moveCamera
}
