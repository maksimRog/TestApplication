
package com.mraha.weatherforecast.pojo;

import com.mraha.weatherforecast.R;
import com.mraha.weatherforecast.WFApplication;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Main {

    @SerializedName("temp")
    @Expose
    private Double temp;
    @SerializedName("feels_like")
    @Expose
    private Double feelsLike;
    @SerializedName("temp_min")
    @Expose
    private Double tempMin;
    @SerializedName("temp_max")
    @Expose
    private Double tempMax;
    @SerializedName("pressure")
    @Expose
    private Integer pressure;
    @SerializedName("humidity")
    @Expose
    private Integer humidity;

    public int getTemp() {
        return (int)(temp - 273.15);
    }

    public void setTemp(Double temp) {
        this.temp = temp;
    }

    public Double getFeelsLike() {
        return feelsLike;
    }

    public void setFeelsLike(Double feelsLike) {
        this.feelsLike = feelsLike;
    }

    public Double getTempMin() {
        return tempMin;
    }

    public void setTempMin(Double tempMin) {
        this.tempMin = tempMin;
    }

    public Double getTempMax() {
        return tempMax;
    }

    public void setTempMax(Double tempMax) {
        this.tempMax = tempMax;
    }

    public String getPressure() {
        return pressure+ WFApplication.getInstance().getString(R.string.h_Pa);
    }

    public void setPressure(Integer pressure) {
        this.pressure = pressure;
    }

    public String getHumidity() {
        return humidity + "%";
    }

    public void setHumidity(Integer humidity) {
        this.humidity = humidity;
    }

}
