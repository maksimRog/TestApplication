package com.mraha.weatherforecast.fragments;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import com.mraha.weatherforecast.MainActivity;
import com.mraha.weatherforecast.R;
import com.mraha.weatherforecast.WFApplication;
import com.mraha.weatherforecast.restapi.Repository;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


public class SettingsFragment extends PreferenceFragmentCompat implements FragmentView {
    public static final String LAST_TIME_OF_UPDATE = "last_time_of_update_key";
    public static final String UPDATE_BY_CITY_KEY = "update_by_city_key";
    private Preference lastUpdateMarkPreference;
    private EditTextPreference cityEditText;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ((MainActivity)context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
        cityEditText = findPreference(UPDATE_BY_CITY_KEY);

        lastUpdateMarkPreference = findPreference(LAST_TIME_OF_UPDATE);
        setLastUpdateMark();
        setCityEditTextPreference();
        setCitySwitchPreference();


    }

    private void setLastUpdateMark() {
        if (lastUpdateMarkPreference != null) {
            String time = getPreferenceManager().getSharedPreferences().getString(LAST_TIME_OF_UPDATE, null);
            if (time == null) {
                lastUpdateMarkPreference.setSummary("");
            } else {
                lastUpdateMarkPreference.setSummary(getDateFromMillis(Long.parseLong(time)));
            }

        }
    }

    private void setCitySwitchPreference() {
        SwitchPreferenceCompat switchPreferenceCompat = findPreference("switch_pref_city");
        if (switchPreferenceCompat.isChecked()) {
            cityEditText.setVisible(true);
        } else {
            cityEditText.setVisible(false);
        }

        switchPreferenceCompat.setOnPreferenceClickListener(preference -> {
            if (switchPreferenceCompat.isChecked()) {
                cityEditText.setVisible(true);
                setSummaryAtCityET();
            } else {
                cityEditText.setVisible(false);
                getPreferenceManager().getSharedPreferences().edit().putString(UPDATE_BY_CITY_KEY, null).apply();
            }
            return true;
        });
    }
    private void setSummaryAtCityET(){
        String city = getPreferenceManager().getSharedPreferences().getString(UPDATE_BY_CITY_KEY, null);
        if (city == null) {
            cityEditText.setSummary(getString(R.string.city_not_set));
        } else {
            cityEditText.setSummary(city);
        }
    }
    private void setCityEditTextPreference() {
        setSummaryAtCityET();
        cityEditText.setOnPreferenceChangeListener((preference, newValue) -> {
            Observable.fromCallable(() ->
                    Repository.callByCity(((String) newValue).trim()))
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DisposableObserver<Boolean>() {
                        @Override
                        public void onNext(Boolean aBoolean) {
                            if (!aBoolean) {
                                Toast.makeText(WFApplication.getInstance(), R.string.not_valid_city_or_no_internet, Toast.LENGTH_LONG).show();
                            } else {
                                getPreferenceManager().getSharedPreferences().edit()
                                        .putString(UPDATE_BY_CITY_KEY, (String) newValue).apply();
                                preference.setSummary((String) newValue);
                                long time = System.currentTimeMillis();
                                getPreferenceManager().getSharedPreferences().edit()
                                        .putString(LAST_TIME_OF_UPDATE, Long.toString(time))
                                        .apply();
                                lastUpdateMarkPreference.setSummary(getDateFromMillis(time));
                                Toast.makeText(WFApplication.getInstance(), getString(R.string.data_updated), Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {
                        }

                    });

            return false;
        });
    }


    public String getDateFromMillis(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        Date date = calendar.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy '" + getString(R.string.at) + "' hh:mm:ss", Locale.getDefault());

        return formatter.format(date);
    }

    @Override
    public void initViews() {
        setLastUpdateMark();
    }
}
