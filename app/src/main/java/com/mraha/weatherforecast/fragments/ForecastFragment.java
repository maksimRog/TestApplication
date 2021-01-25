package com.mraha.weatherforecast.fragments;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mraha.weatherforecast.ForecastAdapter;
import com.mraha.weatherforecast.MainActivity;
import com.mraha.weatherforecast.R;
import com.mraha.weatherforecast.WFApplication;
import com.mraha.weatherforecast.restapi.Repository;

public class ForecastFragment extends Fragment implements FragmentView {
    private Context context;
    private RecyclerView recyclerView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        ((MainActivity) context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_forecast, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recycler_view);
        initViews();

    }

    @Override
    public void initViews() {
        if (Repository.getForecastData() != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ForecastAdapter forecastAdapter = new ForecastAdapter(context,
                            WFApplication.getInstance().getAppDatabase().appDataDAO().getAll());
                    ((MainActivity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.setAdapter(forecastAdapter);
                        }
                    });
                }
            }).start();

        }

    }
}

