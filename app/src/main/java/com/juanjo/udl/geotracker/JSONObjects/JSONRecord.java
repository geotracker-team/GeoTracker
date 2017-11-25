package com.juanjo.udl.geotracker.JSONObjects;

import android.content.Context;

import com.juanjo.udl.geotracker.Utilities.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class JSONRecord extends JSONGlobal {
    private final String description, date, username;
    private final Double latitude, longitude;
    private JSONObjectImplSerializable otherFields;

    public JSONRecord(Context context, String description, String date, String username, Double latitude, Double longitude) throws JSONException {
        otherFields = new JSONObjectImplSerializable();
        this.context = context;
        this.description = description;
        this.date = date;
        this.username = username;
        this.latitude = latitude;
        this.longitude = longitude;

        putValues();
    }//Constructor with generic fields

    public JSONRecord(Context context, File file) throws IOException, JSONException {
        otherFields = new JSONObjectImplSerializable();
        BufferedReader br = new BufferedReader(new FileReader(file)); //Open JSON file
        String line;
        StringBuilder text = new StringBuilder();

        while ((line = br.readLine()) != null){
            text.append(line);
        }//Read the file

        br.close();

        JSONObject jsonObject = new JSONObject(text.toString());
        this.context = context;
        this.description = jsonObject.getString("description");
        this.date = jsonObject.getString("date");
        this.username = jsonObject.getString("username");
        this.latitude = jsonObject.getDouble("latitude");
        this.longitude = jsonObject.getDouble("longitude");
        this.otherFields = new JSONObjectImplSerializable(jsonObject.getJSONObject("otherFields"));

        putValues();//Save the values in the inner JSON form
    }//Constructor with file

    public void putValues() throws JSONException {
        put("description", description);
        put("date", date);
        put("username", username);
        put("latitude", latitude);
        put("longitude", longitude);

        if(this.has("otherFields")) this.remove("otherFields"); //if exists the map remove it
        put("otherFields", otherFields);
    }//putValues

    public void addNewField(String name, Constants.FieldTypes type, String content) throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put(type.toString(), content);
        otherFields.put(name, obj);//Save into the JSONObject the new field with it's content
        putValues();
    }//addNewField

    String getFileRoute() throws IOException {
        return context.getFilesDir().getCanonicalPath() + Constants.StaticFields.getFolderOfRecords();
    }//getFileRoute

    String getFileName() {
        return this.username + "_" + this.date.replace(" ", "_").replace("/", "-") + ".json";
    }//getFileName

    //GETERS
    public String getDescription() {
        return description;
    }
    public String getDate() {
        return date;
    }
    public String getUsername() {
        return username;
    }
    public Double getLatitude() {
        return latitude;
    }
    public Double getLongitude() {
        return longitude;
    }
    public JSONObject getField(String name) throws JSONException { return (JSONObject) otherFields.get(name); }
    public JSONObject getOtherFields() { return otherFields; }
}
