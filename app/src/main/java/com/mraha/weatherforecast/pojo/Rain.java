
package com.mraha.weatherforecast.pojo;

import com.mraha.weatherforecast.R;
import com.mraha.weatherforecast.WFApplication;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Rain {

    @SerializedName("1h")
    @Expose
    private Double _1h;
    @SerializedName("3h")
    @Expose
    private Double _3h;

    public Double get1h() {
        return _1h;
    }

    public String generatePrecipitationString() {
        if (_3h != null) {
            return _3h + WFApplication.getInstance().getString(R.string.precip_per_3hour) ;
        } else if (_1h != null) {
            return _1h + WFApplication.getInstance().getString(R.string.precip_per_hour);
        } else {
            return WFApplication.getInstance().getString(R.string.precip_def_value);
        }
    }

    public void set1h(Double _1h) {
        this._1h = _1h;
    }

    public Double get_3h() {
        return _3h;
    }

    public void set_3h(Double _3h) {
        this._3h = _3h;
    }
}
