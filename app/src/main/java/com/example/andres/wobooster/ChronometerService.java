package com.example.andres.wobooster;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ANDRES on 02/04/2015.
 */
public class ChronometerService extends Service {
/*

    private Timer temporizador = new Timer();
    private static final long INTERVALO_ACTUALIZACION = 10; // En ms
    //public static BurningFragment UPDATE_LISTENER;
    private double cronometro = 0;
    //private Handler handler;

    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;
    private static final String CUSTOM_INTENT = "com.wobooster.BroadcastReceiver.chronometer";
    private final IntentFilter intentFilter = new IntentFilter(CUSTOM_INTENT);
    private LocalBroadcastManager mBroadcastMgr;
    private final ChronometerReceiver receiver = new ChronometerReceiver();

    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }
        @Override
        public void handleMessage(Message msg) {
            // Normally we would do some work here, like download a file.
            temporizador.scheduleAtFixedRate(new TimerTask() {
                public void run() {
                    cronometro += 0.01;
                    //handler.sendEmptyMessage(0);
                }
            }, 0, INTERVALO_ACTUALIZACION);

*/
/*            handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    UPDATE_LISTENER.actualizarCronometro(Math.round(cronometro));
                }
            };*//*


            mBroadcastMgr = LocalBroadcastManager
                    .getInstance(getApplicationContext());
            mBroadcastMgr.registerReceiver(receiver, intentFilter);
        }
    }

    // Establece quien va ha recibir las actualizaciones del cronometro
*/
/*    public static void setUpdateListener(BurningFragment poiService) {
        UPDATE_LISTENER = poiService;
    }*//*


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
        if (temporizador != null)
            temporizador.cancel();
        super.onDestroy();
    }

*/

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

}
