package weatherwear.weatherwear;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
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

    // References to preference values
    public static final String PREFERENCE_VALUE_DISPLAY_NAME = "editTextPref_DisplayName";
    public static final String PREFERENCE_VALUE_GENDER = "listPref_Gender";
    public static final String PREFERENCE_VALUE_CURRENT_LOCATION = "checkboxPref_CurrentLocation";
    public static final String PREFERENCE_VALUE_SET_LOCATION = "editTextPref_SetLocation";
    public static final String PREFERENCE_VALUE_TEMP = "listPref_Temp";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Initialize the view, and set the title
        View rootView = inflater.inflate(R.layout.content_main, container, false);
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
        // Reload pref values
        SharedPreferences prefs = getPreferenceManager().getSharedPreferences();
        onSharedPreferenceChanged(prefs, PREFERENCE_VALUE_DISPLAY_NAME);
        onSharedPreferenceChanged(prefs, PREFERENCE_VALUE_GENDER);
        onSharedPreferenceChanged(prefs, PREFERENCE_VALUE_CURRENT_LOCATION);
        onSharedPreferenceChanged(prefs, PREFERENCE_VALUE_SET_LOCATION);
        onSharedPreferenceChanged(prefs, PREFERENCE_VALUE_TEMP);
        prefs.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key) {
            case (PREFERENCE_VALUE_DISPLAY_NAME):
                EditTextPreference editTextName = (EditTextPreference) findPreference(key);
                editTextName.setSummary(sharedPreferences.getString
                        (key, getString(R.string.prefText_YourName)));
                break;
            case (PREFERENCE_VALUE_GENDER):
                ListPreference listPrefGender = (ListPreference) findPreference(key);
                listPrefGender.setSummary(sharedPreferences.getString
                        (key, getString(R.string.prefText_Gender)));
                break;
            case (PREFERENCE_VALUE_CURRENT_LOCATION):
                final EditTextPreference setEditLocation = (EditTextPreference)
                        findPreference(PREFERENCE_VALUE_SET_LOCATION);
                CheckBoxPreference checkBoxPref = (CheckBoxPreference) findPreference(key);
                if(checkBoxPref.isChecked()){
                    setEditLocation.setEnabled(false);
                    setEditLocation.setSummary(R.string.prefText_CurrentLocation);
                } else{
                    setEditLocation.setEnabled(true);
                    setEditLocation.setSummary(getString(R.string.prefText_YourLocation));
                }
                break;
            case (PREFERENCE_VALUE_SET_LOCATION):
                EditTextPreference editTextLocation = (EditTextPreference) findPreference(key);
                editTextLocation.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        return false;
                    }
                });
                editTextLocation.setSummary(sharedPreferences.getString(key, getString(R.string.prefText_YourLocation)));
                break;
            case(PREFERENCE_VALUE_TEMP):
                ListPreference listPrefTemp = (ListPreference) findPreference(key);
                listPrefTemp.setSummary(sharedPreferences.getString(key, getString(R.string.prefText_Farenheit)));
                break;
            default:
                break;
        }
    }

}
