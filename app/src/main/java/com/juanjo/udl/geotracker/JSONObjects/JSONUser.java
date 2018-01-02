package com.juanjo.udl.geotracker.JSONObjects;

import android.content.Context;

import com.juanjo.udl.geotracker.Utilities.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class JSONUser extends JSONGlobal {
    private final String name;
    private final String pass;

    public JSONUser(Context context, String name, String pass) throws JSONException {
        this.context = context;
        this.name = name;
        this.pass = pass;

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
        this.name = jsonObject.getString("name");
        this.pass = jsonObject.getString("pass");

        putValues();//Save the values in the inner JSON form
    }

    private void putValues() throws JSONException {
        put("name", name);
        put("pass", pass);
    }//putValues

    String getFileRoute() throws IOException {
        return context.getFilesDir().getCanonicalPath() + Constants.StaticFields.getFolderOfUsers();
    }//getFileRoute

    String getFileName() {
        return "users_" + this.name + ".json";
    }//getFileName

    public String getName() {
        return name;
    }
    public String getPass() {
        return pass;
    }

}
