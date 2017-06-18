package com.example.adeogo.bakingapp.ui;

import android.os.AsyncTask;
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

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MenuAdapter.ListItemClickListener{
    private RecyclerView mRecyclerView;
    private MenuAdapter mMenuAdapter;
    private LinearLayoutManager mLayoutManager;
    private  String url = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
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

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Toast.makeText(this, ""+clickedItemIndex, Toast.LENGTH_SHORT).show();
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
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
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
