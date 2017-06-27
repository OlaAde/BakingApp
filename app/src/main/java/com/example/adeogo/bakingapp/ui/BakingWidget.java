package com.example.adeogo.bakingapp.ui;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.IntegerRes;
import android.widget.RemoteViews;

import com.example.adeogo.bakingapp.R;
import com.example.adeogo.bakingapp.data.BakingContract;
import com.example.adeogo.bakingapp.data.BakingDbHelper;
import com.example.adeogo.bakingapp.models.Recipe;
import com.example.adeogo.bakingapp.util.JsonFormat;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class BakingWidget extends AppWidgetProvider {

    private static String selection = BakingContract.BakingEntry.COLUMN_FAVORITE + "=?";
    private static String[] selectionArgs = new String[]{"1"};
    private static List<String> mIngredientList;
    private static List<String> mMeasureIngredientList;
    private static List<Integer> mQuantityIngredientList;


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
         Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,0);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_widget);
        Cursor mRecipeCursor =  context.getContentResolver().query(BakingContract.BakingEntry
                .CONTENT_URI,null,null,null,null);
        mRecipeCursor.moveToFirst();

        int responseJsonIndex = mRecipeCursor.getColumnIndex(BakingContract.BakingEntry.COLUMN_RESPONSE);
        int recipeNameIndex = mRecipeCursor.getColumnIndex(BakingContract.BakingEntry.COLUMN_RECIPE_NAME);
        int numServingsIndex = mRecipeCursor.getColumnIndex(BakingContract.BakingEntry.COLUMN_NO_SERVINGS);


        String responseJSon = mRecipeCursor.getString(responseJsonIndex);
        String recipeName = mRecipeCursor.getString(recipeNameIndex);
        int numServings = mRecipeCursor.getInt(numServingsIndex);

        try {
            JSONArray ingredArray = JsonFormat.getIngredientsArray(responseJSon,0);
            mIngredientList = JsonFormat.getIngredientList(ingredArray);
            mMeasureIngredientList = JsonFormat.getMeasureIngredientList(ingredArray);
            mQuantityIngredientList = JsonFormat.getQuantityIngredientList(ingredArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Ingredients:");
        for(int i = 0 ; i < mIngredientList.size(); i++){
            if(mQuantityIngredientList.get(i) != 1)
                stringBuilder.append("\n" + mQuantityIngredientList.get(i)+ " " + mMeasureIngredientList.get(i)+ "s of " + mIngredientList.get(i));
            else
                stringBuilder.append("\n" + mQuantityIngredientList.get(i)+ " " + mMeasureIngredientList.get(i)+ " of " + mIngredientList.get(i));
        }

        String widgetText = stringBuilder.toString();
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

