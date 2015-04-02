package com.example.andres.wobooster;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by ANDRES on 31/03/2015.
 */
public class InclinationPollService extends Service {

    private static final String CUSTOM_INTENT = "com.wobooster.BroadcastReceiver.wakeup";
    private final IntentFilter intentFilter = new IntentFilter(CUSTOM_INTENT);
    private final WakeUpScreenReceiver receiver = new WakeUpScreenReceiver();
    private LocalBroadcastManager mBroadcastMgr;

    private SensorManager sensorManager;
    private Sensor sensor;

    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;

    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }
        @Override
        public void handleMessage(Message msg) {
            // Normally we would do some work here, like download a file.
            sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
            sensorManager.registerListener(gyroListener, sensor,
                    SensorManager.SENSOR_DELAY_UI);

            mBroadcastMgr = LocalBroadcastManager
                    .getInstance(getApplicationContext());
            mBroadcastMgr.registerReceiver(receiver, intentFilter);
        }
    }

    @Override
    public void onCreate() {
        // Start up the thread running the service.  Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.  We also make it
        // background priority so CPU-intensive work will not disrupt our UI.
        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                10);//Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        mServiceHandler.sendMessage(msg);

        // If we get killed, after returning from here, restart
        return(START_STICKY);
    }

    @Override
    public void onDestroy() {
        Log.v(getClass().getSimpleName(),"onDestroy()");
        sensorManager.unregisterListener(gyroListener);
        mBroadcastMgr.unregisterReceiver(receiver);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return(null);
    }

    public SensorEventListener gyroListener = new SensorEventListener() {

        public void onAccuracyChanged(Sensor sensor, int acc) { }

        public void onSensorChanged(SensorEvent event) {

            float x = event.values[0];

            if(x < -0.1){
                Log.v(getClass().getSimpleName(),"onSensorChanged()--> "+x);
                //Toast.makeText(getApplicationContext(),"Inclinacion X", Toast.LENGTH_LONG).show();
                mBroadcastMgr.sendBroadcast(new Intent(CUSTOM_INTENT));
            }

        }
    };

}
