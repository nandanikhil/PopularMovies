package com.nikhil.popularmovies.ui;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.View;

import com.nikhil.popularmovies.R;

/**
 * Created by nikhil on 21/12/15.
 */
public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.pref_general);

        bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_key)));

    }


    private void bindPreferenceSummaryToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(SettingsFragment.this);
        onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        view.setBackgroundColor(getResources().getColor(android.R.color.white));
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        String stringValue = newValue.toString();
        if (preference instanceof ListPreference) {
            // For list preferences, look up the correct display value in
            // the preference's 'entries' list (since they have separate labels/values).
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(stringValue);
            if (prefIndex >= 0) {
                preference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        }
        return true;
    }
}
