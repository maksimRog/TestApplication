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

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class TodayFragment extends Fragment implements FragmentView {
    private TextView locationTV;
    private Context context;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((MainActivity) context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.context = context;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_today, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        locationTV = view.findViewById(R.id.location);
        initViews();

    }

    @Override
    public void initViews() {
        MLocationListener mLocationListener = (MLocationListener) ((MainActivity) context).getLocationListener();
        List<Address> addresses;
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        double latitude = mLocationListener.getLatitude();
        double longitude = mLocationListener.getLongitude();
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (!addresses.isEmpty()) {
                locationTV.setText("Your current coordinates:"
                        + "\n" + "Longitude is "
                        + longitude
                        + "\n" + "Latitude is " + latitude
                        + "\n" + "Your current adress is " + addresses.get(0).getAddressLine(0));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}


