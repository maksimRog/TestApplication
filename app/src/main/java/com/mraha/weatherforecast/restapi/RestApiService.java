package com.mraha.weatherforecast.restapi;

import com.mraha.weatherforecast.pojo.ForecastMain;
import com.mraha.weatherforecast.pojo.CurrentMain;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RestApiService {
    String APP_ID="APPID=87ac5753104c475dbafb74959735fd67";
    @GET("weather?"+APP_ID)
    Call<CurrentMain> getCurrentData(@Query("lat") double latitude, @Query("lon") double longitude,@Query("lang") String lang);
    @GET("weather?"+APP_ID)
    Call<CurrentMain> getCurrentDataByCity(@Query("q") String city,@Query("lang") String lang);
    @GET("forecast?"+APP_ID)
    Call<ForecastMain> getForecastData(@Query("lat") double latitude, @Query("lon") double longitude,@Query("lang") String lang);
    @GET("forecast?"+APP_ID)
    Call<ForecastMain> getForecastDataByCity(@Query("q") String city,@Query("lang") String lang);

}
