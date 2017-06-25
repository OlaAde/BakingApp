package com.example.adeogo.bakingapp.ui;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;

import com.example.adeogo.bakingapp.R;
import com.example.adeogo.bakingapp.data.BakingContract;
import com.example.adeogo.bakingapp.data.BakingDbHelper;
import com.example.adeogo.bakingapp.models.Recipe;

/**
 * Implementation of App Widget functionality.
 */
public class BakingWidget extends AppWidgetProvider {

    private static String selection = BakingContract.BakingEntry.COLUMN_FAVORITE + "=?";
    private static String[] selectionArgs = new String[]{"1"};


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
         Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,0);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_widget);
        Cursor mRecipeCursor =  context.getContentResolver().query(BakingContract.BakingEntry
                .CONTENT_URI,null,null,null,null);
        mRecipeCursor.moveToFirst();

        int recipeNameIndex = mRecipeCursor.getColumnIndex(BakingContract.BakingEntry.COLUMN_RECIPE_NAME);
        int numServingsIndex = mRecipeCursor.getColumnIndex(BakingContract.BakingEntry.COLUMN_NO_SERVINGS);

        String recipeName = mRecipeCursor.getString(recipeNameIndex);
        int numServings = mRecipeCursor.getInt(numServingsIndex);

        String widgetText = "Let's make " + numServings + " servings of " + recipeName;
        views.setTextViewText(R.id.widget_tv, widgetText);
        views.setOnClickPendingIntent(R.id.image_widgt, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

