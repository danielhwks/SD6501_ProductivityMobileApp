package com.example.assignment_1_study_app.database.flashcards;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.assignment_1_study_app.database.flashcards.FlashCardsContract;

public class FlashCardsDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FlashCards.db";

    public FlashCardsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(FlashCardsContract.CREATE_TABLE_DECKS);
        db.execSQL(FlashCardsContract.CREATE_TABLE_CARDS);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(FlashCardsContract.DELETE_TABLE_CARDS);
        db.execSQL(FlashCardsContract.DELETE_TABLE_DECKS);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
