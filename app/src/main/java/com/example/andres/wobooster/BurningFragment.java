package com.example.andres.wobooster;

import android.app.AlarmManager;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private TextView chronoText;
    private boolean toggleButton;

    private static final int ALARM_ID = 1030;
    private static final int PERIOD = 500;
    private PendingIntent pi = null;
    private AlarmManager mgr = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "onCreate()");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.v(TAG, "onCreateView()");
        View view = inflater.inflate(R.layout.fragment_burning, container, false);
        final ToggleButton toggle = (ToggleButton) view.findViewById(R.id.burn_toggle_btn);
        chronoText = (TextView) view.findViewById(R.id.burn_chrono_txt);

        if(savedInstanceState!=null){
            toggle.setChecked(savedInstanceState.getBoolean("toggleButton"));
        }
        else {
            toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    //Intent i = new Intent(getActivity(), InclinationPollService.class);
                    Intent service = new Intent(getActivity(), ChronometerService.class);
                    if (isChecked) {
                        // The toggle is enabled
                        //textView.setText("01:14");

                        getActivity().startService(service);

                        //start polling gyroscope service
                        //getActivity().startService(i);


                    } else {
                        // The toggle is disabled
                        //textView.setText("1:05:32");
                        getActivity().stopService(service);

                        //stop polling
                        //getActivity().stopService(i);
                    }

                    toggleButton = toggle.isChecked();
                }
            });
            ChronometerService.setUpdateListener(this);
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("toggleButton", toggleButton);
    }

    public void actualizarCronometro(long tiempo) {

/*        chronoText.setText(String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(tiempo),
                TimeUnit.MILLISECONDS.toMinutes(tiempo) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(tiempo)),
                TimeUnit.MILLISECONDS.toSeconds(tiempo) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(tiempo))));*/

        long hours = (tiempo>3600)?(tiempo / 3600):0;

        long minutes = (tiempo>60)?(tiempo % 3600) / 60:0;

        long seconds = tiempo % 60;

        chronoText.setText(String.format("%02d:%02d:%02d",hours, minutes, seconds));

//        chronoText.setText(String.format("%02d", tiempo));
    }
}
