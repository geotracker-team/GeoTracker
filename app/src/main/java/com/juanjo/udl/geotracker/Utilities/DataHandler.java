package com.juanjo.udl.geotracker.Utilities;

import android.os.Handler;
import android.os.Message;

import com.juanjo.udl.geotracker.Activities.GlobalActivity.GlobalAppCompatActivity;

public abstract class DataHandler extends Handler {
    private GlobalAppCompatActivity context;

    public DataHandler(GlobalAppCompatActivity context){
        super();
        this.context = context;
    }

    @Override
    public void handleMessage(Message msg){
        if(msg.what == 0) {
            try {
                isOk(msg.obj);
            } catch (Exception e) {
                context.processException(e);
            }
        }
        else {
            context.processException(new Exception((String)msg.obj));
        }//check
        context.dismissDialog();
    }

    protected abstract void isOk(Object obj) throws Exception;
}
