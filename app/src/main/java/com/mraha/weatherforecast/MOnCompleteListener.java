package com.mraha.weatherforecast;

import android.content.IntentSender;
import android.content.pm.ActivityInfo;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MOnCompleteListener implements OnCompleteListener<LocationSettingsResponse> {
    private FragmentActivity fragmentActivity;

    public MOnCompleteListener(FragmentActivity fragmentActivity) {
        this.fragmentActivity = fragmentActivity;
    }

    @Override
    public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
        try {
            LocationSettingsResponse response = task.getResult(ApiException.class);
        } catch (ApiException exception) {
            switch (exception.getStatusCode()) {
                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                    try {
                        ResolvableApiException resolvable = (ResolvableApiException) exception;
                        resolvable.startResolutionForResult(fragmentActivity, MainActivity.REQUEST_TURN_ON_LOCATION_SERVICE);
                        fragmentActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
                        break;
                    } catch (IntentSender.SendIntentException | ClassCastException e) {

                    }
                    break;
                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                    break;
            }
        }

    }
}
