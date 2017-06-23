package com.example.adeogo.bakingapp.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

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

import java.util.List;

public class TestExo extends AppCompatActivity {

    private SimpleExoPlayerView mPlayerView;
    private SimpleExoPlayer mPlayer;

    // autoplay = false
    private boolean autoPlay = false;

    // used to remember the playback position
    private int currentWindow;
    private long playbackPosition;

    // constant fields for saving and restoring bundle
    public static final String AUTOPLAY = "autoplay";
    public static final String CURRENT_WINDOW_INDEX = "current_window_index";
    public static final String PLAYBACK_POSITION = "playback_position";

    public static String urlVideo = null;
    // sample audio for testing exoplayer
    public static final String NIGERIA_NATIONAL_ANTHEM_MP3 = "http://www.noiseaddicts.com/samples_1w72b820/4237.mp3";

    // sample videos for testing exoplayer
    public static final String VIDEO_1 = "http://techslides.com/demos/sample-videos/small.mp4";
    public static final String VIDEO_2 = "http://clips.vorwaerts-gmbh.de/VfE_html5.mp4";

    private List<String> mvideoList;
    private List<String> mDescriptionList;
    private int mClickedId;
    private FragmentManager mFragmentManager;
    private Button mPreviousButton;
    private Button mNextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_exo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

         //find the view responsible for visual feedback (media controls & display)
        mPlayerView = (SimpleExoPlayerView) findViewById(R.id.media_view);
        Intent intent = getIntent();

        mClickedId = intent.getIntExtra("clickedId",0);
        mDescriptionList = intent.getStringArrayListExtra("DescriptionList");
        mvideoList = intent.getStringArrayListExtra("videoUrlList");


        mNextButton = (Button) findViewById(R.id.nextButton);

        mPreviousButton = (Button) findViewById(R.id.previousButton);
        if(mClickedId==0)
            mPreviousButton.setVisibility(View.INVISIBLE);

        if (mClickedId == mDescriptionList.size())
            mNextButton.setVisibility(View.INVISIBLE);

        urlVideo = mvideoList.get(mClickedId);
        String description = intent.getStringExtra("Description");
        ExoFragment exoFragment = new ExoFragment();
        exoFragment.sendToExoFrag(urlVideo,description);

        String recipeName = intent.getStringExtra("RecipeName");

        getSupportActionBar().setTitle(recipeName);
         mFragmentManager = getSupportFragmentManager();

        mFragmentManager.beginTransaction()
                .add(R.id.container_exo,exoFragment)
                .commit();

        // if we have saved player state, restore it
        if (savedInstanceState != null) {
            playbackPosition = savedInstanceState.getLong(PLAYBACK_POSITION, 0);
            currentWindow = savedInstanceState.getInt(CURRENT_WINDOW_INDEX, 0);
            autoPlay = savedInstanceState.getBoolean(AUTOPLAY, false);
        }

    }
    public void Previous(View view){
        if(mClickedId == 0 ){
            mPreviousButton.setVisibility(View.INVISIBLE);
        }
        else{
            mPreviousButton.setVisibility(View.VISIBLE);
            mClickedId = mClickedId - 1;
            ExoFragment exoFragment = new ExoFragment();
            exoFragment.sendToExoFrag(mvideoList.get(mClickedId),mDescriptionList.get(mClickedId));

            mFragmentManager.beginTransaction()
                    .replace(R.id.container_exo,exoFragment)
                    .commit();
        }

    }

    public void Next(View view){
        if(mClickedId==mDescriptionList.size()){
            mNextButton.setVisibility(View.INVISIBLE);
        }
        else {
            mNextButton.setVisibility(View.VISIBLE);
            mClickedId = mClickedId + 1;
            ExoFragment exoFragment = new ExoFragment();
            exoFragment.sendToExoFrag(mvideoList.get(mClickedId),mDescriptionList.get(mClickedId));

            mFragmentManager.beginTransaction()
                    .replace(R.id.container_exo,exoFragment)
                    .commit();
        }
    }

    
    void initializePlayer() {
        // create a new instance of SimpleExoPlayer
        mPlayer = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(this),
                new DefaultTrackSelector(),
                new DefaultLoadControl());

        // attach the just created player to the view responsible for displaying the media (i.e. media controls, visual feedback)
        mPlayerView.setPlayer(mPlayer);
        mPlayer.setPlayWhenReady(autoPlay);

        // resume playback position
        mPlayer.seekTo(currentWindow, playbackPosition);

        Uri uri = Uri.parse(urlVideo);
        MediaSource mediaSource = buildMediaSource(uri);

        // now we are ready to start playing our media files
        mPlayer.prepare(mediaSource);
    }

    /*
    * This method returns ExtractorMediaSource or one of its compositions
    * ExtractorMediaSource is suitable for playing regular files like (mp4, mp3, webm etc.)
    * This is appropriate for the baking app project, since all recipe videos are not in adaptive formats (i.e. HLS, Dash etc)
    */
    private MediaSource buildMediaSource(Uri uri) {
        DefaultExtractorsFactory extractorSourceFactory = new DefaultExtractorsFactory();
        DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("ua");

        ExtractorMediaSource audioSource = new ExtractorMediaSource(uri, dataSourceFactory, extractorSourceFactory, null, null);

        // this return a single mediaSource object. i.e. no next, previous buttons to play next/prev media file
        return new ExtractorMediaSource(uri, dataSourceFactory, extractorSourceFactory, null, null);

        /*
         * Uncomment the line below to play multiple meidiaSources in sequence aka playlist (and totally without buffering!)
         * NOTE: you have to comment the return statement just above this comment
         */


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
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}