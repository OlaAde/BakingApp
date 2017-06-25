package com.example.adeogo.bakingapp.ui;

import android.content.ContentValues;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adeogo.bakingapp.R;
import com.example.adeogo.bakingapp.data.BakingContract;

import java.util.List;

public class DetailRecipe extends AppCompatActivity  {

    List<String> mShrtStepDescList;
    List<String> mStepDescriptionList;
    List<String> mThumbnailStepUrlList;
    List<String> mVideoStepUrlList;
    List<String> mIngredientsList;
    List<String> mMeasureIngredientsList;
    List<Integer> mQuantyIngredientsList;
    private Boolean mTwoPane = false;
    private String mRecipeName;
    private int condition_favorite;
    private ContentValues movieValues = new ContentValues();
    private String selection = BakingContract.BakingEntry.COLUMN_RECIPE_NAME + "=?";
    private static String[] selectionArgs;

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
        mRecipeName = intent.getStringExtra("recipe_name");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(mRecipeName);

        selectionArgs = new String[]{mRecipeName};

        if(findViewById(R.id.container_exo) !=null){
            mTwoPane = true;
            ExoFragment exoFragment = new ExoFragment();
            exoFragment.sendToExoFrag(mVideoStepUrlList.get(0),mStepDescriptionList.get(0));

            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .add(R.id.container_exo,exoFragment)
                    .commit();

        }

        StepFragment stepFragment = new StepFragment();
        stepFragment.setData(mTwoPane,mRecipeName, mShrtStepDescList,mVideoStepUrlList,mIngredientsList,mMeasureIngredientsList,mQuantyIngredientsList,mStepDescriptionList);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.id_container_rv,stepFragment)
                .commit();

        TextView ingredientTextView = (TextView) findViewById(R.id.ingredient_list_tv);
        String ingredientList;
        StringBuilder stringBuilder = new StringBuilder();

        for(int i = 0 ; i < mIngredientsList.size(); i++){
            stringBuilder.append("\n" + mQuantyIngredientsList.get(i)+ " " + mMeasureIngredientsList.get(i)+ " of " + mIngredientsList.get(i));
        }
        ingredientList = stringBuilder.toString();
        ingredientTextView.setText(ingredientList);
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.recipe, menu);
        if(condition_favorite == 1){
            menu.findItem(R.id.action_favorites).setIcon(R.mipmap.favorites_dark);
        }
        else
            menu.findItem(R.id.action_favorites).setIcon(R.mipmap.favorites_light);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int idSelected = item.getItemId();
        switch (idSelected){
            case R.id.action_favorites:
                if(condition_favorite == 1){
                    // unfavorited as it is checked already
                    condition_favorite = 0;

                    movieValues.put(BakingContract.BakingEntry.COLUMN_FAVORITE,condition_favorite);
                    int updatedMovie =  getContentResolver().update(BakingContract.BakingEntry
                            .CONTENT_URI,movieValues,selection,selectionArgs);
                    Log.v("Movie Updated" ,""+ updatedMovie );
                    item.setIcon(R.mipmap.favorites_light);
                    Toast.makeText(this,getText(R.string.removing_from_favorites),Toast.LENGTH_SHORT).show();
                }

                else{
                    //Going to be made favorites as it is not checked
                    condition_favorite = 1;
                    movieValues.put(BakingContract.BakingEntry.COLUMN_FAVORITE,condition_favorite);
                    int updatedMovie =  getContentResolver().update(BakingContract.BakingEntry
                            .CONTENT_URI,movieValues,selection,selectionArgs);
                    Log.v("Movie Updated" ,""+ updatedMovie );
                    item.setIcon(R.mipmap.favorites_dark);

                    Toast.makeText(this, getText(R.string.adding_to_favorites), Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
