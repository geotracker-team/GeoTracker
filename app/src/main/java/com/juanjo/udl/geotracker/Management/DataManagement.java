package com.juanjo.udl.geotracker.Management;

import com.juanjo.udl.geotracker.Activities.GlobalActivity.GlobalAppCompatActivity;
import com.juanjo.udl.geotracker.JSONObjects.JSONProject;
import com.juanjo.udl.geotracker.JSONObjects.JSONRecord;

import java.util.ArrayList;

public class DataManagement {
    private GlobalAppCompatActivity context;

    public DataManagement(GlobalAppCompatActivity context){
        this.context = context;
    }//constructor

    public boolean login (String user, String pass){
        boolean ret = true;
        return ret;
    }//login

    public ArrayList<JSONProject> getProjectsOfUser(String user, String pass) {
        ArrayList<JSONProject> ret = new ArrayList<>();
        return ret;
    }//getProjects

    public ArrayList<JSONRecord> getRecordsOfProject(String user, String pass, int idProject){
        ArrayList<JSONRecord> ret = new ArrayList<>();
        return ret;
    }//getRecordsOfProject

    public boolean addRecord(String user, String pass, JSONRecord record){
        boolean ret = true;
        return context.isConnectionAllowed() && ret;
    }//addRecord

    public boolean editRecord(String user, String pass, int idRecrod, JSONRecord record){
        boolean ret = true;
        return context.isConnectionAllowed() && ret;
    }//addRecord

}
