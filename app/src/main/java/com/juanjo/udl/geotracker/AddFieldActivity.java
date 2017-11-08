package com.juanjo.udl.geotracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AddFieldActivity extends Activity {

    private enum FieldType {
        TEXT,
        NUMERIC,
        TEMPERATURE,
        HUMIDITY,
        PRESSURE
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_field);




        Button btnCreateField = findViewById(R.id.btncreatefield);
        btnCreateField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Log.d("!!!!!!!!!!!!!!!","title:" + titleValue +" type: " + typeValue + " child: " + textFiled.getText().toString() + " enum " + FieldType.HUMIDITY);
                //fieldSet.removeAllViews();



                /*LinearLayout fieldSet = (LinearLayout) findViewById(R.id.add_field_id);
                fieldSet.addView(createTextView(), fieldSet.getChildCount()-1);
                fieldSet.addView(createInputField(), fieldSet.getChildCount()-1);*/
//                Intent intent = new Intent(AddFieldActivity.this, RecordRegistrationActivity.class);
//                startActivity(intent);


                EditText editText = (EditText) findViewById(R.id.fildetitle);
                String titleValue = editText.getText().toString();

                Spinner fieldType = (Spinner) findViewById(R.id.fieldtype);
                String typeValue = fieldType.getSelectedItem().toString();

                // put the String to pass back into an Intent and close this activity

                Log.d("################# " , "text");

                Intent intent = new Intent();
                intent.putExtra("title", titleValue);
                //intent.putExtra("type", typeValue);
                Log.d("################# " , "finish");
                setResult(RESULT_OK, intent);
                finish();


            }
        });
    }//onCreate

    private TextView createTextView(){
        EditText fieldTitle = (EditText) findViewById(R.id.fildetitle);
        String titleValue = fieldTitle.getText().toString();

        TextView textFiled = new TextView(AddFieldActivity.this);
        textFiled.setText(titleValue);
        return textFiled;
    }


    private EditText createInputField(){
        Spinner fieldType = (Spinner) findViewById(R.id.fieldtype);
        String typeValue = fieldType.getSelectedItem().toString();
        EditText textInput = new EditText(AddFieldActivity.this);
        textInput.setInputType(InputType.TYPE_CLASS_TEXT);
        //FieldType type = FieldType.valueOf(fieldType.getSelectedItem().toString().toUpperCase());

       /* switch (type){
            case TEXT:
                Log.d("type!!!!!!!!!!!!!! " , "text");
                break;

            case NUMERIC:
                Log.d("type!!!!!!!!!!!!!! " , "numeric");
                break;

            case TEMPERATURE:
                Log.d("type!!!!!!!!!!!!!! " , "temperature");
                break;

            case HUMIDITY:
                Log.d("type!!!!!!!!!!!!!! " , "humidity");
                break;

            case PRESSURE:
                Log.d("type!!!!!!!!!!!!!! " , "pressure");
                break;
        }*/
        return textInput;
    }


}
