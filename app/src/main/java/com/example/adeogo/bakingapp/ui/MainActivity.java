package com.example.adeogo.bakingapp.ui;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adeogo.bakingapp.R;
import com.example.adeogo.bakingapp.adapter.MenuAdapter;
import com.example.adeogo.bakingapp.data.BakingContract;
import com.example.adeogo.bakingapp.sync.BakingSyncIntentService;
import com.example.adeogo.bakingapp.sync.BakingSyncTask;
import com.example.adeogo.bakingapp.sync.BakingSyncUtils;
import com.example.adeogo.bakingapp.util.JsonFormat;
import com.example.adeogo.bakingapp.util.NetworkUtil;
import com.facebook.stetho.Stetho;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MenuAdapter.ListItemClickListener{
    private RecyclerView mRecyclerView;
    private MenuAdapter mMenuAdapter;
    private LinearLayoutManager mLayoutManager;
    private List<String> mShrtStepDescList = null;
    private List<String> mVideoStepUrlList = null;
    private List<String> mStepDescriptionList = null;
    private List<String> mThumbnailStepUrlList = null;
    private List<String> mIngredientsList = null;
    private List<Integer> mQuantyIngredientsList = null;
    private List<String> mMeasureIngredientsList = null;

    private TextView noRecipeTextView;

    private static final int RECIPE_LOADER_ID = 1;

    private String mRecipeName;
    private ProgressBar mProgressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.menu_rv);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mMenuAdapter = new MenuAdapter(this,this);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        noRecipeTextView = (TextView) findViewById(R.id.no_recipe_tv);

        LoaderManager loaderManager = getSupportLoaderManager();
        loaderManager.initLoader(RECIPE_LOADER_ID,null,new CursorCallback());


        mRecyclerView.setAdapter(mMenuAdapter);

        Intent intent = new Intent(this, BakingSyncIntentService.class);
        Bundle mBundle = new Bundle();
        mBundle.putString("sortPref", BakingSyncTask.ACTION_FIRSTLOAD );
        intent.putExtras(mBundle);
        Stetho.initializeWithDefaults(this);
        BakingSyncUtils.startImmediateSync(this);

        update();
    }


    private void update(){
        Intent refreshDatabaseIntent = new Intent(this,BakingSyncIntentService.class);
        refreshDatabaseIntent.setAction(BakingSyncTask.ACTION_FIRSTLOAD);
        startService(refreshDatabaseIntent);
    }
    private void formatData(String response, int id){
        JSONArray stepArray;
        JSONArray ingredArray;
        try {

            mRecipeName = JsonFormat.getRecipeName(response,id);
            stepArray = JsonFormat.getStepsArray(response,id);
            ingredArray = JsonFormat.getIngredientsArray(response,id);

            mShrtStepDescList = JsonFormat.getListStepShortDescns(stepArray);
            mStepDescriptionList = JsonFormat.getListDescription(stepArray);
            mThumbnailStepUrlList = JsonFormat.getListStepThumbnailUrls(stepArray);
            mVideoStepUrlList = JsonFormat.getListStepVideoUrls(stepArray);

            mIngredientsList = JsonFormat.getIngredientList(ingredArray);
            mMeasureIngredientsList = JsonFormat.getMeasureIngredientList(ingredArray);
            mQuantyIngredientsList = JsonFormat.getQuantityIngredientList(ingredArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onListItemClick(int clickedItemIndex, String JSOnResponse) {
        Toast.makeText(this, ""+clickedItemIndex, Toast.LENGTH_SHORT).show();

        formatData(JSOnResponse,clickedItemIndex);
        Intent intent =  new Intent(MainActivity.this, DetailRecipe.class);
        intent.putStringArrayListExtra("ShrtStepDescList", (ArrayList<String>) mShrtStepDescList);
        intent.putStringArrayListExtra("StepDescriptionList", (ArrayList<String>) mStepDescriptionList);
        intent.putStringArrayListExtra("ThumbnailStepUrlList", (ArrayList<String>) mThumbnailStepUrlList);
        intent.putStringArrayListExtra("VideoStepUrlList", (ArrayList<String>) mVideoStepUrlList);
        intent.putStringArrayListExtra("IngredientsList", (ArrayList<String>) mIngredientsList);
        intent.putStringArrayListExtra("MeasureIngredientsList", (ArrayList<String>) mMeasureIngredientsList);
        intent.putIntegerArrayListExtra("QuantyIngredientsList", (ArrayList<Integer>) mQuantyIngredientsList);
        intent.putExtra("recipe_name",mRecipeName);
        startActivity(intent);
    }

    public class CursorCallback implements LoaderManager.LoaderCallbacks<Cursor>{

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            mProgressBar.setVisibility(View.VISIBLE);

            Uri recipesQueryUri = BakingContract.BakingEntry.CONTENT_URI;
            return new CursorLoader(MainActivity.this,
                    recipesQueryUri,
                    null,
                    null,
                    null,
                    null);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            mProgressBar.setVisibility(View.INVISIBLE);
            if(data==null)
            noRecipeTextView.setVisibility(View.VISIBLE);
            mMenuAdapter.swapData(data);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            mMenuAdapter.swapData(null);
        }
   }

}
