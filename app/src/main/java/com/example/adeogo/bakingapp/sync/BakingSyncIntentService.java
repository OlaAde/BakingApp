package com.example.adeogo.bakingapp.sync;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by Adeogo on 6/23/2017.
 */

public class BakingSyncIntentService extends IntentService {

    public BakingSyncIntentService() {
        super("BakingSyncIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String action = intent.getAction();
        BakingSyncTask.syncRecipes(this,action);
    }
}
