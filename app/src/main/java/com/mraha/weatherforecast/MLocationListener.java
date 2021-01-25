package com.mraha.weatherforecast;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.Toast;

import com.mraha.weatherforecast.fragments.SettingsFragment;
import com.mraha.weatherforecast.restapi.Repository;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MLocationListener implements LocationListener {
    private MainActivity activity;
    private double mLatitude=0;
    private double mLongitude=0;

    public double getLatitude() {
        return mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public MLocationListener(MainActivity activity) {
        this.activity = activity;
    }

    private void updateByCoordinates(double latitude, double longitude) {
        Completable.fromRunnable(() -> Repository.doCalls(latitude, longitude))
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        activity.getProgressBarDialog().show();
                    }

                    @Override
                    public void onComplete() {
                        WFApplication.getInstance()
                                .getApplicationPreferences().edit()
                                .putString(SettingsFragment.LAST_TIME_OF_UPDATE, Long.toString(System.currentTimeMillis()))
                                .apply();
                        Toast.makeText(activity, "Data updated", Toast.LENGTH_SHORT).show();
                        activity.initViews();
                        activity.initViewsInFragment();
                        activity.getProgressBarDialog().dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show();

                    }
                });

    }


    @Override
    public void onLocationChanged(Location location) {
        mLatitude=location.getLatitude();
        mLongitude=location.getLongitude();
        updateByCoordinates(location.getLatitude(), location.getLongitude());
        WFApplication.getInstance().getLocationManager().removeUpdates(this);
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
