package com.stardust.crusaders.android;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "score_table.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "scores";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_SCORE = "score";

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db){
        String createTable = "CREATE TABLE " + TABLE_NAME + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_NAME + " TEXT, "
            + COLUMN_SCORE + " INTEGER)";
        db.execSQL(createTable);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersiom, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    public boolean insertScore(String name, int score){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_SCORE, score);
        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }
    public String[] getTopScores() {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] topScores = new String[5];

        Cursor cursor = db.query(TABLE_NAME,
            new String[] {COLUMN_NAME, COLUMN_SCORE},
            null,
            null,
            null,
            null,
            COLUMN_SCORE + " DESC", // Sort by score in descending order
            "5");  // Limit to top 5

        int i = 0;
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(0);
                int score = cursor.getInt(1);
                topScores[i] = name + " - " + score;
                i++;
            } while (cursor.moveToNext() && i < 5);
        }
        cursor.close();
        db.close();
        return topScores;
    }
    public void deleteAllScores() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
    }
}
