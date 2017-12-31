package com.juanjo.udl.geotracker.JSONObjects;

import android.content.Context;

import com.google.gson.JsonObject;
import com.juanjo.udl.geotracker.Utilities.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JSONRecord extends JSONGlobal {
    private String description, date;
    private final String userName, projectName;
    private final Double latitude, longitude;
    private final int idProject;
    private boolean sync = true, edited = false;
    private HashMap<String, Object> otherFields;
    private int idRecord;

    public JSONRecord(Context context, String description, String date, String userName, int idProject, String projectName, Double latitude, Double longitude) throws JSONException {
        otherFields = new HashMap();
        this.context = context;
        this.description = description;
        this.date = date;
        this.userName = userName;
        this.idProject = idProject;
        this.projectName = projectName;
        this.latitude = latitude;
        this.longitude = longitude;

        putValues();
    }//Constructor with generic fields

    //  Provisional to compatibility with previous version
    public JSONRecord(Context context, String description, String date, String userName, Double latitude, Double longitude, JSONProject project) throws JSONException, IOException {
        otherFields = new HashMap<>();
        this.context = context;
        this.description = description;
        this.date = date;
        this.userName = userName;
        this.idProject = project.getId();
        this.projectName = project.getName();
        this.latitude = latitude;
        this.longitude = longitude;

        putValues();
    }//Constructor with generic fields

    public JSONRecord(Context context, File file) throws IOException, JSONException {
        otherFields = new HashMap<>();
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
        this.userName = jsonObject.has("userName") ? jsonObject.getString("userName") : "";
        this.idProject = jsonObject.has("idProject") ? jsonObject.getInt("idProject") : 0;
        this.projectName = jsonObject.has("projectName") ? jsonObject.getString("projectName") : "";
        this.latitude = jsonObject.getDouble("latitude");
        this.longitude = jsonObject.getDouble("longitude");
        this.idRecord = jsonObject.has("idRecord") ? jsonObject.getInt("idRecord") : -1;
        this.otherFields = (HashMap<String, Object>) Constants.AuxiliarFunctions.jsonToMap(jsonObject.getJSONObject("otherFields"));
        this.fileRoute = jsonObject.has("fileRoute") ? getFileRoute()+getFileName() : getFileRoute() + getFileName();
        this.sync = jsonObject.has("sync") && jsonObject.getBoolean("sync");
        this.edited = jsonObject.has("edited") && jsonObject.getBoolean("edited");

        putValues();//Save the values in the inner JSON form
    }//Constructor with file

    public JSONRecord(Context context, JsonObject gson) throws JSONException, IOException {
        this.context = context;
        JSONObject jsonObject = new JSONObject(gson.toString());
        this.description = jsonObject.getString("description");
        this.date = jsonObject.getString("date");
        this.userName = jsonObject.has("userName") ? jsonObject.getString("userName") : "";
        this.idProject = jsonObject.has("idProject") ? jsonObject.getInt("idProject") : 0;
        this.projectName = jsonObject.has("projectName") ? jsonObject.getString("projectName") : "";
        this.latitude = jsonObject.getDouble("latitude");
        this.longitude = jsonObject.getDouble("longitude");
        this.idRecord = jsonObject.has("idRecord") ? jsonObject.getInt("idRecord") : -1;
        this.otherFields = (HashMap<String, Object>) Constants.AuxiliarFunctions.APIExtraToAPPExtra(gson.getAsJsonArray("otherFields"));
        this.fileRoute = getFileRoute() + getFileName();

        putValues();
    }//Constructor for the records of the api

    public void putValues() throws JSONException {
        put("description", description);
        put("date", date);
        put("userName", userName);
        put("idProject", idProject);
        put("projectName", projectName);
        put("latitude", latitude);
        put("longitude", longitude);
        put("idRecord", idRecord);
        put("sync", sync);
        put("edited", edited);

        if(this.has("otherFields")) this.remove("otherFields"); //if exists the map remove it
        put("otherFields", new JSONObject(otherFields));
    }//putValues

    public void addNewField(String name, Constants.FieldTypes type, String content) throws JSONException {
        HashMap<String, String> obj = new HashMap();
        obj.put(type.toString(), content);
        otherFields.put(name, obj);//Save into the Map the new field with it's content
        putValues();
    }//addNewField

    String getFileRoute() throws IOException {
        return context.getFilesDir().getCanonicalPath() + Constants.StaticFields.getFolderOfRecords() + idProject + "/";
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
    public String getUserName() {
        return userName;
    }
    public int getIdProject() {
        return idProject;
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
    public int getIdRecord() { return idRecord; }
    public Object getField(String name) throws JSONException { return (otherFields.get(name)); }
    public Map<String, Object> getOtherFields() { return otherFields; }
    public boolean isSync() { return sync; }
    public boolean isEdited() { return edited; }

    //SETTERS
    public void setContext(Context context){
        this.context = context;
    }
    public void setDescription(String description) throws JSONException {
        this.description = description;
        putValues();
    }
    public void setIdRecord(int idRecord) throws JSONException {
        this.idRecord = idRecord; putValues();
    }
    public void setSync(boolean sync) throws JSONException {
        this.sync = sync;
        putValues();
    }
    public void setEdited(boolean edited) throws JSONException {
        this.edited = edited;
        putValues();
    }
    public void setField(String name, JSONObject values) throws JSONException {
        if(otherFields.containsKey(name)) otherFields.remove(name);
        otherFields.put(name, values);
        putValues();
    }

}
