package com.mraha.weatherforecast;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.LocationManager;

import androidx.preference.PreferenceManager;

public class WFApplication extends Application {
    private static WFApplication wfApplication;
    private LocationManager locationManager;


    @Override
    public void onCreate() {
        super.onCreate();
        wfApplication = this;
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    public LocationManager getLocationManager() {
        return locationManager;
    }

    public static WFApplication getInstance() {
        return wfApplication;
    }

    public SharedPreferences getApplicationPreferences() {

        return PreferenceManager.getDefaultSharedPreferences(this);
    }
}
