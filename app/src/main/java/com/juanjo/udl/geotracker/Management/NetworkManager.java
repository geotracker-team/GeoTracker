package com.juanjo.udl.geotracker.Management;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

public class NetworkManager extends BroadcastReceiver {
    private boolean isWifi = false;
    private boolean isMobile = false;
    public boolean isConnected = false;
    private boolean switchMobileDataOn = false;

    public NetworkManager(Context context){
        super();
        processEvent(context);
    }//Constructor

    @Override
    public void onReceive(Context context, Intent intent) {
        processEvent(context);
    }//onReceive

    private void processEvent(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        SharedPreferences pref = pref = PreferenceManager.getDefaultSharedPreferences(context);

        isConnected = false;
        isWifi = false;
        isMobile = false;
        switchMobileDataOn = pref.getBoolean("enable_net_data", false);

        if (ni != null && ni.isConnected()) {
            isConnected = true;
            if (ni.getType() == ConnectivityManager.TYPE_WIFI) {
                isWifi = true;
            } else if (ni.getType() == ConnectivityManager.TYPE_MOBILE) {
                isMobile = true;
            }
        }
    }//processEvent

    public boolean isConectionAllowed(){
        return (isConnected && (isWifi || (switchMobileDataOn == isMobile)) ); //If we have connection and it's the user wants it's ok
    }//isConectionAllowed
}
