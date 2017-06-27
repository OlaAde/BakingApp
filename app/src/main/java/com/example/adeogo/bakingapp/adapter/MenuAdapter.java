package com.example.adeogo.bakingapp.adapter;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Adeogo on 6/17/2017.
 */
 import com.example.adeogo.bakingapp.R;
import com.example.adeogo.bakingapp.data.BakingContract;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {

    final private ListItemClickListener mOnClickListener;
    private Context mContext;
    private Cursor mCursor = null;
    private String mResponseJSon = null;

    public MenuAdapter(Context context,ListItemClickListener mOnClickListener){
        this.mOnClickListener = mOnClickListener;
        mContext = context;
    }


    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex, String JsonResponse);
    }

    @Override
    public MenuAdapter.MenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutIdForListItem = R.layout.menu_list_item;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        MenuViewHolder viewHolder = new MenuViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MenuAdapter.MenuViewHolder holder, int position) {
        String menuIconUrl = null ;

        String menuName = null;

        int menuServing = 0;

        if(mCursor!=null){
            int menuIconIndex = mCursor.getColumnIndex(BakingContract.BakingEntry.COLUMN_IMAGE);
            int menuNameIndex = mCursor.getColumnIndex(BakingContract.BakingEntry.COLUMN_RECIPE_NAME);
            int responseIndex = mCursor.getColumnIndex(BakingContract.BakingEntry.COLUMN_RESPONSE);
            int menuServingIndex = mCursor.getColumnIndex(BakingContract.BakingEntry.COLUMN_NO_SERVINGS);

            mCursor.moveToPosition(position);
            menuIconUrl = mCursor.getString(menuIconIndex);
            menuName = mCursor.getString(menuNameIndex);
            menuServing = mCursor.getInt(menuServingIndex);
            mResponseJSon = mCursor.getString(responseIndex);

            Log.v("icon at"+ position, menuIconUrl);
        }

        holder.nameTextView.setText(menuName);
        holder.servingTextView.setText(menuServing + " "+ mContext.getString(R.string.serving_base_text));

        if(menuIconUrl.isEmpty()){
            holder.menuImageView.setImageResource(R.mipmap.ic_launcher_round);
        }
        else
        {
            Uri uri = Uri.parse(menuIconUrl);
            Picasso.with(mContext).load(uri).into(holder.menuImageView);
        }


        Log.v("menu_name at " + position, menuName);

    }

    @Override
    public int getItemCount() {
        if(mCursor==null){
            return 0;
        }
        else
        return mCursor.getCount();
    }

   public class MenuViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
      public final TextView nameTextView ;
      public final TextView servingTextView;
      public final ImageView menuImageView;

       public MenuViewHolder(View itemView) {
           super(itemView);

           nameTextView = (TextView) itemView.findViewById(R.id.menu_name_tv);
           servingTextView = (TextView) itemView.findViewById(R.id.servings_tv);
           menuImageView = (ImageView) itemView.findViewById(R.id.menu_iv);
           itemView.setOnClickListener(this);
       }

       @Override
       public void onClick(View v) {
           int clickedPosition = getAdapterPosition();
           mOnClickListener.onListItemClick(clickedPosition, mResponseJSon);

       }
   }

   public void swapData(Cursor cursor){
       if(mCursor == cursor)
           return ;
       mCursor = cursor;
       if(cursor != null)
       this.notifyDataSetChanged();
   }
}
