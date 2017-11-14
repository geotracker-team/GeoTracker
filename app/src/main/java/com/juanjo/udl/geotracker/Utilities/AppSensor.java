package com.juanjo.udl.geotracker.Utilities;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import com.juanjo.udl.geotracker.Utilities.Constants.FieldTypes;

public class AppSensor implements SensorEventListener {

   private float value;

   public AppSensor(int type, Context context){
       value = 0;
       SensorManager sensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
       Sensor sensor = sensorManager.getDefaultSensor(type);
       sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
   }

    @Override
    public void onSensorChanged(SensorEvent event) {
        value = event.values[0];
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d("Sensor log:  ", "Changed");
    }

    public float getValue(){
       return value;
    }
}
