package com.example.adeogo.bakingapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Adeogo on 6/23/2017.
 */

public class BakingDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "recipeDb.db";
    private static final int VERSION = 1;

    public BakingDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE = "CREATE TABLE "  + BakingContract.BakingEntry.TABLE_NAME + " (" +
                BakingContract.BakingEntry._ID                + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                BakingContract.BakingEntry. COLUMN_RECIPE_NAME + " TEXT NOT NULL, " +
                BakingContract.BakingEntry. COLUMN_IMAGE + " TEXT, " +
                BakingContract.BakingEntry.COLUMN_NO_SERVINGS + " INTEGER NOT NULL);";
        Log.v("Create_State,ent", CREATE_TABLE);
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + BakingContract.BakingEntry.TABLE_NAME);
        onCreate(db);
    }
}
