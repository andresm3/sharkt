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
    //private TextView chronoText;
    private String mode;
    private boolean screen_inclination;
    private Chronometer chronometer;
    ToggleButton toggle;
    private boolean toggleButton;
    private boolean answer;

    private static final int ALARM_ID = 1030;
    private static final int PERIOD = 500;
    private PendingIntent pi = null;
    private AlarmManager mgr = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "onCreate()");
        // Set incoming parameters
        Bundle args = getArguments();
        mode = args.getString("title_tag");
        screen_inclination = args.getBoolean("screen_inclination");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.v(TAG, "onCreateView()");
        View view = inflater.inflate(R.layout.fragment_burning, container, false);
        TextView title = (TextView)view.findViewById(R.id.burn_title);
        title.setText(mode);

        toggle = (ToggleButton) view.findViewById(R.id.burn_toggle_btn);
        chronometer = (Chronometer) view.findViewById(R.id.burn_chrono_chronometer);

        if(savedInstanceState!=null){
/*            Toast.makeText(getActivity(),"BURNING-onCreateView(): savedInstanceState no null", Toast.LENGTH_SHORT).show();
            toggle.setChecked(savedInstanceState.getBoolean("toggleButton"));
            chronometer.setBase(System.currentTimeMillis()-savedInstanceState.getLong("time"));*/
        }
        else {
            toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (isChecked) {
                        // The toggle is enabled
                        //textView.setText("01:14");
                        chronometer.start();
                        //getActivity().startService(service);

                        if(screen_inclination){
                            //start polling gyroscope service
                            Intent i = new Intent(getActivity(), InclinationPollService.class);
                            //Intent service = new Intent(getActivity(), ChronometerService.class);
                            getActivity().startService(i);
                        }


                    } else {
                        // The toggle is disabled
                        //textView.setText("1:05:32");
                        //getActivity().stopService(service);
                        showDialogFragment();
                        /*if(isAnswer()){
                            chronometer.stop();
                            chronometer.setBase(0L);
                            //stop polling
                            getActivity().stopService(i);
                        }*/
                    }

                    toggleButton = toggle.isChecked();
                    Toast.makeText(getActivity(),"BURNING-onCheckedChange(): "+toggleButton, Toast.LENGTH_SHORT).show();
                }
            });
            //ChronometerService.setUpdateListener(this);
        }
        return view;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Toast.makeText(getActivity(),"BURNING-onSaveInstanceState(): "+chronometer.getBase(), Toast.LENGTH_SHORT).show();
        outState.putBoolean("toggleButton", toggleButton);
        outState.putLong("time",chronometer.getBase());
    }

/*    public boolean isAnswer() {
        Toast.makeText(getActivity(),"BURNING-isAnswer(): "+answer, Toast.LENGTH_SHORT).show();
        return answer;
    }

    public void setAnswer(boolean answer) {
        this.answer = answer;
    }*/

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

                chronometer.setBase(SystemClock.elapsedRealtime());
                //chronometer.setBase(0L);

                if(screen_inclination){
                    //stop polling
                    Intent i = new Intent(getActivity(), InclinationPollService.class);
                    getActivity().stopService(i);
                }
                dialog.dismiss();
            }
        });
        dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setAnswer(false);
                toggle.setChecked(true);
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
