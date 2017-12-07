package com.juanjo.udl.geotracker.Utilities;

import android.content.Context;

import com.juanjo.udl.geotracker.JSONObjects.JSONProject;
import com.juanjo.udl.geotracker.JSONObjects.JSONRecord;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
        } // getLocalSavedJsonRecordsç

        public static List<JSONProject> getLocalSavedJsonProjects(Context context) throws IOException, JSONException {
            ArrayList<JSONProject> records = new ArrayList<>();

            File dir = new File(context.getFilesDir().getCanonicalPath() + Constants.StaticFields.getFolderOfProjects());
            File[] files = dir.listFiles();
            if(files != null){
                for(File f : files){
                    if(f.isFile()){
                        JSONProject j = new JSONProject(context, f);
                        records.add(j);
                    }
                }
            }
            return records;
        } // getLocalSavedJsonProjects

        public static Map<String, Object> jsonToMap(JSONObject json) throws JSONException {
            Map<String, Object> retMap = new HashMap<>();

            if(json != null) {
                retMap = toMap(json);
            }
            return retMap;
        }

        public static Map<String, Object> toMap(JSONObject object) throws JSONException {
            Map<String, Object> map = new HashMap<>();

            Iterator<String> keysItr = object.keys();
            while(keysItr.hasNext()) {
                String key = keysItr.next();
                Object value = object.get(key);

                if(value instanceof JSONObject) value = toMap((JSONObject) value);

                map.put(key, value);
            }
            return map;
        }
    }
}//Constants
