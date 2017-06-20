package com.example.adeogo.bakingapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.adeogo.bakingapp.R;

public class StepDescription extends AppCompatActivity {


    private String videoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_description);
        Intent intent = getIntent();

        videoUrl = intent.getStringExtra("videoUrl");
        String description = intent.getStringExtra("Description");
        Log.v("Description1121", description);
        StepDescriptionFragment stepDescriptionFragment = new StepDescriptionFragment();
        stepDescriptionFragment.swapData(videoUrl,description);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.video_container_descrip, stepDescriptionFragment)
                .commit();
    }


}
