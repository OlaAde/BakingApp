package com.example.adeogo.bakingapp.models;

/**
 * Created by Adeogo on 6/24/2017.
 */
import com.google.gson.annotations.SerializedName;
public class Recipe {
    @SerializedName("name")
    private String mRecipeName;
    @SerializedName("servings")
    private int mNumServings;
    @SerializedName("image")
    private String mImageLink;

    public Recipe(String RecipeName, int NumServings, String ImageLink){
        mRecipeName = RecipeName;
        mNumServings = NumServings;
        mImageLink = ImageLink;
    }

    public String getRecipeName(){
        return mRecipeName;
    }
    public int getNumServings(){
        return mNumServings;
    }

    public String getImageLink(){
        return mImageLink;
    }
}
