package com.example.adeogo.bakingapp.ui;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.adeogo.bakingapp.R;

import java.util.List;

public class DetailRecipe extends AppCompatActivity  {

    List<String> mShrtStepDescList;
    List<String> mStepDescriptionList;
    List<String> mThumbnailStepUrlList;
    List<String> mVideoStepUrlList;
    List<String> mIngredientsList;
    List<String> mMeasureIngredientsList;
    List<Integer> mQuantyIngredientsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_recipe);
        Intent intent = getIntent();
        mShrtStepDescList = intent.getStringArrayListExtra("ShrtStepDescList");
        mStepDescriptionList = intent.getStringArrayListExtra("StepDescriptionList");
        mThumbnailStepUrlList = intent.getStringArrayListExtra("ThumbnailStepUrlList");
        mVideoStepUrlList = intent.getStringArrayListExtra("VideoStepUrlList");
        mIngredientsList = intent.getStringArrayListExtra("IngredientsList");
        mMeasureIngredientsList = intent.getStringArrayListExtra("MeasureIngredientsList");
        mQuantyIngredientsList = intent.getIntegerArrayListExtra("QuantyIngredientsList");



        StepFragment stepFragment = new StepFragment();
        stepFragment.setData(mShrtStepDescList,mVideoStepUrlList,mIngredientsList,mMeasureIngredientsList,mQuantyIngredientsList,mStepDescriptionList);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.id_container_rv,stepFragment)
                .commit();

        TextView ingredientTextView = (TextView) findViewById(R.id.ingredient_list_tv);
        ingredientTextView.setText(mIngredientsList.get(0));
    }
}
