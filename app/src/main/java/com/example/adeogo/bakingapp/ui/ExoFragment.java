package com.example.adeogo.bakingapp.ui;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import com.example.adeogo.bakingapp.R;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExoFragment extends Fragment {

    private SimpleExoPlayerView mPlayerView;
    private SimpleExoPlayer mPlayer;

    // autoplay = false
    private boolean autoPlay = false;

    // used to remember the playback position
    private int currentWindow;
    private long playbackPosition;

    // constant fields for saving and restoring bundle
    private static final String AUTOPLAY = "autoplay";
    private static final String CURRENT_WINDOW_INDEX = "current_window_index";
    private static final String PLAYBACK_POSITION = "playback_position";

    private Context mContext;

    private static String mUrlVideo = null;
    private static String mDescription = "Here we have it ncsblbhl.dg";
    private  TextView mDescTextView;
    private ImageView mThumbnailImageView;
    private String mThumbnail = null;


    public ExoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // find the view responsible for visual feedback (media controls & display)
        View rootView = inflater.inflate(R.layout.fragment_exo, container, false);
        mContext = getContext();
        mDescTextView = (TextView) rootView.findViewById(R.id.full_description_tv);
        mDescTextView.setMovementMethod(new ScrollingMovementMethod());
        mDescTextView.setText(mDescription);
        mPlayerView = (SimpleExoPlayerView) rootView.findViewById(R.id.media_view);
        mThumbnailImageView = (ImageView) rootView.findViewById(R.id.thumbnail_iv);

        if(!mUrlVideo.isEmpty() ){
            mThumbnailImageView.setVisibility(View.INVISIBLE);
            mPlayerView.setVisibility(View.VISIBLE);
        }
        else if (!mThumbnail.isEmpty()){
            mPlayerView.setVisibility(View.INVISIBLE);
            mThumbnailImageView.setVisibility(View.VISIBLE);
        }
        else
            setNoVideo();

        // if we have saved player state, restore it
        if (savedInstanceState != null) {
            playbackPosition = savedInstanceState.getLong(PLAYBACK_POSITION, 0);
            currentWindow = savedInstanceState.getInt(CURRENT_WINDOW_INDEX, 0);
            autoPlay = savedInstanceState.getBoolean(AUTOPLAY, false);
        }
        return rootView;
    }

    public void sendToExoFrag(String VideoUrl, String Description, String Thumbnail){
        mUrlVideo = VideoUrl;
        mDescription = Description;
        mThumbnail = Thumbnail;
    }

    private void setNoVideo(){
        mPlayerView.setVisibility(View.GONE);
        mThumbnailImageView.setVisibility(View.GONE);
        mDescTextView.setVisibility(View.VISIBLE);
    }

    void initializePlayer(String UrlVideo) {

        if(mUrlVideo == null&&mUrlVideo=="")
            return;
        // create a new instance of SimpleExoPlayer
        mPlayer = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(mContext),
                new DefaultTrackSelector(),
                new DefaultLoadControl());

        // attach the just created player to the view responsible for displaying the media (i.e. media controls, visual feedback)
        mPlayerView.setPlayer(mPlayer);
        mPlayer.setPlayWhenReady(autoPlay);

        // resume playback position
        mPlayer.seekTo(currentWindow, playbackPosition);

        Uri uri = Uri.parse( UrlVideo);
        MediaSource mediaSource = buildMediaSource(uri);

        // now we are ready to start playing our media files
        mPlayer.prepare(mediaSource);
    }

    private MediaSource buildMediaSource(Uri uri) {
        DefaultExtractorsFactory extractorSourceFactory = new DefaultExtractorsFactory();
        DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("ua");

        ExtractorMediaSource audioSource = new ExtractorMediaSource(uri, dataSourceFactory, extractorSourceFactory, null, null);

        // this return a single mediaSource object. i.e. no next, previous buttons to play next/prev media file
        return new ExtractorMediaSource(uri, dataSourceFactory, extractorSourceFactory, null, null);


    }


    private void releasePlayer() {
        if (mPlayer != null) {
            // save the player state before releasing its resources
            playbackPosition = mPlayer.getCurrentPosition();
            currentWindow = mPlayer.getCurrentWindowIndex();
            autoPlay = mPlayer.getPlayWhenReady();
            mPlayer.release();
            mPlayer = null;
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mPlayer == null) {
            outState.putLong(PLAYBACK_POSITION, playbackPosition);
            outState.putInt(CURRENT_WINDOW_INDEX, currentWindow);
            outState.putBoolean(AUTOPLAY, autoPlay);
        }
    }

    // This is just an implementation detail to have a pure full screen experience. Nothing fancy here
    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        View decorView = getActivity().getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }



    private boolean checkIfLandscape(){
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            return true;
        else return false;
    }
    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            if(!mUrlVideo.isEmpty())
            {
                initializePlayer(mUrlVideo);
            }
            else if(!mThumbnail.isEmpty())
            loadThumbnail(mThumbnail);
            mDescTextView.setText(mDescription);
        }
    }

    private void loadThumbnail(String Thumbnail){
        Uri uri = Uri.parse(Thumbnail);
        Picasso.with(mContext).load(uri).into(mThumbnailImageView);
    }

    @Override
    public void onResume() {
        super.onResume();
        // start in pure full screen
        if(checkIfLandscape())
            hideSystemUi();
        else mPlayerView.setMinimumHeight(1500);

        if ((Util.SDK_INT <= 23 || mPlayer == null)) {
            if(mUrlVideo.isEmpty() && !mThumbnail.isEmpty())
            {
                mUrlVideo = mThumbnail;
            }
            initializePlayer(mUrlVideo);
            mDescTextView.setText(mDescription);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

}
