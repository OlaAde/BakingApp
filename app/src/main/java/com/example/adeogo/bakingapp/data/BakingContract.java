package com.example.adeogo.bakingapp.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Adeogo on 6/23/2017.
 */

public class BakingContract {
    private BakingContract(){

    }

    public static final String AUTHORITY = "com.example.adeogo.bakingapp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_RECIPES = "recipe";

    public static final class BakingEntry implements BaseColumns{
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_RECIPES).build();
        public static final String TABLE_NAME = "recipes";
        public static final String COLUMN_RECIPE_NAME = "recipe_name";
        public static final String COLUMN_NO_SERVINGS = "num_servings";
        public static final String COLUMN_IMAGE = "menu_image";
    }
}
