package com.juanjo.udl.geotracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.juanjo.udl.geotracker.JSONObjects.JSONProject;
import com.juanjo.udl.geotracker.JSONObjects.JSONRecord;
import com.juanjo.udl.geotracker.JSONObjects.JSONRecordAdapter;
import com.juanjo.udl.geotracker.JSONObjects.JSONUser;
import com.juanjo.udl.geotracker.Utilities.Constants;
import com.juanjo.udl.geotracker.Utilities.SampleData;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

public class HistoricActivity extends AppCompatActivity {

    Spinner fUser;
    Spinner fProject;
    EditText fDateIni;
    EditText fDateFin;
    Button btSearch;

    ArrayList<JSONRecord> records;
    ArrayList<String> projects;
    ArrayList<String> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historic);

        SampleData sample = new SampleData();
//        sample.deleteProjects(this);
//        sample.deleteUsers(this);
//        sample.deleteRecords(this);
//        sample.createProjects(this);
//        sample.createUsers(this);
//        sample.createRecords(this);


        fUser = (Spinner) findViewById(R.id.fUser);
        fProject = (Spinner) findViewById(R.id.fProject);
        fDateIni = (EditText) findViewById(R.id.fDateIni);
        fDateFin = (EditText) findViewById(R.id.fDateFin);
        btSearch = (Button) findViewById(R.id.btSearch);


        users = readUsers();
        ArrayAdapter<String> userAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, users);
        fUser.setAdapter(userAdapter);

        projects = readProjects();
        ArrayAdapter<String> projectAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, projects);
        fProject.setAdapter(projectAdapter);

        records = readRecords();
        final JSONRecordAdapter itemsAdapter = new JSONRecordAdapter(this, records);
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(itemsAdapter);

        btSearch.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             Toast.makeText(getApplicationContext(), "Search button",Toast.LENGTH_SHORT).show();

/*
                                             records = readRecords();
                                             JSONRecordAdapter itemsAdapter = new JSONRecordAdapter(this, records);
                                             ListView listView = (ListView) findViewById(R.id.list);
                                             listView.setAdapter(itemsAdapter);
  */
                                         }
                                     }
        );


    }


    private ArrayList<JSONRecord> readRecords()  {
        JSONRecord record;
        ArrayList<JSONRecord> records = new ArrayList<JSONRecord>();
        try {

        File dir = new File(getFilesDir().getCanonicalPath() + Constants.StaticFields.getFolderOfRecords());
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    record = new JSONRecord(this, file);
                    if (validate(record)) {
                        records.add(record);
                    }
                }
            }
        }

        return records;

        } catch (Exception e) {
            return null;
        }
    }

    private ArrayList<String> readProjects()  {
        JSONProject project = null;
        ArrayList<String> projects = new ArrayList<String>();
        try {
            File dir = new File(getFilesDir().getCanonicalPath() + Constants.StaticFields.getFolderOfProjects());
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        project = new JSONProject(this, file);
                        projects.add(project.getDescription());
                    }
                }
            }

            return projects;

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(),Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    private ArrayList<String> readUsers()  {
        JSONUser user = null;
        ArrayList<String> users = new ArrayList<String>();
        try {
            File dir = new File(getFilesDir().getCanonicalPath() + Constants.StaticFields.getFolderOfUsers());
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        user = new JSONUser(this, file);
                        users.add(user.getDescription());
                    }
                }
            }

            return users;

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(),Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    private boolean validate(JSONRecord record) {
        boolean valid;

        valid = true;
    //    Toast.makeText(getApplicationContext(), getResources().getString(),Toast.LENGTH_SHORT).show();

        /*
        if (record.getUsername().equals(fUser.toString()))
            valid = true;
*/
        return valid;
    }

}
