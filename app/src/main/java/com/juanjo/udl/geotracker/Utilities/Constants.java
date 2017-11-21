package com.juanjo.udl.geotracker.Utilities;

public class Constants {
    public static class StaticFields{
        private static final String folderOfRecords = "/records";
        private static final String folderOfProjects = "/projects";
        private static final String folderOfUsers = "/users";

        public static String getFolderOfRecords() { return folderOfRecords; }//getFolderOfRecords
        public static String getFolderOfProjects() { return folderOfProjects; }//getFolderOfProjects
        public static String getFolderOfUsers() { return folderOfUsers; }//getFolderOfUsers

    }//StaticFields

    public enum FieldTypes {
        Text,
        Numeric,

    }//FieldTypes
}//Constants
