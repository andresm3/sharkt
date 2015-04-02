package com.example.andres.wobooster;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;

/**
 * Created by ANDRES on 31/03/2015.
 */
public class WakeUpScreenReceiver extends BroadcastReceiver {
    private final String TAG = "WakeUpScreenReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v(TAG, "INTENT RECEIVED");

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
        wl.acquire();

        //start activity
        /*Intent i = new Intent();
        i.setClassName("com.test", "com.test.MainActivity");
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(i);*/

        wl.release();
    }
}
