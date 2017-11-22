package com.juanjo.udl.geotracker.JSONObjects;

import android.content.Context;

import com.juanjo.udl.geotracker.Utilities.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by David on 13/11/2017.
 */

public class JSONUser extends JSONGlobal {
    private final int id;
    private final String description;

    public JSONUser(Context context, int id, String description) throws JSONException {
        this.context = context;
        this.id = id;
        this.description = description;

        putValues();
    }

    public JSONUser(Context context, File file) throws IOException, JSONException {

        BufferedReader br = new BufferedReader(new FileReader(file)); //Open JSON file
        String line;
        StringBuilder text = new StringBuilder();

        while ((line = br.readLine()) != null){
            text.append(line);
        }//Read the file

        br.close();

        JSONObject jsonObject = new JSONObject(text.toString());
        this.context = context;
        this.id = jsonObject.getInt("id");
        this.description = jsonObject.getString("description");

        putValues();//Save the values in the inner JSON form
    }

    private void putValues() throws JSONException {
        put("id", id);
        put("description", description);
    }//putValues

    String getFileRoute() throws IOException {
        return context.getFilesDir().getCanonicalPath() + Constants.StaticFields.getFolderOfUsers();
    }//getFileRoute

    String getFileName() {
        return "users_" + this.id + ".json";
    }//getFileName

    //GETERS
    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }


}