package com.example.adeogo.bakingapp.ui;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

    private TextView noInternetTextView;

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
        noInternetTextView = (TextView) findViewById(R.id.no_internet_tv);


        mRecyclerView.setAdapter(mMenuAdapter);
        update();
        Stetho.initializeWithDefaults(this);
    }

    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        noInternetTextView.setVisibility(View.VISIBLE);
    }

    private void showRecipiesView() {
        /* First, to make sure the error is invisible */
        noInternetTextView.setVisibility(View.INVISIBLE);
        /* Then, to make sure the weather data is visible */
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void update(){

            LoaderManager loaderManager = getSupportLoaderManager();
            Loader<Cursor> loader = loaderManager.getLoader(RECIPE_LOADER_ID);
            Bundle mBundle = new Bundle();
            mBundle.putString("sortPref", BakingSyncTask.ACTION_FIRSTLOAD );

        if (isNetworkAvailable() == true) {
            showRecipiesView();
            mMenuAdapter.swapData(null);
            Intent refreshDatabaseIntent = new Intent(this,BakingSyncIntentService.class);
            refreshDatabaseIntent.setAction(BakingSyncTask.ACTION_FIRSTLOAD);
            startService(refreshDatabaseIntent);
            if(loader == null)
                loaderManager.initLoader(RECIPE_LOADER_ID,mBundle,new CursorCallback());
            else
                loaderManager.restartLoader(RECIPE_LOADER_ID,mBundle,new CursorCallback());
        } else {
            if(loader == null)
            {
                showErrorMessage();
                return;
            }
            else
                loaderManager.initLoader(RECIPE_LOADER_ID,mBundle,new CursorCallback());

        }


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
            noInternetTextView.setVisibility(View.VISIBLE);
            mMenuAdapter.swapData(data);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            mMenuAdapter.swapData(null);
        }
   }
    public boolean isNetworkAvailable() {
        boolean status = false;
        try {
            ConnectivityManager cm =
                    (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            status = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        Log.v("Internet_statussssss", "" + status);
        return status;

    }
}
