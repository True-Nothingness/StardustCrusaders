package com.stardust.crusaders.android;

import android.content.Context;

import com.stardust.crusaders.DatabaseInterface;

public class AndroidDatabaseHelper implements DatabaseInterface {
    private DatabaseHelper dbhelper;

    public AndroidDatabaseHelper(Context context){
        dbhelper = new DatabaseHelper(context);
    }
    public boolean insertScore(String name, int score){
        return dbhelper.insertScore(name,score);
    }
    public String[] getTopScores() {
        return dbhelper.getTopScores();
    }
}
