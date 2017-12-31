package com.juanjo.udl.geotracker.Activities.Layouts;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.juanjo.udl.geotracker.Activities.GlobalActivity.GlobalAppCompatActivity;
import com.juanjo.udl.geotracker.Adapters.JSONProjectCardAdapter;
import com.juanjo.udl.geotracker.JSONObjects.JSONProject;
import com.juanjo.udl.geotracker.JSONObjects.JSONUser;
import com.juanjo.udl.geotracker.R;
import com.juanjo.udl.geotracker.Utilities.Constants;
import com.juanjo.udl.geotracker.Utilities.DataHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class ProjectSelectActivity extends GlobalAppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<JSONProject> mDataset = new ArrayList<>();

    private Handler loadProjectsHandler;
    private boolean first = true;

    private JSONUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_select);

        if(savedInstanceState != null){
            first = savedInstanceState.getBoolean("first");
        }

        Intent it = getIntent();
        if(it != null)  user = (JSONUser) it.getSerializableExtra("user");
        else finish();

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new JSONProjectCardAdapter(mDataset, user);
        mRecyclerView.setAdapter(mAdapter);

        loadProjectsHandler = new DataHandler(this){
            @Override
            protected void isOk(Object obj) throws Exception {
                readServerData(obj);
                processData();
            }
        };
    }//onCreate

    @Override
    public void onResume(){
        super.onResume();
        try {
            showDialog();
            if(first) loadServerData();
            else processData();
        } catch (Exception e) {
            processException(e);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle saveInstanceState){
        saveInstanceState.putBoolean("first", first);
        super.onSaveInstanceState(saveInstanceState);
    }

    private void loadServerData() throws IOException, JSONException {
        if(isConnected()){
            try {
                dataManagement.getProjectsOfUserApi(user.getName(), user.getPass(), loadProjectsHandler);
            } catch (Exception e) {
                processException(e);
            }
        }//If there are connection, load from the server
        else processData(); //read offline
    }//loadData

    private void readServerData(Object obj) throws JSONException, IOException {
        if(obj instanceof JSONArray){
            Constants.AuxiliarFunctions.deleteLocalProjectFiles(this); //Delete old projects to prevent misreads
            JSONArray projects = (JSONArray) obj;
            for(int i = 0; i < projects.length(); i++){
                JSONObject tmp = (JSONObject) projects.get(i);
                JSONProject proj = new JSONProject(this, tmp);
                proj.save();
            }//Save the projects
        }
        else {
            processException(new Exception((String)obj));
        }//If there is not a JSONArray process it as an error
    }//readServerData

    private void processData() throws IOException, JSONException {
        mDataset.clear();
        mDataset.addAll(Constants.AuxiliarFunctions.getLocalSavedJsonProjects(this));
        if(mDataset.size() == 0) {
            showToast(getText(R.string.txtNoProjects).toString(), Toast.LENGTH_SHORT);
        }
        mAdapter.notifyDataSetChanged();
        first = false;

        dismissDialog();
    }//processData
}
