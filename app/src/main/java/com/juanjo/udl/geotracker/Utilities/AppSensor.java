package com.juanjo.udl.geotracker.Utilities;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;

import com.juanjo.udl.geotracker.R;
import com.juanjo.udl.geotracker.Utilities.Constants.FieldTypes;

public class AppSensor extends Activity implements SensorEventListener {
   private FieldTypes type;
   private SensorManager sensorManager;
   private Sensor sensor;
   private float value;

   public AppSensor(FieldTypes type){
       value = 0;
       this.type = type;
   }

    @Override
    public final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_registration);
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
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
