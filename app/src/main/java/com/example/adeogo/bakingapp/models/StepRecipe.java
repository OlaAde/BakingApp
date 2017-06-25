package com.example.adeogo.bakingapp.models;

/**
 * Created by Adeogo on 6/24/2017.
 */

public class StepRecipe {

    private String mVideourl;
    private int mStepNum;
    private String mShortDescription;
    private String mDescription;
    private String mThumbnailUrl;

    public StepRecipe(String VideoUrl, int StepNum, String ShortDescription, String Description, String ThumbnailUrl){
        mVideourl = VideoUrl;
        mStepNum = StepNum;
        mShortDescription = ShortDescription;
        mDescription = Description;
        mThumbnailUrl = ThumbnailUrl;
    }

    public String getVideourl(){
        return mVideourl;
    }

    public int getStepNum(){
        return mStepNum;
    }

    public String getShortDescription(){
        return mShortDescription;
    }

    public String getDescription(){
        return mDescription;
    }

    public String getThumbnailUrl(){
        return mThumbnailUrl;
    }
}
