package com.mraha.weatherforecast.restapi;

import com.mraha.weatherforecast.WFApplication;
import com.mraha.weatherforecast.database.AppData;
import com.mraha.weatherforecast.pojo.CurrentMain;
import com.mraha.weatherforecast.pojo.ForecastMain;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Repository {
    public static void doCalls(double latitude, double longitude) {
        Locale locale = Locale.getDefault();
        Call<CurrentMain> currentData = RestApiHelper.initRestApiObject().getCurrentData(latitude, longitude, Locale.getDefault().getLanguage());
        Call<ForecastMain> forecastData = RestApiHelper.initRestApiObject().getForecastData(latitude, longitude, Locale.getDefault().getLanguage());
        Gson gson = new Gson();
        try {
            Response<CurrentMain> currentMainResponse = currentData.execute();
            CurrentMain currentMain = currentMainResponse.body();
            AppData appData = new AppData();
            appData.data = gson.toJson(currentMain);
            WFApplication.getInstance().getAppDatabase().appDataDAO().insertAll(appData);
            WFApplication.getInstance()
                    .getApplicationPreferences().edit()
                    .putString("dataToday", gson.toJson(currentMain))
                    .putString("dataForecast", gson.toJson(forecastData.execute().body())).apply();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean test(String city) {
        Call<CurrentMain> currentData = RestApiHelper.initRestApiObject().getCurrentDataByCity(city, Locale.getDefault().getLanguage());
        Call<ForecastMain> forecastData = RestApiHelper.initRestApiObject().getForecastDataByCity(city, Locale.getDefault().getLanguage());

        boolean res = false;
        Gson gson = new Gson();
        currentData.enqueue(new Callback<CurrentMain>() {
            @Override
            public void onResponse(Call<CurrentMain> call, Response<CurrentMain> response) {

            }

            @Override
            public void onFailure(Call<CurrentMain> call, Throwable t) {

            }
        });

        return res;
    }

    public static boolean callByCity(String city) {
        Call<CurrentMain> currentData = RestApiHelper.initRestApiObject().getCurrentDataByCity(city, Locale.getDefault().getLanguage());
        Call<ForecastMain> forecastData = RestApiHelper.initRestApiObject().getForecastDataByCity(city, Locale.getDefault().getLanguage());

        boolean res = false;
        Gson gson = new Gson();

        try {
            Response<CurrentMain> currentMainResponse = currentData.execute();
            Response<ForecastMain> forecastMainResponse = forecastData.execute();
            if (currentMainResponse.isSuccessful() && forecastMainResponse.isSuccessful()) {
                res = true;
                WFApplication.getInstance()
                        .getApplicationPreferences().edit()
                        .putString("dataToday", gson.toJson(currentMainResponse.body()))
                        .putString("dataForecast", gson.toJson(forecastMainResponse.body()))
                        .apply();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return res;
    }

    public static ForecastMain getForecastData() {
        return new Gson().fromJson(WFApplication.getInstance()
                .getApplicationPreferences().getString("dataForecast", null), ForecastMain.class);
    }

    public static CurrentMain getCurrentData() {
        return new Gson().fromJson(WFApplication.getInstance()
                .getApplicationPreferences().getString("dataToday", null), CurrentMain.class);
    }

}

