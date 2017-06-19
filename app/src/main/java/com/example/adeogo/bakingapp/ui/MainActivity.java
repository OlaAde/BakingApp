package com.example.adeogo.bakingapp.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.adeogo.bakingapp.R;
import com.example.adeogo.bakingapp.adapter.MenuAdapter;
import com.example.adeogo.bakingapp.util.JsonFormat;
import com.example.adeogo.bakingapp.util.NetworkUtil;

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
        TestTask testTask = new TestTask();
        testTask.execute(url);
        mRecyclerView.setAdapter(mMenuAdapter);

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


    private class TestTask extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... params) {
            String response = null;
            try {
                 response = NetworkUtil.getResponseFromHttpUrl(NetworkUtil.buildUrl(params[0]));

            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(@NonNull String s) {
            super.onPostExecute(s);
            jsonResponse = s;
            List<String> menuNameList = null;
            List<String> menuIconUrlList = null;
            List<Integer> menuServingsList = null;

            try {
                menuNameList = JsonFormat.getListName(s);
                menuIconUrlList = JsonFormat.getListImagesUrl(s);
                menuServingsList = JsonFormat.getListServings(s);

                Log.v("Response", menuNameList.get(1));
                Log.v("Response", menuIconUrlList.get(2));
                Log.v("Response", menuServingsList.get(2).toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mMenuAdapter.swapData(menuNameList,menuIconUrlList,menuServingsList);
        }
    }
}
