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
    private  String url = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    private List<String> mShrtStepDescList = null;
    private List<String> mVideoStepUrlList = null;
    private List<String> mStepDescriptionList = null;
    private List<String> mThumbnailStepUrlList = null;
    private List<String> mIngredientsList = null;
    private List<Integer> mQuantyIngredientsList = null;
    private List<String> mMeasureIngredientsList = null;

    private static final int RECIPE_LOADER_ID = 1;


    String jsonResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.menu_rv);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mMenuAdapter = new MenuAdapter(this,this);
//        TestTask testTask = new TestTask();
//        testTask.execute(url);



        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<Cursor> loader = loaderManager.getLoader(RECIPE_LOADER_ID);
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
    public void onListItemClick(int clickedItemIndex) {
        Toast.makeText(this, ""+clickedItemIndex, Toast.LENGTH_SHORT).show();
        formatData(jsonResponse,clickedItemIndex);
        Intent intent =  new Intent(MainActivity.this, DetailRecipe.class);
        intent.putStringArrayListExtra("ShrtStepDescList", (ArrayList<String>) mShrtStepDescList);
        intent.putStringArrayListExtra("StepDescriptionList", (ArrayList<String>) mStepDescriptionList);
        intent.putStringArrayListExtra("ThumbnailStepUrlList", (ArrayList<String>) mThumbnailStepUrlList);
        intent.putStringArrayListExtra("VideoStepUrlList", (ArrayList<String>) mVideoStepUrlList);
        intent.putStringArrayListExtra("IngredientsList", (ArrayList<String>) mIngredientsList);
        intent.putStringArrayListExtra("MeasureIngredientsList", (ArrayList<String>) mMeasureIngredientsList);
        intent.putIntegerArrayListExtra("QuantyIngredientsList", (ArrayList<Integer>) mQuantyIngredientsList);
        startActivity(intent);
    }

    public class CursorCallback implements LoaderManager.LoaderCallbacks<Cursor>{

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
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
            mMenuAdapter.swapData(data);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            mMenuAdapter.swapData(null);
        }
   }
//    private class TestTask extends AsyncTask<String,Void,String>{
//        @Override
//        protected String doInBackground(String... params) {
//            String response = null;
//            try {
//                 response = NetworkUtil.getResponseFromHttpUrl(NetworkUtil.buildUrl(params[0]));
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return response;
//        }
//
//        @Override
//        protected void onPostExecute(@NonNull String s) {
//            super.onPostExecute(s);
//            jsonResponse = s;
//            List<String> menuNameList = null;
//            List<String> menuIconUrlList = null;
//            List<Integer> menuServingsList = null;
//
//            try {
//                menuNameList = JsonFormat.getListName(s);
//                menuIconUrlList = JsonFormat.getListImagesUrl(s);
//                menuServingsList = JsonFormat.getListServings(s);
//
//                Log.v("Response", menuNameList.get(1));
//                Log.v("Response", menuIconUrlList.get(2));
//                Log.v("Response", menuServingsList.get(2).toString());
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            mMenuAdapter.swapData(menuNameList,menuIconUrlList,menuServingsList);
//        }
//    }
}
