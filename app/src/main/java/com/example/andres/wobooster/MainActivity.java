package com.example.andres.wobooster;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, DashboardFragment.OnHeadlineSelectedListener {

    private final String TAG = this.getClass().getSimpleName();
    private boolean screen;
    private PersonalFragment mPersonalFragment;
    private DashboardFragment mDashboardFragment;
    private BurningFragment mBurningFragment;
    private BurningFragment mPumpingFragment;

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        //Capturar Preferencias de Screen
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        screen = prefs.getBoolean(getString(R.string.pref_active_inclination_key),true);

        Toast.makeText(this,"screen pref is: "+screen, Toast.LENGTH_SHORT).show();
        //////
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

        Log.v(TAG,"onNavigationDrawerItemSelected: "+position);

        // update the main content by replacing fragments
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction()
//                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
//                .commit();
        FragmentManager fragmentManager = getFragmentManager();//getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.container, mPersonalFragment)
//                .commit();
        // execute transaction now
//        getFragmentManager().executePendingTransactions();

        switch (position+1) {
            case 1:
                if(mDashboardFragment == null)
                    mDashboardFragment = new DashboardFragment();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.container, mDashboardFragment,"Dashboard");

                fragmentTransaction.commit();
                // execute transaction now
                getFragmentManager().executePendingTransactions();
                break;
            case 2:
                if(mPersonalFragment == null)
                    mPersonalFragment = new PersonalFragment();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.container, mPersonalFragment,"Personal");

                fragmentTransaction.commit();
                // execute transaction now
                getFragmentManager().executePendingTransactions();
                break;
            case 3:
                startActivity(new Intent(this,SettingsActivity.class));
                break;
        }
    }

    public void onDashItemSelected(int position) {
        FragmentManager fragmentManager = getFragmentManager();//getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =fragmentManager.beginTransaction();
        switch (position+1) {
            case 1:
                if(mBurningFragment == null){
                    mBurningFragment = new BurningFragment();
                    Bundle args = new Bundle();
                    args.putString("title_tag", getResources().getString(R.string.burning_title));
                    args.putBoolean("screen_inclination",screen);
                    mBurningFragment.setArguments(args);
                }
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.container, mBurningFragment,"Burning");

                fragmentTransaction.commit();
                // execute transaction now
                getFragmentManager().executePendingTransactions();
                break;
            case 2:
                if(mPumpingFragment == null){
                    mPumpingFragment = new BurningFragment();
                    Bundle args = new Bundle();
                    args.putString("title_tag", getResources().getString(R.string.pump_title));
                    mPumpingFragment.setArguments(args);
                }
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.container, mPumpingFragment,"Pumping");

                fragmentTransaction.commit();
                // execute transaction now
                getFragmentManager().executePendingTransactions();
                break;
            case 3:
                Toast.makeText(this, "Seleccion: " + position, Toast.LENGTH_SHORT).show();
                break;
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }


    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        Log.v(TAG,"restoreActionBar()");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
