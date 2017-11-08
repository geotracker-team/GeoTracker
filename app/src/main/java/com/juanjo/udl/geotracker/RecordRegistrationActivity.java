package com.juanjo.udl.geotracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RecordRegistrationActivity extends Activity {

    private static final int FIELD_ADDED_SUCCESSFULLY = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_registration);

        Button btnAddField = findViewById(R.id.btnaddfield);
        btnAddField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecordRegistrationActivity.this, AddFieldActivity.class);
                startActivityForResult(intent, 0);
            }
        });
    }//onCreate

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // check that it is the SecondActivity with an OK result
        Log.d("type!!!!!!!!!!!!!! " , "response");
        if (requestCode == FIELD_ADDED_SUCCESSFULLY) {
            Log.d("type!!!!!!!!!!!!!! " , "success");
            if (resultCode == RESULT_OK) { // Activity.RESULT_OK

                // get String data from Intent


                TextView textFiled = new TextView(RecordRegistrationActivity.this);
                textFiled.setText(data.getStringExtra("title"));

                EditText textInput = new EditText(RecordRegistrationActivity.this);
                textInput.setInputType(InputType.TYPE_CLASS_TEXT);

                LinearLayout fieldSet = (LinearLayout) findViewById(R.id.record_layout);
                fieldSet.addView(textFiled, fieldSet.getChildCount()-2);
                fieldSet.addView(textInput, fieldSet.getChildCount()-2);
                // set text view with string
                /*TextView textView = (TextView) findViewById(R.id.textView);
                textView.setText(returnString);*/
            }
        }
    }
}
