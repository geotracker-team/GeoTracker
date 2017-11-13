package com.juanjo.udl.geotracker;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;


public class AddFieldActivity extends Activity implements AdapterView.OnItemSelectedListener, SensorEventListener {

    private int inputType = InputType.TYPE_CLASS_TEXT;
    private SensorManager sensorManager;
    private Sensor sensor;
    private EditText editText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_field);

        String[] typesString = getResources().getStringArray(R.array.field_types);

        Spinner spinner = findViewById(R.id.fieldtype);
        ArrayAdapter<String>adapter = new ArrayAdapter<>(AddFieldActivity.this,
                android.R.layout.simple_spinner_dropdown_item, typesString);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);

        editText = findViewById(R.id.fildetitle);

        Button btnCreateField = findViewById(R.id.btncreatefield);
        btnCreateField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("title", editText.getText().toString());
                intent.putExtra("type", inputType);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }//onCreate

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        switch (position) {
            case 0: //Plain text
                inputType = InputType.TYPE_CLASS_TEXT;
                break;
            case 1: //Numeric
                inputType = InputType.TYPE_CLASS_NUMBER;
                break;
            case 2: //Temperature
                editText.setText(getResources().getString(R.string.temperature));
                inputType = InputType.TYPE_CLASS_TEXT; //Pending to change
                break;
            case 3: //Humidity
                editText.setText(getResources().getString(R.string.humidity));
                inputType = InputType.TYPE_CLASS_TEXT; //Pending to change
                break;
            case 4: //Pressure
                editText.setText(getResources().getString(R.string.pressure));
                inputType = InputType.TYPE_CLASS_TEXT; //Pending to change
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        inputType = InputType.TYPE_CLASS_TEXT;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float ambient_temperature = event.values[0];
        EditText editText = findViewById(R.id.fildetitle);
        editText.setText(String.valueOf(ambient_temperature) + " Cº");
        Log.d("Sensor log: ", Float.toString(ambient_temperature));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d("Sensor log:  ", "Changed");
    }
}
