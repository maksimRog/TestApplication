package com.mraha.weatherforecast;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mraha.weatherforecast.database.AppData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.MViewHolder> {
    private Context context;

    List<AppData> appDataList;

    public ForecastAdapter(Context context, List<AppData> appDataList) {
        this.context = context;
        this.appDataList = appDataList;
    }

    @NonNull
    @Override
    public MViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_view, parent, false);
        return new MViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MViewHolder holder, int position) {
        holder.textTv.setText("time: " + appDataList.get(position).time
                + "\n" + "data: " + appDataList.get(position).data);


    }

    @Override
    public int getItemCount() {
        return appDataList.size();
    }

    public class MViewHolder extends RecyclerView.ViewHolder {
        public TextView textTv;

        public MViewHolder(@NonNull View itemView) {
            super(itemView);
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
