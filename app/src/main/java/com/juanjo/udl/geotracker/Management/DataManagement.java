package com.juanjo.udl.geotracker.Management;

import com.juanjo.udl.geotracker.JSONObjects.JSONProject;
import com.juanjo.udl.geotracker.JSONObjects.JSONRecord;

import java.util.ArrayList;

public class DataManagement {

    public boolean login (String user, String pass){
        return true;
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
        return true;
    }//addRecord

    public boolean editRecord(String user, String pass, int idRecrod, JSONRecord record){
        return true;
    }//addRecord

}
