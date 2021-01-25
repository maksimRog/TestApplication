package com.mraha.weatherforecast;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.mraha.weatherforecast.fragments.ForecastFragment;
import com.mraha.weatherforecast.fragments.FragmentView;
import com.mraha.weatherforecast.fragments.SettingsFragment;
import com.mraha.weatherforecast.fragments.TodayFragment;
import com.mraha.weatherforecast.restapi.Repository;

import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    Fragment selectedFragment;
    LocationListener locationListener;
    private SharedPreferences sharedPreferences;
    public static final String CURRENT_FRAGMENT = "current fragment";
    public static final int REQUEST_TURN_ON_LOCATION_SERVICE = 123;
    private final int REQUEST_LOCATION_PERMISSION = 124;
    String[] permissions = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    private LocationManager locationManager;
    private AlertDialog progressBarDialog;

    public LocationListener getLocationListener() {
        return locationListener;
    }

    public void initViews() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.item_today:
                    selectedFragment = new TodayFragment();
                    break;
                case R.id.item_forecast:
                    selectedFragment = new ForecastFragment();
                    break;
                case R.id.item_settings:
                    selectedFragment = new SettingsFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.fragment_container, selectedFragment, CURRENT_FRAGMENT).commit();
            return true;
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        updateDataSteps();
        sharedPreferences.edit().putString("Last known language", Locale.getDefault().getLanguage()).apply();

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (selectedFragment != null && selectedFragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, CURRENT_FRAGMENT, selectedFragment);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initObjects();
        setUpFragment(savedInstanceState);
        getSupportFragmentManager().beginTransaction().
                replace(R.id.fragment_container, selectedFragment, CURRENT_FRAGMENT).commit();

    }

    public void updateDataSteps() {

        String city = sharedPreferences.getString(SettingsFragment.UPDATE_BY_CITY_KEY, null);
        if (city != null) {
            updateByCity(city);
            return;
        }
        if (!checkPermissions()) {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_LOCATION_PERMISSION);
        } else {
            updateByLocationSteps();
        }
    }

    public void updateByLocationSteps() {
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        } else {
            doLocationRequest();
        }
    }

    public boolean isUpdateRequired() {

        String time = sharedPreferences.getString(SettingsFragment.LAST_TIME_OF_UPDATE, null);

        String updateInterval = sharedPreferences.getString("update_interval_key", null);
        long interval = 600000;
        if (updateInterval != null) {
            interval = Long.parseLong(updateInterval);
        }

        if (time == null)
            return true;

        if (!Locale.getDefault().getLanguage().equals(sharedPreferences.getString("Last known language", null)))
            return true;

        return System.currentTimeMillis() - Long.parseLong(time) > interval;
    }

    public void setUpFragment(Bundle state) {
        Fragment currentFragment = null;
        if (state != null) {
            currentFragment = getSupportFragmentManager().getFragment(state, CURRENT_FRAGMENT);
        }
        if (currentFragment != null) {
            selectedFragment = currentFragment;
        } else {
            selectedFragment = new TodayFragment();
        }
    }

    public void doLocationRequest() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true); // this is the key ingredient
        Task<LocationSettingsResponse> task = LocationServices.getSettingsClient(this).checkLocationSettings(builder.build());

        task.addOnCompleteListener(new MOnCompleteListener(this));

    }

    public void initObjects() {
        locationManager = WFApplication.getInstance().getLocationManager();
        sharedPreferences = WFApplication.getInstance().getApplicationPreferences();
        locationListener = new MLocationListener(this);
        progressBarDialog = initProgressBarDialog();
    }

    public AlertDialog getProgressBarDialog() {
        return progressBarDialog;
    }

    private AlertDialog initProgressBarDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(R.layout.progress_bar_layout);
        return builder.create();
    }

    public boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION && checkPermissions()) {
            updateByLocationSteps();
        } else {
            showSnackBarWithText(R.string.app_uses_geolocation);
            initViews();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_TURN_ON_LOCATION_SERVICE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
            switch (resultCode) {
                case RESULT_OK:

                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, permissions, REQUEST_LOCATION_PERMISSION);

                    } else {
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

                    }
                    break;
                case RESULT_CANCELED:
                    initViews();
                    initViewsInFragment();
                    showSnackBarWithText(R.string.turn_on_location_service);
                    break;
            }


        }
    }

    private void showSnackBarWithText(int textID) {
        Snackbar snackbar = Snackbar.make(findViewById(R.id.main_layout), textID, Snackbar.LENGTH_INDEFINITE);
        View view = snackbar.getView();
        TextView textView = view.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setMaxLines(6);
        snackbar.setAnchorView(R.id.bottom_nav_view);
        snackbar.setAction("ok", v -> snackbar.dismiss());
        snackbar.show();
    }

    public void initViewsInFragment() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(CURRENT_FRAGMENT);
        if (fragment != null) {
            if (fragment instanceof FragmentView) {
                ((FragmentView) fragment).initViews();
            }
        }
    }


    private void updateByCity(String city) {
        progressBarDialog.show();
        Observable.fromCallable(() -> Repository.callByCity(city))
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (!aBoolean) {
                            Toast.makeText(WFApplication.getInstance(), R.string.seems_no_internet_connection, Toast.LENGTH_LONG).show();
                        } else {
                            sharedPreferences.edit()
                                    .putString(SettingsFragment.LAST_TIME_OF_UPDATE, Long.toString(System.currentTimeMillis()))
                                    .apply();

                            Toast.makeText(WFApplication.getInstance(), R.string.data_updated, Toast.LENGTH_LONG).show();

                        }
                    }

                    @Override
                    public void onComplete() {
                        initViews();
                        initViewsInFragment();
                        progressBarDialog.dismiss();

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });
    }
}



