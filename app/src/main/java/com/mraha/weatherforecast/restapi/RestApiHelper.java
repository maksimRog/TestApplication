package com.mraha.weatherforecast.restapi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestApiHelper {
    public static final String BASE_URL = "http://api.openweathermap.org/data/2.5/";
    public static RestApiService initRestApiObject(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(RestApiService.class);
    }
}
