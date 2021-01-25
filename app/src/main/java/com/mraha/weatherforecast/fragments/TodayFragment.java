package com.mraha.weatherforecast.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mraha.weatherforecast.MLocationListener;
import com.mraha.weatherforecast.MainActivity;
import com.mraha.weatherforecast.R;
import com.mraha.weatherforecast.pojo.CurrentMain;
import com.mraha.weatherforecast.restapi.Repository;
import com.mraha.weatherforecast.util.LocationHelper;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class TodayFragment extends Fragment implements FragmentView {
    private TextView locationTV;
    private Context context;
    private TextView humidityTV;
    private TextView pressureTV;
    private TextView speedTV;
    private TextView directionTV;
    private TextView precipitationTV;
    private TextView degreesTV;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_today, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        humidityTV = view.findViewById(R.id.humidity);
        pressureTV = view.findViewById(R.id.pressure);
        precipitationTV = view.findViewById(R.id.precipitation);
        speedTV = view.findViewById(R.id.wind_speed);
        directionTV = view.findViewById(R.id.wind_direction);
        degreesTV = view.findViewById(R.id.degrees);
        locationTV = view.findViewById(R.id.location);
        initViews();
    }

    @Override
    public void initViews() {
        CurrentMain currentMain = Repository.getCurrentData();
        if (currentMain != null) {
            setCurrentLocation();
            degreesTV.setText(currentMain.getWeather().get(0).getDescription());
            humidityTV.setText(currentMain.getMain().getHumidity());
            pressureTV.setText(currentMain.getMain().getPressure());
            if (currentMain.getRain() == null) {
                precipitationTV.setText(getString(R.string.precip_def_value));
            } else {
                precipitationTV.setText(currentMain.getRain().generatePrecipitationString());
            }
            speedTV.setText(currentMain.getWind().getSpeed());
            directionTV.setText(currentMain.getWind().getDeg() + currentMain.getWind().getWindDirection());
            switch (currentMain.getWeather().get(0).getMain().toLowerCase()) {
                case "thunderstorm":
                    locationTV.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.storm, 0, 0);
                    break;
                case "clouds":
                    locationTV.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.clouds, 0, 0);
                    break;
                case "rain":
                    locationTV.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.rain, 0, 0);
                    break;
                case "snow":
                    locationTV.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.snowing, 0, 0);
                    break;
                case "clear":
                    locationTV.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.sun, 0, 0);
                    break;
                default:
                    locationTV.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.foggy, 0, 0);
                    break;
            }

        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((MainActivity) context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.context = context;
    }


    public void setCurrentLocation() {
        MLocationListener mLocationListener = (MLocationListener) ((MainActivity) context).getLocationListener();


        locationTV.setText(LocationHelper.
                generateAddressFromCoords(context, mLocationListener.getLatitude(), mLocationListener.getLongitude()));


    }

}


