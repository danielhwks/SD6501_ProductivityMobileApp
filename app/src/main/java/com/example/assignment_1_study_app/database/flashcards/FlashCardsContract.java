package com.example.assignment_1_study_app.database.flashcards;

import android.provider.BaseColumns;

public final class FlashCardsContract {

    private FlashCardsContract() {}

    public static class CardEntry implements BaseColumns {
        public static final String TABLE_NAME = "cards";
        public static final String COLUMN_NAME_FRONT = "front";
        public static final String COLUMN_NAME_BACK = "back";
        public static final String COLUMN_NAME_DECK = "deck";
    }

    public static class DeckEntry implements BaseColumns {
        public static final String TABLE_NAME = "decks";
        public static final String COLUMN_NAME_NAME = "name";
    }

    public static final String CREATE_TABLE_DECKS =
            "CREATE TABLE " + DeckEntry.TABLE_NAME + " (" +
                    DeckEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    DeckEntry.COLUMN_NAME_NAME + " TEXT)";

    public static final String CREATE_TABLE_CARDS =
            "CREATE TABLE " + CardEntry.TABLE_NAME + " (" +
                    CardEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    CardEntry.COLUMN_NAME_FRONT + " TEXT," +
                    CardEntry.COLUMN_NAME_BACK + " TEXT," +
                    CardEntry.COLUMN_NAME_DECK + " INTEGER," +
                    " FOREIGN KEY (" + CardEntry.COLUMN_NAME_DECK + ") REFERENCES " +
                    DeckEntry.TABLE_NAME + "(" + DeckEntry._ID + "))";

    public static final String DELETE_TABLE_DECKS =
            "DROP TABLE IF EXISTS " + DeckEntry.TABLE_NAME;

    public static final String DELETE_TABLE_CARDS =
            "DROP TABLE IF EXISTS " + CardEntry.TABLE_NAME;
}