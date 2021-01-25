package com.mraha.weatherforecast.util;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationHelper {
    public static String generateAddressFromCoords(Context context, double lat, double lon) {
        List<Address> addresses;
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        String result = "";
        try {
            addresses = geocoder.getFromLocation(lat, lon, 1);
            if (!addresses.isEmpty()) {
                result = "Coordinates:"
                        + "\n" + "Longitude is "
                        + lon
                        + "\n" + "Latitude is " + lat
                        + "\n" + "Your current adress is " + addresses.get(0).getAddressLine(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

}