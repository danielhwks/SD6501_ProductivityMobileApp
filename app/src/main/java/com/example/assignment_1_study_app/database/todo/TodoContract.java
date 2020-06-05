package com.example.assignment_1_study_app.database.todo;

import android.provider.BaseColumns;

public final class TodoContract {

    private TodoContract() {}

    public static class TodoEntry implements BaseColumns {
        public static final String TABLE_NAME = "todos";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_TICKED = "ticked";
    }

    public static final String CREATE_TABLE_TODOS =
            "CREATE TABLE " + TodoEntry.TABLE_NAME + " (" +
                    TodoEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    TodoEntry.COLUMN_NAME_TITLE + " TEXT," +
                    TodoEntry.COLUMN_NAME_TICKED + " INTEGER)";

    public static final String DELETE_TABLE_TODOS =
            "DROP TABLE IF EXISTS " + TodoEntry.TABLE_NAME;
}
