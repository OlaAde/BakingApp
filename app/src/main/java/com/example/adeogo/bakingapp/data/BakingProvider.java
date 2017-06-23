package com.example.adeogo.bakingapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import static com.example.adeogo.bakingapp.data.BakingContract.BakingEntry.TABLE_NAME;

/**
 * Created by Adeogo on 6/23/2017.
 */

public class BakingProvider extends ContentProvider {
    public static final int RECIPES = 100;
    public static final int RECIPE_WITH_ID = 101;
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private BakingDbHelper mBakingDbHelper;

    public static UriMatcher buildUriMatcher() {

        // Initialize a UriMatcher with no matches by passing in NO_MATCH to the constructor
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        /*
          All paths added to the UriMatcher have a corresponding int.
          For each kind of uri you may want to access, add the corresponding match with addURI.
          The two calls below add matches for the task directory and a single item by ID.
         */
        uriMatcher.addURI(BakingContract.AUTHORITY, BakingContract.PATH_RECIPES, RECIPES);
        uriMatcher.addURI(BakingContract.AUTHORITY, BakingContract.PATH_RECIPES + "/#", RECIPE_WITH_ID);

        return uriMatcher;
    }
    @Override
    public boolean onCreate() {
        Context context = getContext();
        mBakingDbHelper = new BakingDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db =  mBakingDbHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        Cursor retCursor;
        switch (match){
            case RECIPES:
                retCursor =  db.query(TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            // Default exception
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(),uri);


        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = mBakingDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case RECIPES:
                // Insert new values into the database
                // Inserting values into tasks table
                long id = db.insert(TABLE_NAME, null, values);
                Log.v("rsdfd", id+ "");
                if ( id > 0 ) {
                    returnUri = ContentUris.withAppendedId(BakingContract.BakingEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            // Set the value for the returnedUri and write the default case for unknown URI's
            // Default case throws an UnsupportedOperationException
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notify the resolver if the uri has been changed, and return the newly inserted URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Return constructed uri (this points to the newly inserted row of data)
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int numRowsDeleted;
        if (null == selection) selection = "1";
        switch (sUriMatcher.match(uri)) {

            case RECIPES:
                numRowsDeleted = mBakingDbHelper.getWritableDatabase().delete(
                        BakingContract.BakingEntry.TABLE_NAME,
                        selection,
                        selectionArgs);

                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (numRowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numRowsDeleted;

    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        int numOfRowsChanged;
        switch (match) {

            case RECIPES:
                numOfRowsChanged = mBakingDbHelper.getWritableDatabase().update(BakingContract.BakingEntry.TABLE_NAME,
                        values, selection, selectionArgs);
                break;


            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);

        }

        if(numOfRowsChanged !=0){
            getContext().getContentResolver().notifyChange(uri,null);
        }

        return numOfRowsChanged;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = mBakingDbHelper.getWritableDatabase();


        switch (sUriMatcher.match(uri)) {

            case RECIPES:
                db.beginTransaction();
                int rowsInserted = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(BakingContract.BakingEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                return rowsInserted;

            default:
                return super.bulkInsert(uri, values);
        }
    }
}
