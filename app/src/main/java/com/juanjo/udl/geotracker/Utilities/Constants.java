package com.juanjo.udl.geotracker.Utilities;

import android.content.Context;

import com.juanjo.udl.geotracker.JSONObjects.JSONRecord;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Constants {
    public static class StaticFields{
        private static final String folderOfRecords = "/records/";
        private static final String folderOfProjects = "/projects/";
        private static final String folderOfUsers = "/users/";

        public static String getFolderOfRecords() { return folderOfRecords; }//getFolderOfRecords
        public static String getFolderOfProjects() { return folderOfProjects; }//getFolderOfProjects
        public static String getFolderOfUsers() { return folderOfUsers; }//getFolderOfUsers

    }//StaticFields

    public enum FieldTypes {
        TEXT,
        NUMERIC,
        TEMPERATURE,
        HUMIDITY,
        PRESSURE
    }//FieldTypes

    public static class AuxiliarFunctions{

        public static List<JSONRecord> getLocalSavedJsonRecords(Context context) throws IOException, JSONException {
            ArrayList<JSONRecord> records = new ArrayList<>();

            File dir = new File(context.getFilesDir().getCanonicalPath() + Constants.StaticFields.getFolderOfRecords());
            File[] files = dir.listFiles();
            if(files != null){
                for(File f : files){
                    if(f.isFile()){
                        JSONRecord j = new JSONRecord(context, f);
                        records.add(j);
                    }
                }
            }
            return records;
        } // getLocalSavedJsonRecords
    }
}//Constants
