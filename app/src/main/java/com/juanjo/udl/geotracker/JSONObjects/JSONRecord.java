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

    public JSONRecord(Context context, String description, String date, String username, Double latitude, Double longitude) {
        this.context = context;
        this.description = description;
        this.date = date;
        this.username = username;
        this.latitude = latitude;
        this.longitude = longitude;
    }//Constructor with generic fields

    public JSONRecord(Context context, File file) throws IOException, JSONException {
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

        putValues();//Save the values in the inner JSON form
    }//Constructor with file

    private void putValues() throws JSONException {
        put("description", description);
        put("date", date);
        put("username", username);
        put("latitude", latitude);
        put("longitude", longitude);
    }//putValues

    String getFileRoute() throws IOException {
        return context.getFilesDir().getCanonicalPath() + Constants.StaticFields.getFolderOfRecords();
    }//getFileRoute

    String getFileName() {
        return this.username + "_" + this.date.replace(" ", "_") + ".json";
    }//getFileName

    //GETTERS
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
}
