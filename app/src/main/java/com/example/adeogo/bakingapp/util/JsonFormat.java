package com.example.adeogo.bakingapp.util;

import android.content.ContentValues;

import com.example.adeogo.bakingapp.data.BakingContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.example.adeogo.bakingapp.data.BakingContract.BakingEntry;
import com.example.adeogo.bakingapp.models.Recipe;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import static com.example.adeogo.bakingapp.data.BakingContract.BakingEntry;

/**
 * Created by Adeogo on 6/17/2017.
 */

public class JsonFormat {

    public static ContentValues[] getBasisRecipe(String JSONresponse) throws JSONException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        List<Recipe> recipes = Arrays.asList(gson.fromJson(JSONresponse, Recipe[].class));
        JSONArray jsonArray = new JSONArray(JSONresponse);
        int lengthResponse = jsonArray.length();
        ContentValues[] contentValues = new ContentValues[lengthResponse];
        for(int i = 0; i < lengthResponse;i++ ){
            ContentValues contentValues1 = new ContentValues();

            contentValues1.put(BakingEntry.COLUMN_RECIPE_NAME, recipes.get(i).getRecipeName());
            contentValues1.put(BakingEntry.COLUMN_IMAGE,recipes.get(i).getImageLink());
            contentValues1.put(BakingEntry.COLUMN_RESPONSE, JSONresponse);
            contentValues1.put(BakingEntry.COLUMN_FAVORITE, 0);
            contentValues1.put(BakingEntry.COLUMN_NO_SERVINGS,recipes.get(i).getNumServings());
            contentValues[i] = contentValues1;
        }
        return contentValues;
    }

    public static String getRecipeName(String JSonResponse, int id) throws JSONException {
        JSONArray jsonArray = new JSONArray(JSonResponse);
        JSONObject jsonObject = jsonArray.getJSONObject(id);
        String name = jsonObject.getString("name");
        return name;
    }
    public static JSONArray getIngredientsArray(String JSONResponse, int id) throws JSONException {
        JSONArray jsonArray = new JSONArray(JSONResponse);
        JSONObject jsonObject = jsonArray.getJSONObject(id);
        JSONArray ingredientsArray = jsonObject.getJSONArray("ingredients");
        return ingredientsArray;
    }

    public static int getQuantityIngredient(JSONArray ingredientArray , int idIngredient) throws JSONException {
        JSONObject jsonObject = ingredientArray.getJSONObject(idIngredient);
        int quantity = jsonObject.getInt("quantity");
        return quantity;
    }

    public static String getMeasureIngredient(JSONArray ingredientArray , int idIngredient) throws JSONException {
        JSONObject jsonObject = ingredientArray.getJSONObject(idIngredient);
        String measure = jsonObject.getString("measure");
        return measure;
    }

    public static String getIngredient(JSONArray ingredientArray , int idIngredient) throws JSONException {
        JSONObject jsonObject = ingredientArray.getJSONObject(idIngredient);
        String ingredient = jsonObject.getString("ingredient");
        return ingredient;
    }

    public static List<String> getIngredientList(JSONArray ingredientArray) throws JSONException {
        List<String> ingredientsList = new ArrayList<>();
        for(int i = 0; i<ingredientArray.length();i++){
            String ingredient = getIngredient(ingredientArray,i);
            if(ingredient == null)
                ingredientsList.add("none");
            else
                ingredientsList.add(ingredient);
        }
        return ingredientsList;
    }

    public static List<String> getMeasureIngredientList(JSONArray ingredientArray) throws JSONException {
        List<String> measureIngredientsList = new ArrayList<>();
        for(int i = 0; i<ingredientArray.length();i++){
            String measure = getMeasureIngredient(ingredientArray,i);
            if(measure == null)
                measureIngredientsList.add("none");
            else
                measureIngredientsList.add(measure);
        }
        return measureIngredientsList;
    }

    public static List<Integer> getQuantityIngredientList(JSONArray ingredientArray) throws JSONException {
        List<Integer> quantityIngredientsList = new ArrayList<>();
        for(int i = 0; i<ingredientArray.length();i++){
            int quantity = getQuantityIngredient(ingredientArray,i);
                quantityIngredientsList.add(quantity);
        }
        return quantityIngredientsList;
    }



    public static JSONArray getStepsArray(String JSONResponse, int id) throws JSONException {
        JSONArray jsonArray = new JSONArray(JSONResponse);
        JSONObject jsonObject = jsonArray.getJSONObject(id);
        JSONArray stepsArray = jsonObject.getJSONArray("steps");
        return stepsArray;
    }

    public static String getShortDescpStep(JSONArray stepsArray, int idStep) throws JSONException {
        JSONObject jsonObject = stepsArray.getJSONObject(idStep);
        String shortDescp = jsonObject.getString("shortDescription");
        return shortDescp;
    }

    public static String getDescriptionStep(JSONArray stepsArray, int idStep) throws JSONException {
        JSONObject jsonObject = stepsArray.getJSONObject(idStep);
        String description = jsonObject.getString("description");
        return description;
    }

    public static String getVideoUrl(JSONArray stepsArray, int idStep) throws JSONException {
        JSONObject jsonObject = stepsArray.getJSONObject(idStep);
        String videoUrl = jsonObject.getString("videoURL");
        return videoUrl;
    }

    public static String getThumbnailUrl(JSONArray stepsArray, int idStep) throws JSONException {
        JSONObject jsonObject = stepsArray.getJSONObject(idStep);
        String thumbnailUrl = jsonObject.getString("thumbnailURL");
        return thumbnailUrl;
    }

    public static List<String> getListStepShortDescns(JSONArray stepsArray) throws JSONException {
        List<String> stepDescrips = new ArrayList<>();
        for(int i = 0; i<stepsArray.length();i++){
            String stepDescrip = getShortDescpStep(stepsArray,i);
            if (stepDescrip == null)
                stepDescrips.add("none");
            else
                stepDescrips.add(stepDescrip);
        }
        return stepDescrips;
    }

    public static List<String> getListStepVideoUrls(JSONArray stepsArray) throws JSONException {
        List<String> stepVideoUrls = new ArrayList<>();
        for(int i = 0; i<stepsArray.length();i++){
            String videourl = getVideoUrl(stepsArray,i);
                stepVideoUrls.add(videourl);
        }
        return stepVideoUrls;
    }

    public static List<String> getListStepThumbnailUrls(JSONArray stepsArray) throws JSONException {
        List<String> stepThumbnailUrls = new ArrayList<>();
        for(int i = 0; i<stepsArray.length();i++){
            String thumbnailurl = getThumbnailUrl(stepsArray,i);
            if (thumbnailurl == null)
                stepThumbnailUrls.add("none");
            else
                stepThumbnailUrls.add(thumbnailurl);
        }
        return stepThumbnailUrls;
    }

    public static List<String> getListDescription(JSONArray stepsArray) throws JSONException {
        List<String> descriptions = new ArrayList<>();
        for(int i = 0; i<stepsArray.length();i++){
            String descrip = getDescriptionStep(stepsArray,i);
            descriptions.add(descrip);
        }
        return descriptions;
    }

}
