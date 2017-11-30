package com.juanjo.udl.geotracker.Utilities;

import android.content.Context;

import com.juanjo.udl.geotracker.JSONObjects.JSONProject;
import com.juanjo.udl.geotracker.JSONObjects.JSONRecord;
import com.juanjo.udl.geotracker.JSONObjects.JSONUser;


import java.io.File;
import java.util.ArrayList;

/**
 * Created by David on 12/11/2017.
 */

public class SampleData {

    public boolean createProjects(Context context) {

        JSONProject project = null;
        try {
            File dir = new File(context.getFilesDir().getCanonicalPath() + Constants.StaticFields.getFolderOfProjects());
            File[] files = dir.listFiles();
            if ((files == null) || (files.length == 0)) {
                project = new JSONProject(context, 1, "Pest control");
                project.save();
                project = new JSONProject(context, 2, "Mushroom predictor");
                project.save();
                project = new JSONProject(context, 3, "Excursion tracker");
                project.save();
                project = new JSONProject(context, 4, "EPS project tracker");
                project.save();
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean createRecords(Context context) {

        JSONRecord record = null;
        try {
            File dir = new File(context.getFilesDir().getCanonicalPath() + Constants.StaticFields.getFolderOfRecords());
            File[] files = dir.listFiles();
            if ((files == null) || (files.length == 0)) {
                record = new JSONRecord(context, "Track point 1", "01-10-2017 10:15:00", 3, "David", 1, "Pest control", new Double(41.6109), new Double(0.6419));
                record.save();
                record = new JSONRecord(context, "Track point 2", "01-10-2017 11:23:00", 3, "David", 1, "Pest control", new Double(41.6121), new Double(0.6408));
                record.save();
                record = new JSONRecord(context, "Track point 3", "02-10-2017 11:23:00", 3, "David", 2, "", new Double(41.6111), new Double(0.6404));
                record.save();
                record = new JSONRecord(context, "Track point 4", "03-10-2017 11:23:00", 1, "Joan Josep", 1, "Pest control", new Double(41.6109), new Double(0.646));
                record.save();
                record = new JSONRecord(context, "Track point 5", "04-10-2017 11:23:00", 2, "Xavier", 3, "Excursion tracker", new Double(41.6107), new Double(0.6412));
                record.save();
                record = new JSONRecord(context, "Track point 6", "05-10-2017 11:23:00", 4, "Dejan", 2, "", new Double(41.6103), new Double(0.6414));
                record.save();
                record = new JSONRecord(context, "Track point 7", "07-10-2017 12:34:00", 1, "Joan Josep", 3, "Excursion tracker", new Double(41.6210), new Double(0.6510));
                record.save();
                record = new JSONRecord(context, "Track point 8", "12-10-2017 14:56:00", 2, "Xavier", 2, "Mushroom predictor", new Double(41.6123), new Double(0.6307));
                record.save();
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean createUsers(Context context) {

        JSONProject project = null;
        try {
            File dir = new File(context.getFilesDir().getCanonicalPath() + Constants.StaticFields.getFolderOfUsers());
            File[] files = dir.listFiles();
            if ((files == null) || (files.length == 0)) {
                project = new JSONProject(context, 1, "Joan Josep");
                project.save();
                project = new JSONProject(context, 2, "Xavier");
                project.save();
                project = new JSONProject(context, 3, "David");
                project.save();
                project = new JSONProject(context, 4, "Dejan");
                project.save();
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean deleteProjects(Context context)  {

        try {
            ArrayList<JSONProject> records = new ArrayList<JSONProject>();

            File dir = new File(context.getFilesDir().getCanonicalPath() + Constants.StaticFields.getFolderOfProjects());
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

    public boolean deleteRecords(Context context)  {

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

    public boolean deleteUsers(Context context)  {

        try {

            ArrayList<JSONUser> records = new ArrayList<JSONUser>();
            File dir = new File(context.getFilesDir().getCanonicalPath() + Constants.StaticFields.getFolderOfUsers());
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
