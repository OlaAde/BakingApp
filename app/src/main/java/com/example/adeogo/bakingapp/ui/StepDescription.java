package com.example.adeogo.bakingapp.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.adeogo.bakingapp.R;

public class StepDescription extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_description);
        Intent intent = getIntent();
        String videoUrl = intent.getStringExtra("videoUrl");
        String description = intent.getStringExtra("Description");
    }
}
