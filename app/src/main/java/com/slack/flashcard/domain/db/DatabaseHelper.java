package com.slack.flashcard.domain.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {


    //// Database Information - start

    // Database Name
    public static final String DB_NAME = "FLASHCARDS.DB";
    // Database Version
    public static final int DB_VERSION = 1;

    //// CardSet Table
    // Table Name
    public static final String CARDSET_TABLE_NAME = "CARDSETS";
    // Table Columns
    public static final String _ID = "_id";
    public static final String TITLE = "title";

    //// Card Table
    // Table Name
    public static final String CARD_TABLE_NAME = "CARDS";
    // Table Columns
    public static final String CARDSET_ID = "cardset_id";
    public static final String QUESTION = "question";
    public static final String ANSWER = "answer";

    //// Database Information -end


    //// Table creation query strings - start

    // Create CardSet table query string
    private static final String CREATE_CARDSET_TABLE = "create table " + CARDSET_TABLE_NAME + "("
            + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + TITLE + " TEXT NOT NULL)";

    // Create Card table query string
    private static final String CREATE_CARD_TABLE = "create table " + CARD_TABLE_NAME + "("
            + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + CARDSET_ID + " INTEGER, "
            + QUESTION + " TEXT NOT NULL, "
            + ANSWER + " TEXT NOT NULL, "
            + " FOREIGN KEY(" + CARDSET_ID + ") REFERENCES cardsets(" + _ID + ") ON DELETE CASCADE)";

    //// Table creation query strings - end


    // Private static DatabaseHelper instance
    private static DatabaseHelper instance;

    // Private Constructor
    private DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // Get databaseHelper instance
    public static DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_CARDSET_TABLE);
        sqLiteDatabase.execSQL(CREATE_CARD_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CARDSET_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CARD_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL("PRAGMA foreign_keys = ON;");
    }
}
