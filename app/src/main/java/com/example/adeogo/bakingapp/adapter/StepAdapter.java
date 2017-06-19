package com.example.adeogo.bakingapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.adeogo.bakingapp.R;

import java.util.List;

/**
 * Created by Adeogo on 6/19/2017.
 */

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewHolder> {

    final private StepAdapter.ListItemClickListener mOnClickListener;
    private Context mContext;
    private List<String> mShrtStepDescList = null;
    private List<String> mVideoStepUrlList = null;

    public StepAdapter(Context context, StepAdapter.ListItemClickListener mOnClickListener){
        this.mOnClickListener = mOnClickListener;
        mContext = context;
    }


    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    @Override
    public StepAdapter.StepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutIdForListItem = R.layout.step_list_item;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        StepAdapter.StepViewHolder viewHolder = new StepAdapter.StepViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(StepAdapter.StepViewHolder holder, int position) {
        String shortDecrip = mShrtStepDescList.get(position);
        String videoUrl = mVideoStepUrlList.get(position);

        holder.stepDescripTextView.setText(position+1 + ".  " + shortDecrip);
        if (videoUrl != "" || videoUrl!=null)
        holder.videoImageView.setImageResource(R.drawable.ic_play_arrow_black_48dp);
    }

    @Override
    public int getItemCount() {
        if(mVideoStepUrlList==null){
            Log.v("Size of Sevings List is",""+ 0 );
            return 0;
        }
        else
            return mVideoStepUrlList.size();
    }

    public class StepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView stepDescripTextView ;
        public final ImageView videoImageView;

        public StepViewHolder(View itemView) {
            super(itemView);

            stepDescripTextView = (TextView) itemView.findViewById(R.id.description_step_tv);
            videoImageView = (ImageView) itemView.findViewById(R.id.image_video_available);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }

    public void swapData(List<String> shrtStepDescList, List<String> videoStepUrlList){
        mShrtStepDescList = shrtStepDescList;
        mVideoStepUrlList = videoStepUrlList;
        this.notifyDataSetChanged();
    }

}
