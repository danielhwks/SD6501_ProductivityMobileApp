package com.example.assignment_1_study_app.database.timetable;

import android.provider.BaseColumns;

public class TimetableContract {

    private TimetableContract() {}

    public static class TimetableEntry implements BaseColumns {
        public static final String TABLE_NAME = "timetables";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_START_HOUR = "h_start";
        public static final String COLUMN_NAME_END_HOUR = "h_end";
        public static final String COLUMN_NAME_START_MIN = "m_start";
        public static final String COLUMN_NAME_END_MIN = "m_end";
        public static final String COLUMN_NAME_DAY = "day";
    }

    public static final String CREATE_TABLE_TIMETABLES =
            "CREATE TABLE " + TimetableEntry.TABLE_NAME + " (" +
                    TimetableEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    TimetableEntry.COLUMN_NAME_TITLE + " TEXT," +
                    TimetableEntry.COLUMN_NAME_START_HOUR + " INTEGER," +
                    TimetableEntry.COLUMN_NAME_END_HOUR + " INTEGER," +
                    TimetableEntry.COLUMN_NAME_START_MIN + " INTEGER," +
                    TimetableEntry.COLUMN_NAME_END_MIN + " INTEGER," +
                    TimetableEntry.COLUMN_NAME_DAY + " INTEGER)";

    public static final String DELETE_TABLE_TIMETABLES =
            "DROP TABLE IF EXISTS " + TimetableEntry.TABLE_NAME;
}
