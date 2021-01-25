
package com.mraha.weatherforecast.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mraha.weatherforecast.R;
import com.mraha.weatherforecast.WFApplication;

public class Wind {

    @SerializedName("speed")
    @Expose
    private double speed;
    @SerializedName("deg")
    @Expose
    private int deg;
    @SerializedName("gust")
    @Expose
    private double gust;

    public String getSpeed() {
        return speed + WFApplication.getInstance().getString(R.string.m_s);
    }

    public String getWindDirection() {
        String[] cardinals = WFApplication.getInstance()
                .getResources().getStringArray(R.array.wind_directions);
        return cardinals[(int) Math.round((((double) deg % 360) / 45)) % 8];
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    public String getDeg() {
        return deg + "°";
    }

    public void setDeg(Integer deg) {
        this.deg = deg;
    }


    public double getGust() {
        return gust;
    }

    public void setGust(double gust) {
        this.gust = gust;
    }
}
