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
    private final String description, date;
    private final String userName, projectName;
    private final Double latitude, longitude;
    private final int userId, projectId;
    private JSONObjectImplSerializable otherFields;

    public JSONRecord(Context context, String description, String date, int userId, String userName, int projectId, String projectName, Double latitude, Double longitude) throws JSONException {
        otherFields = new JSONObjectImplSerializable();
        this.context = context;
        this.description = description;
        this.date = date;
        this.userId = userId;
        this.userName = userName;
        this.projectId = projectId;
        this.projectName = projectName;
        this.latitude = latitude;
        this.longitude = longitude;

        putValues();
    }//Constructor with generic fields

    //  Provisional to compatibility with previous version
    public JSONRecord(Context context, String description, String date, String userName, Double latitude, Double longitude) throws JSONException {
        otherFields = new JSONObjectImplSerializable();
        this.context = context;
        this.description = description;
        this.date = date;
        this.userId = 0;
        this.userName = userName;
        this.projectId = 0;
        this.projectName = "";
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
        this.userId = jsonObject.has("userid") ? jsonObject.getInt("userid") : 0;
        this.userName = jsonObject.has("username") ? jsonObject.getString("username") : "";
        this.projectId = jsonObject.has("projectid") ? jsonObject.getInt("projectid") : 0;
        this.projectName = jsonObject.has("projectname") ? jsonObject.getString("projectname") : "";
        this.latitude = jsonObject.getDouble("latitude");
        this.longitude = jsonObject.getDouble("longitude");
        this.otherFields = new JSONObjectImplSerializable(jsonObject.getJSONObject("otherFields"));

        putValues();//Save the values in the inner JSON form
    }//Constructor with file

    private void putValues() throws JSONException {
        put("description", description);
        put("date", date);
        put("userId", userId);
        put("username", userName);
        put("projectId", projectId);
        put("projectName", projectName);
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
        return this.userName + "_" + this.date.replace(" ", "_").replace("/", "-") + ".json";
    }//getFileName

    //GETERS
    public String getDescription() {
        return description;
    }
    public String getDate() {
        return date;
    }
    public int getUserId() {
        return userId;
    }
    public String getUserName() {
        return userName;
    }
    public int getProjectId() {
        return projectId;
    }
    public String getProjectName() {
        return projectName;
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
