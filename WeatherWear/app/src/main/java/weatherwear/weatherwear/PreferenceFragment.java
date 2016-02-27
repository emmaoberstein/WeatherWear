package weatherwear.weatherwear;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Emily on 2/16/16.
 */
public class PreferenceFragment extends android.preference.PreferenceFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.closet_activity, container, false);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setTitle(R.string.my_preferences);
        return rootView;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preference);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key) {
            case ("editTextPref_DisplayName"):
                EditTextPreference editTextName = (EditTextPreference) findPreference(key);
                editTextName.setSummary(sharedPreferences.getString
                        (key, getString(R.string.prefText_YourName)));
                break;
            case ("listPref_Gender"):
                ListPreference listPrefGender = (ListPreference) findPreference(key);
                listPrefGender.setSummary(sharedPreferences.getString
                        (key, getString(R.string.prefText_Gender)));
                break;
            case ("editTextPref_SetLocation"):
                EditTextPreference editTextLocation = (EditTextPreference) findPreference(key);
                editTextLocation.setSummary(sharedPreferences.getString
                        (key, getString(R.string.prefText_YourLocation)));
                break;
            case ("checkboxPref_CurrentLocation"):
                final EditTextPreference setEditLocation = (EditTextPreference)
                        findPreference("editTextPref_SetLocation");
                CheckBoxPreference checkBoxPref = (CheckBoxPreference) findPreference(key);
                if(checkBoxPref.isChecked()){
                    setEditLocation.setEnabled(false);
                    setEditLocation.setSummary("Current location");
                } else{
                    setEditLocation.setEnabled(true);
                    setEditLocation.setSummary(getString(R.string.prefText_YourLocation));
                }
                break;
            case("listPref_Temp"):
                ListPreference listPrefTemp = (ListPreference) findPreference(key);
                listPrefTemp.setSummary(sharedPreferences.getString(key, getString(R.string.prefText_Farenheit)));
            default:
                break;
        }
    }

}
