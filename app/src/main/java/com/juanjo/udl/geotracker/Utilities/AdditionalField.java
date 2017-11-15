package com.juanjo.udl.geotracker.Utilities;
import android.widget.EditText;

import com.juanjo.udl.geotracker.Utilities.Constants.FieldTypes;

public class AdditionalField {

    private String name;
    private FieldTypes type;
    private EditText content;

    public  AdditionalField(String name, FieldTypes type, EditText content){
        this.name = name;
        this.type = type;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FieldTypes getType() {
        return type;
    }

    public void setType(FieldTypes type) {
        this.type = type;
    }

    public EditText getContent() {
        return content;
    }

    public void setContent(EditText content) {
        this.content = content;
    }
}
