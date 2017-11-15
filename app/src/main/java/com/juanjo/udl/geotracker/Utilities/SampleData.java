package com.juanjo.udl.geotracker.Utilities;

import android.content.Context;

import com.juanjo.udl.geotracker.JSONObjects.JSONRecord;
import com.juanjo.udl.geotracker.JSONObjects.JSONRecordAdapter;
import com.juanjo.udl.geotracker.Utilities.Constants;


import java.io.File;
import java.util.ArrayList;

/**
 * Created by David on 12/11/2017.
 */

public class SampleData {

    public boolean create(Context context) {

        JSONRecord record = null;
        try {
            File dir = new File(context.getFilesDir().getCanonicalPath() + Constants.StaticFields.getFolderOfRecords());
            File[] files = dir.listFiles();
            if ((files == null) || (files.length == 0)) {
                record = new JSONRecord(context, "Track point 1", "01-10-2017 10:15:00", "David", new Double(41.6109), new Double(0.6419));
                record.save();
                record = new JSONRecord(context, "Track point 2", "01-10-2017 11:23:00", "David", new Double(41.6121), new Double(0.6408));
                record.save();
                record = new JSONRecord(context, "Track point 3", "02-10-2017 11:23:00", "David", new Double(41.6111), new Double(0.6404));
                record.save();
                record = new JSONRecord(context, "Track point 4", "03-10-2017 11:23:00", "Joan Josep", new Double(41.6109), new Double(0.646));
                record.save();
                record = new JSONRecord(context, "Track point 5", "04-10-2017 11:23:00", "Xavier", new Double(41.6107), new Double(0.6412));
                record.save();
                record = new JSONRecord(context, "Track point 5", "05-10-2017 11:23:00", "Dejan", new Double(41.6103), new Double(0.6414));
                record.save();
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }



    public boolean delete(Context context)  {

        try {
            ArrayList<JSONRecord> records = new ArrayList<JSONRecord>();

            File dir = new File(context.getFilesDir().getCanonicalPath() + Constants.StaticFields.getFolderOfRecords());
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    file.delete();
                }
            }

            return true;

        } catch (Exception e) {
            return false;
        }
    }



}
