package com.example.adeogo.bakingapp.ui;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.adeogo.bakingapp.R;
import com.example.adeogo.bakingapp.adapter.StepAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepFragment extends Fragment implements StepAdapter.ListItemClickListener {
    private RecyclerView mRecyclerView;
    private StepAdapter mStepAdapter;
    private LinearLayoutManager mLayoutManager;
    private Context mContext;

    private Parcelable mListState;
    private List<String> mShrtStepDescList;
    private List<String> mVideoStepUrlList;
    private List<String> mIngredientList;
    private List<String> mMeasureIngredientList;
    private List<Integer> mQuantityIngredientList;
    private List<String> mThumbnailList;
    private List<String> mDescriptionList;
    private Boolean mTwoPane = false;


    public StepFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_step, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.step_rv);

        mContext = getContext();
        mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mStepAdapter = new StepAdapter(mContext, this);
        mRecyclerView.setAdapter(mStepAdapter);

        mStepAdapter.swapData(mShrtStepDescList, mVideoStepUrlList, mTwoPane);

        if(savedInstanceState!=null){
            mLayoutManager.onRestoreInstanceState(mListState);
        }

        return rootView;
    }


    @Override
    public void onListItemClick(int clickedItemIndex) {
        if (mTwoPane) {
            ExoFragment exoFragment = new ExoFragment();
            exoFragment.sendToExoFrag(mVideoStepUrlList.get(clickedItemIndex), mDescriptionList.get(clickedItemIndex),mThumbnailList.get(clickedItemIndex));

            FragmentManager fragmentManager = getFragmentManager();

            fragmentManager.beginTransaction()
                    .replace(R.id.container_exo, exoFragment)
                    .commit();
        }

        else {
            Intent intent = new Intent(mContext, TestExo.class);
            intent.putStringArrayListExtra("videoUrlList", (ArrayList<String>) mVideoStepUrlList);
            intent.putStringArrayListExtra("DescriptionList", (ArrayList<String>) mDescriptionList);
            intent.putStringArrayListExtra("ThumbnailList", (ArrayList<String>) mThumbnailList);
            intent.putExtra("clickedId", clickedItemIndex);
            startActivity(intent);
        }
    }

    public void setData(Boolean TwoPane,String RecipeName, List<String> ThumbnailList, List<String> ShrtStepDescList, List<String> VideoStepUrlList, List<String> IngredientList, List<String> MeasureIngredientList, List<Integer> QuantityIngredientList, List<String> DescriptionList) {
        mTwoPane = TwoPane;
        mShrtStepDescList = ShrtStepDescList;
        mVideoStepUrlList = VideoStepUrlList;
        mIngredientList = IngredientList;
        mMeasureIngredientList = MeasureIngredientList;
        mQuantityIngredientList = QuantityIngredientList;
        mDescriptionList = DescriptionList;
        mThumbnailList = ThumbnailList;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mListState = mLayoutManager.onSaveInstanceState();
        outState.putParcelable("list_state", mListState);
    }


}
