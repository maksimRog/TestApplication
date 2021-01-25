package com.mraha.weatherforecast;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mraha.weatherforecast.pojo.ForecastMain;
import com.mraha.weatherforecast.pojo.ForecastUnit;
import com.mraha.weatherforecast.restapi.Repository;

import org.apache.commons.lang3.StringUtils;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.MViewHolder> {
    private Context context;

    private ForecastMain forecastMain;

    public ForecastAdapter(Context context) {
        this.context = context;
        this.forecastMain = Repository.getForecastData();
    }

    @NonNull
    @Override
    public MViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_view, parent, false);
        return new MViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MViewHolder holder, int position) {
        ForecastUnit forecastUnit = forecastMain.getForecastUnit().get(position);
        String degrees = forecastUnit.getMain().getTemp() + "°";
        holder.degreesTv.setText(degrees);
        holder.textTv.setText(forecastUnit.getWeather().get(0).getDescription());
        String[] array = forecastUnit.getDtTxt().split(" ");

        holder.timeTv.setText(array[1].substring(0, 5));
        if (position == 0 || array[1].equals("00:00:00")) {
            holder.dayOfWeekTv.setVisibility(View.VISIBLE);
            holder.dayOfWeekTv.setText(StringUtils.capitalize(getDayOfWeek(array[0])));
        } else {
            holder.dayOfWeekTv.setVisibility(View.GONE);
        }

        switch (forecastUnit.getWeather().get(0).getMain().toLowerCase()) {
            case "clouds":
                holder.imageView.setImageResource(R.drawable.clouds);
                break;
            case "rain":
                holder.imageView.setImageResource(R.drawable.rain);
                break;
            case "snow":
                holder.imageView.setImageResource(R.drawable.snowing);
                break;
            case "clear":
                holder.imageView.setImageResource(R.drawable.sun);
                break;
            default:
                holder.imageView.setImageResource(R.drawable.foggy);
                break;
        }

    }

    @Override
    public int getItemCount() {
        return forecastMain.getForecastUnit().size();
    }

    public class MViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView degreesTv;
        public TextView dayOfWeekTv;
        public TextView timeTv;
        public TextView textTv;

        public MViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_view_image);
            degreesTv = itemView.findViewById(R.id.item_view_degrees);
            dayOfWeekTv = itemView.findViewById(R.id.item_view_day_of_week);
            timeTv = itemView.findViewById(R.id.item_view_time);
            textTv = itemView.findViewById(R.id.item_view_text);

        }
    }

    public static String getDayOfWeek(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date1 = null;
        try {
            date1 = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(date1);
        return calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
    }
}
