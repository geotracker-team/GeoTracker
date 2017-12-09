package com.juanjo.udl.geotracker.Activities.Layouts;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.juanjo.udl.geotracker.Activities.GlobalActivity.GlobalAppCompatActivity;
import com.juanjo.udl.geotracker.Adapters.JSONProjectCardAdapter;
import com.juanjo.udl.geotracker.JSONObjects.JSONProject;
import com.juanjo.udl.geotracker.R;
import com.juanjo.udl.geotracker.Utilities.Constants;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public class ProjectSelectActivity extends GlobalAppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<JSONProject> mDataset = new ArrayList<>();

    private Handler loadProjectsHandler;
    private boolean first = true;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_select);

        if(savedInstanceState != null){
            first = savedInstanceState.getBoolean("first");
        }

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        loadProjectsHandler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                processData();
            }
        };

        try {
            showDialog();
            generateFakeProjects();
            loadData();
            if(first) loadProjectsHandler.sendEmptyMessageDelayed(0, 2000); //Handle the loaded projects 3sg after
            else processData();
        } catch (Exception e) {
            processException(e);
        }
    }//onCreate

    @Override
    public void onSaveInstanceState(Bundle saveInstanceState){
        saveInstanceState.putBoolean("first", first);
        super.onSaveInstanceState(saveInstanceState);
    }

    private void loadData() throws IOException, JSONException {
        mDataset = (ArrayList<JSONProject>) Constants.AuxiliarFunctions.getLocalSavedJsonProjects(this);
    }//loadData

    private void processData(){
        if(mDataset.size() == 0) {
            showToast(getText(R.string.txtNoProjects).toString(), Toast.LENGTH_SHORT);
        }
        mAdapter = new JSONProjectCardAdapter(mDataset);
        mRecyclerView.setAdapter(mAdapter);
        first = false;

        dismissDialog();
    }//processData

    private void generateFakeProjects() throws JSONException {
        new JSONProject(this, 0, "Project 1").save();
        new JSONProject(this, 1, "Project 2").save();
    }//generateFakeProjects
}
