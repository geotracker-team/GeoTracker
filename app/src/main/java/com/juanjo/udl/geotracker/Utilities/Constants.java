package com.juanjo.udl.geotracker.Utilities;

import android.content.Context;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.juanjo.udl.geotracker.JSONObjects.JSONProject;
import com.juanjo.udl.geotracker.JSONObjects.JSONRecord;

import org.json.JSONArray;
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

        public static List<JSONRecord> getLocalSavedJsonRecords(Context context, int idProject) throws IOException, JSONException {
            ArrayList<JSONRecord> records = new ArrayList<>();

            File dir = new File(context.getFilesDir().getCanonicalPath() + Constants.StaticFields.getFolderOfRecords() + "/" + idProject + "/");
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

        public static Map<String, Object> APIExtraToAPPExtra(JsonArray otherFields) throws JSONException {
            HashMap<String, Object> obj = new HashMap<>();
            for (JsonElement e: otherFields ) {
                HashMap<String, String> tmp = new HashMap<>();
                JSONObject jObj = new JSONObject(e.toString());
                String value = jObj.getString("value");
                String title = jObj.getString("title");
                String type = jObj.getString("type");

                tmp.put(type, value);
                obj.put(title, tmp);
            }
            return obj;
        }//APIExtraToAPPExtra

        public static JSONArray APPExtraToAPIExtra(Map<String, Object> otherFields) throws JSONException {
            JSONArray obj = new JSONArray();
            for (String title : otherFields.keySet()){
                HashMap<String, String> tmp = (HashMap<String, String>) otherFields.get(title);
                String type = (String) tmp.keySet().toArray()[0];
                String value = tmp.get(type);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("value", value);
                jsonObject.put("type", type);
                jsonObject.put("title", title);
                obj.put(jsonObject);
            }
            return obj;
        }
    }
}//Constants
