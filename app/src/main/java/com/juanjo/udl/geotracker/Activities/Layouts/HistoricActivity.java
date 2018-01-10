package com.juanjo.udl.geotracker.Activities.Layouts;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.juanjo.udl.geotracker.Activities.GlobalActivity.GlobalAppCompatActivity;
import com.juanjo.udl.geotracker.Adapters.JSONRecordAdapter;
import com.juanjo.udl.geotracker.JSONObjects.JSONRecord;
import com.juanjo.udl.geotracker.R;
import com.juanjo.udl.geotracker.Utilities.Constants;
import com.juanjo.udl.geotracker.Utilities.SampleData;

import org.json.JSONException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;

public class HistoricActivity extends GlobalAppCompatActivity {

    int idProject;
    Spinner fUser;
    TextView fDateIni, fDateFin;
    Button btSearch;
    ListView listView;

    ArrayList<JSONRecord> records;
    ArrayList<JSONRecord> filteredRecords;
    LinkedList<String> users;
    JSONRecordAdapter itemsAdapter;

    SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.StaticFields.getDataFormat());
    Calendar myCalendarIni = Calendar.getInstance();
    Calendar myCalendarFin = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener dateIniDialog;
    TimePickerDialog.OnTimeSetListener timeIniDialog;
    DatePickerDialog.OnDateSetListener dateFinDialog;
    TimePickerDialog.OnTimeSetListener timeFinDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_historic);

        SampleData sample = new SampleData();
        sample.createProjects(this);

        Intent it = getIntent();
        if(it != null){
            idProject = it.getIntExtra("idProject", 0);
        }

        fUser = findViewById(R.id.fUser);
        fDateIni = findViewById(R.id.fDateIni);
        fDateFin = findViewById(R.id.fDateFin);
        btSearch = findViewById(R.id.btSearch);
        listView = findViewById(R.id.list);

        dateIniDialog = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                myCalendarIni.set(Calendar.YEAR, year);
                myCalendarIni.set(Calendar.MONTH, monthOfYear);
                myCalendarIni.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                new TimePickerDialog(HistoricActivity.this, timeIniDialog, myCalendarIni
                        .get(Calendar.HOUR_OF_DAY), myCalendarIni.get(Calendar.MINUTE), true).show();
            }
        };
        timeIniDialog = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                myCalendarIni.set(Calendar.HOUR_OF_DAY, hourOfDay);
                myCalendarIni.set(Calendar.MINUTE, minute);
                fDateIni.setText(dateFormat.format(myCalendarIni.getTime()));
            }
        };
        dateFinDialog = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                myCalendarFin.set(Calendar.YEAR, year);
                myCalendarFin.set(Calendar.MONTH, monthOfYear);
                myCalendarFin.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                new TimePickerDialog(HistoricActivity.this, timeFinDialog, myCalendarFin
                        .get(Calendar.HOUR_OF_DAY), myCalendarIni.get(Calendar.MINUTE), true).show();
            }
        };
        timeFinDialog = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                myCalendarFin.set(Calendar.HOUR_OF_DAY, hourOfDay);
                myCalendarFin.set(Calendar.MINUTE, minute);
                fDateFin.setText(dateFormat.format(myCalendarFin.getTime()));
            }
        };

        fDateIni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(HistoricActivity.this, dateIniDialog, myCalendarIni
                        .get(Calendar.YEAR), myCalendarIni.get(Calendar.MONTH),
                        myCalendarIni.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        fDateFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(HistoricActivity.this, dateFinDialog, myCalendarFin
                        .get(Calendar.YEAR), myCalendarFin.get(Calendar.MONTH),
                        myCalendarFin.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        defaultSearchValues();

        try {
            showDialog();

            //Records
            records = readRecords();
            filteredRecords = readRecords();
            if(filteredRecords.size() == 0) {
                showToast(getString(R.string.txtNoRecords), Toast.LENGTH_SHORT);
                finish();
            }//Must have some records

            itemsAdapter = new JSONRecordAdapter(this, filteredRecords);
            listView.setAdapter(itemsAdapter);



          // Users
            users = readUsers();
            users.addFirst(getResources().getString(R.string.txtAll));
            ArrayAdapter<String> userAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, users);
            fUser.setAdapter(userAdapter);
            dismissDialog();
        } catch (Exception e){
            processException(e);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JSONRecord recordClicked = (JSONRecord) parent.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(), RecordViewActivity.class);
                intent.putExtra("JSONRecord", recordClicked);
                startActivity(intent);
            }
        });

        btSearch.setOnClickListener(new View.OnClickListener()
        {
             @Override
             public void onClick(View v) {
                 try {
                     String userName = (String)fUser.getSelectedItem();
                     filteredRecords.clear();
                     for(JSONRecord r : records) {
                         if ((r.getUserName().equals(userName) || userName.equals(getString(R.string.txtAll)))){
                             Date initData = dateFormat.parse(fDateIni.getText().toString());
                             Date endData = dateFormat.parse(fDateFin.getText().toString());
                             Date dateRecord = dateFormat.parse(r.getDate());
                             if(initData.before(dateRecord) && endData.after(dateRecord)) filteredRecords.add(r);
                         }
                     }
                     itemsAdapter.notifyDataSetChanged();
                 } catch (Exception e) {
                     processException(e);
                 }
             }
        });
    }

    private ArrayList<JSONRecord> readRecords() throws IOException, JSONException {
        ArrayList<JSONRecord> tmpRecords = (ArrayList<JSONRecord>) Constants.AuxiliarFunctions.getLocalSavedJsonRecords(this, idProject);
        return tmpRecords;
    }

    private LinkedList<String> readUsers() throws IOException, JSONException {
        HashSet<String> userSet = new HashSet<>();
        for(JSONRecord r : records){
            userSet.add(r.getUserName());
        }
        LinkedList<String> userNames = new LinkedList<>(userSet);
        return userNames;
    }

    private void defaultSearchValues() {
        Date dateFin = null;

        dateFin = new Date();
        fDateFin.setText(dateFormat.format(dateFin));

        Calendar dateIni = Calendar.getInstance();
        dateIni.setTime(dateFin);
        dateIni.add(Calendar.MONTH, -3);
        fDateIni.setText(dateFormat.format(dateIni.getTime()));
    }
}
