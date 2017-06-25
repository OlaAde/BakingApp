package com.example.adeogo.bakingapp.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Adeogo on 6/24/2017.
 */

public class Ingredient {
    @SerializedName("ingredient")
    private String mIngredient;
    @SerializedName("quantity")
    private int mQuantity;
    @SerializedName("measure")
    private String mMeasure;

    public Ingredient(String Ingredient, int Quantity, String Measure){
        mIngredient = Ingredient;
        mQuantity = Quantity;
        mMeasure = Measure;
    }

    public String getIngredient(){
        return mIngredient;
    }
    public int getQuantity(){
        return mQuantity;
    }
    public String getMeasure(){
        return mMeasure;
    }
}
