
package com.mraha.weatherforecast.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Calendar;

public class Sys {

    @SerializedName("type")
    @Expose
    private Integer type;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("sunrise")
    @Expose
    private Integer sunrise;
    @SerializedName("sunset")
    @Expose
    private Integer sunset;
    @SerializedName("pod")
    @Expose
    private String pod;

    public String getPod() {
        return pod;
    }

    public void setPod(String pod) {
        this.pod = pod;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getSunrise() {
        return sunrise;
    }

    public void setSunrise(Integer sunrise) {
        this.sunrise = sunrise;
    }

    public Integer getSunset() {
        return sunset;
    }

    public void setSunset(Integer sunset) {
        this.sunset = sunset;
    }

    public String generateSunriseString() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis((long)sunrise*1000);
        return "sunrise at "+calendar.get(Calendar.HOUR)+":"+calendar.get(Calendar.MINUTE)+" am";
    }
    public String generateSunsetString() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis((long)sunset*1000);
        return "sunset at "+calendar.get(Calendar.HOUR)+":"+calendar.get(Calendar.MINUTE)+" pm";
    }
}
