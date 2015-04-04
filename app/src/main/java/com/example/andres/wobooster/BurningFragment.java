package com.example.andres.wobooster;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.concurrent.TimeUnit;


/**
 * Created by ANDRES on 27/03/2015.
 */
public class BurningFragment extends Fragment{
    private final String TAG = this.getClass().getSimpleName();
    private String mode;
    private boolean screen_inclination;
    private boolean rep_timer;
    private Chronometer chronometer;

    //ToggleButton toggle;
    private boolean toggleButton = false;
    private Button startButton;
    private Button stopButton;

    private AlarmManager mAlarmManager;
    private Intent mNotificationReceiverIntent;
    private PendingIntent mNotificationReceiverPendingIntent;
    private static final long INITIAL_ALARM_DELAY = 3 * 60 * 1000L;
    private static final long TEN_MINUTES = 10 * 60 * 1000L;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "onCreate()");

        // Get the AlarmManager Service
        mAlarmManager = (AlarmManager) getActivity().getSystemService(getActivity().getApplicationContext().ALARM_SERVICE);

        // Create an Intent to broadcast to the AlarmNotificationReceiver
        mNotificationReceiverIntent = new Intent(getActivity(),
                AlarmNotificationReceiver.class);
        // Create an PendingIntent that holds the NotificationReceiverIntent
        mNotificationReceiverPendingIntent = PendingIntent.getBroadcast(
                getActivity(), 0, mNotificationReceiverIntent, 0);

        // Set incoming parameters
        Bundle args = getArguments();
        mode = args.getString("title_tag");
        screen_inclination = args.getBoolean("screen_inclination");
        rep_timer = args.getBoolean("rep_timer");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.v(TAG, "onCreateView()");
        View view = inflater.inflate(R.layout.fragment_burning, container, false);
        TextView title = (TextView)view.findViewById(R.id.burn_title);
        title.setText(mode);

        chronometer = (Chronometer) view.findViewById(R.id.burn_chrono_chronometer);

        startButton = (Button) view.findViewById(R.id.wo_play);
        stopButton = (Button) view.findViewById(R.id.wo_stop);


/*        if(savedInstanceState!=null){
            Toast.makeText(getActivity(),"BURNING-onCreateView(): savedInstanceState no null", Toast.LENGTH_SHORT).show();
            toggle.setChecked(savedInstanceState.getBoolean("toggleButton"));
            chronometer.setBase(System.currentTimeMillis()-savedInstanceState.getLong("time"));
        }*/
/*        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                switch (chronometer.getText().toString()) {
                    case "01:30":
                        Toast.makeText(getActivity(), "BEEEP l:30", Toast.LENGTH_LONG).show();
                        break;
                    case "02:15":
                        Toast.makeText(getActivity(), "BEEEP 2:15", Toast.LENGTH_LONG).show();
                        break;
                }

            }
        });*/

        startButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                chronometer.setBase(SystemClock.elapsedRealtime());
                chronometer.start();

                if(screen_inclination){
                    //start polling gyroscope service
                    Intent i = new Intent(getActivity(), InclinationPollService.class);
                    getActivity().startService(i);
                }

                if(rep_timer){
                    // Set repeating alarm
                    mAlarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,
                            SystemClock.elapsedRealtime() + INITIAL_ALARM_DELAY,
                            INITIAL_ALARM_DELAY,
                            mNotificationReceiverPendingIntent);

                }
                startButton.setClickable(false);
                startButton.setEnabled(false);
                stopButton.setClickable(true);
                stopButton.setEnabled(true);
            }
        });

        stopButton.setClickable(false);
        stopButton.setEnabled(false);
        stopButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogFragment();
            }
        });

        //ChronometerService.setUpdateListener(this);
        return view;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Toast.makeText(getActivity(),"BURNING-onSaveInstanceState(): "+chronometer.getBase(), Toast.LENGTH_SHORT).show();
        outState.putBoolean("toggleButton", toggleButton);
        outState.putLong("time",chronometer.getBase());
    }

    private void showDialogFragment() {

        // custom dialog
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_dialog);

        // set the custom dialog components - text, image and button
        TextView text = (TextView) dialog.findViewById(R.id.message);
        text.setText("Did you finish your workout for today?");

        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
        Button dialogButtonCancel = (Button) dialog.findViewById(R.id.dialogButtonCancel);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chronometer.stop();
                Toast.makeText(getActivity(),"FIN CHRONO: "+chronometer.getText(), Toast.LENGTH_SHORT).show();

                if(screen_inclination){
                    //stop polling
                    Intent i = new Intent(getActivity(), InclinationPollService.class);
                    getActivity().stopService(i);
                }
                if(rep_timer){
                    // Cancel all alarms using mNotificationReceiverPendingIntent
                    mAlarmManager.cancel(mNotificationReceiverPendingIntent);
                }
                startButton.setClickable(true);
                startButton.setEnabled(true);
                stopButton.setClickable(false);
                stopButton.setEnabled(false);
                dialog.dismiss();
            }
        });
        dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

/*    public void actualizarCronometro(long tiempo) {

*//*        chronoText.setText(String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(tiempo),
                TimeUnit.MILLISECONDS.toMinutes(tiempo) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(tiempo)),
                TimeUnit.MILLISECONDS.toSeconds(tiempo) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(tiempo))));*//*

        long hours = (tiempo>3600)?(tiempo / 3600):0;

        long minutes = (tiempo>60)?(tiempo % 3600) / 60:0;

        long seconds = tiempo % 60;

        chronoText.setText(String.format("%02d:%02d:%02d",hours, minutes, seconds));

    }*/
}
