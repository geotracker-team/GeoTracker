package com.juanjo.udl.geotracker.JSONObjects;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class JSONObjectImplSerializable extends JSONObject implements Serializable{
    public JSONObjectImplSerializable(){
        super();
    }

    public JSONObjectImplSerializable(JSONObject jsonObject) throws JSONException {
        super(jsonObject.toString());
    }
}
