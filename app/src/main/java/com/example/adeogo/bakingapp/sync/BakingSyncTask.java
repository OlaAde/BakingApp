package com.example.adeogo.bakingapp.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.example.adeogo.bakingapp.data.BakingContract;
import com.example.adeogo.bakingapp.util.JsonFormat;
import com.example.adeogo.bakingapp.util.NetworkUtil;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

/**
 * Created by Adeogo on 6/23/2017.
 */


public class BakingSyncTask {
    static public String ACTION_FIRSTLOAD = "first_load";
    static private String urlString = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    synchronized public static void syncRecipes(Context context, String action){
        if(ACTION_FIRSTLOAD.equals(action)){
            URL url = NetworkUtil.buildUrl(urlString);
            try {
                String JSONResponse  = NetworkUtil.getResponseFromHttpUrl(url);
                ContentValues[] recipeList = JsonFormat.getBasisRecipe(JSONResponse);
                if(recipeList!=null && recipeList.length!=0){
                    ContentResolver movieContentResolver = context.getContentResolver();

                    movieContentResolver.delete(
                            BakingContract.BakingEntry.CONTENT_URI,null,null);

                    movieContentResolver.bulkInsert(
                            BakingContract.BakingEntry.CONTENT_URI,
                            recipeList);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
