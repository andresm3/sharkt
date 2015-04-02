package com.example.andres.wobooster;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by ANDRES on 25/03/2015.
 */
public class PersonalFragment extends Fragment{
    private final String TAG = this.getClass().getSimpleName();

    private String name;
    private String age;
    private String height;
    private String weight;
    private String gender;
    private String gender_id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_personal, container, false);
        if(savedInstanceState != null){
            name = savedInstanceState.getString("name");
            age = savedInstanceState.getString("age");
            weight = savedInstanceState.getString("weight");
            height = savedInstanceState.getString("height");
            gender = savedInstanceState.getString("gender");
            gender_id = savedInstanceState.getString("gender_id");

            EditText nameEdit = (EditText) view.findViewById(R.id.personal_name);
            nameEdit.setText(name);
            EditText ageEdit = (EditText) view.findViewById(R.id.personal_age);
            ageEdit.setText(age);
            EditText heightEdit = (EditText) view.findViewById(R.id.personal_height);
            heightEdit.setText(height);
            EditText weightEdit = (EditText) view.findViewById(R.id.personal_weight);
            weightEdit.setText(weight);
            Spinner genderSpinner = (Spinner) view.findViewById(R.id.personal_gender);
            genderSpinner.setSelection(Integer.valueOf(gender_id));
        }
        else {
            SharedPreferences sharedPrefs =
                    PreferenceManager.getDefaultSharedPreferences(getActivity());
            String unitHeightType = sharedPrefs.getString(
                    getString(R.string.pref_hunits_key),
                    getString(R.string.pref_hunit_centimeter));
            String unitWeightType = sharedPrefs.getString(
                    getString(R.string.pref_wunits_key),
                    getString(R.string.pref_wunit_kilos));
            TextView wunit = (TextView) view.findViewById(R.id.personal_wunit);
            wunit.setText(unitWeightType);
            TextView hunit = (TextView) view.findViewById(R.id.personal_hunit);
            hunit.setText(unitHeightType);

            Button saveButton = (Button) view.findViewById(R.id.personal_save_button);
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText nameEdit = (EditText) view.findViewById(R.id.personal_name);
                    name = nameEdit.getText().toString();
                    EditText ageEdit = (EditText) view.findViewById(R.id.personal_age);
                    age = ageEdit.getText().toString();
                    EditText heightEdit = (EditText) view.findViewById(R.id.personal_height);
                    height = heightEdit.getText().toString();
                    EditText weightEdit = (EditText) view.findViewById(R.id.personal_weight);
                    weight = weightEdit.getText().toString();
                    Spinner genderSpinner = (Spinner) view.findViewById(R.id.personal_gender);
                    gender = genderSpinner.getSelectedItem().toString();
                    gender_id = String.valueOf(genderSpinner.getSelectedItemId());

                }
            });
        }

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("name", name);
        outState.putString("age", age);
        outState.putString("height", height);
        outState.putString("weight", weight);
        outState.putString("gender", gender);
        outState.putString("gender_id", gender_id);
    }
}
