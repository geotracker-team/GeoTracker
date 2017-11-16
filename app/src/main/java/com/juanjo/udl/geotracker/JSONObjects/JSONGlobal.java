package com.juanjo.udl.geotracker.JSONObjects;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.juanjo.udl.geotracker.Utilities.AsyncTaskResult;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;

public abstract class JSONGlobal extends JSONObject implements Serializable{
    protected transient Context context; //transient context variable to prevent serialization
    protected String fileRoute;


    public void save(){
        new AsyncTask<JSONObject, Void, AsyncTaskResult<JSONObject>>() {
            @Override
            protected AsyncTaskResult<JSONObject> doInBackground(JSONObject... jsonObjects){
                try{
                    saveJsonFileInAsyncTask(jsonObjects[0]);//Save the JSON
                    return new AsyncTaskResult<JSONObject>(jsonObjects[0]);
                } catch (Exception ex){
                    return new AsyncTaskResult<JSONObject>(ex);
                }
            }

            @Override
            protected void onPostExecute(AsyncTaskResult<JSONObject> result){
                if (result.getError() != null){
                    Toast.makeText(context.getApplicationContext(), "Error, " + result.getError(), Toast.LENGTH_LONG).show();
                }//If has any problems process the exception
            }
        }.execute(this);
    }//save

    public void delete(){
        File file = new File(fileRoute);
        file.delete();
    }//delete

    private void saveJsonFileInAsyncTask(JSONObject jsonObject) throws IOException {
        String route = getFileRoute();
        String fileName = getFileName();
        String json = jsonObject.toString();
        File dir = new File(route);
        if(!dir.exists())dir.mkdirs(); // Make the directory if doesn't exsits
        File file = new File(dir, fileName);

        if(file.exists()) file.delete(); //If it exists delete it

        file.createNewFile();
        FileOutputStream output = new FileOutputStream(file);
        output.write(json.getBytes());
        output.close();

        fileRoute = getFileRoute()+getFileName();
    }//saveJsonFileInAsyncTask

    //Abstract functions to be implemented
    abstract String getFileRoute() throws IOException;

    abstract String getFileName();
}
