package com.example.adeogo.bakingapp.sync;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

/**
 * Created by Adeogo on 6/23/2017.
 */

public class BakingSyncUtils {
    public static void startImmediateSync(@NonNull final Context context) {
        Intent intentToSyncImmediately = new Intent(context, BakingSyncIntentService.class);
        context.startService(intentToSyncImmediately);
    }
}
